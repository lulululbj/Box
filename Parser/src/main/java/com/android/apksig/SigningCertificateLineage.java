/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.apksig;

import static com.android.apksig.internal.apk.ApkSigningBlockUtils.getLengthPrefixedSlice;

import com.android.apksig.apk.ApkFormatException;
import com.android.apksig.internal.apk.ApkSigningBlockUtils;
import com.android.apksig.internal.apk.SignatureAlgorithm;
import com.android.apksig.internal.apk.v3.V3SchemeSigner;
import com.android.apksig.internal.apk.v3.V3SigningCertificateLineage;
import com.android.apksig.internal.apk.v3.V3SigningCertificateLineage.SigningCertificateNode;
import com.android.apksig.internal.util.AndroidSdkVersion;
import com.android.apksig.internal.util.Pair;
import com.android.apksig.internal.util.RandomAccessFileDataSink;
import com.android.apksig.util.DataSink;
import com.android.apksig.util.DataSource;
import com.android.apksig.util.DataSources;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * APK Signer Lineage.
 *
 * <p>The signer lineage contains a history of signing certificates with each ancestor attesting to
 * the validity of its descendant.  Each additional descendant represents a new identity that can be
 * used to sign an APK, and each generation has accompanying attributes which represent how the
 * APK would like to view the older signing certificates, specifically how they should be trusted in
 * certain situations.
 *
 * <p> Its primary use is to enable APK Signing Certificate Rotation.  The Android platform verifies
 * the APK Signer Lineage, and if the current signing certificate for the APK is in the Signer
 * Lineage, and the Lineage contains the certificate the platform associates with the APK, it will
 * allow upgrades to the new certificate.
 *
 * @see <a href="https://source.android.com/security/apksigning/index.html">Application Signing</a>
 */
public class SigningCertificateLineage {

    private final static int MAGIC = 0x3eff39d1;

    private final static int FIRST_VERSION = 1;

    private static final int CURRENT_VERSION = FIRST_VERSION;

    /** accept data from already installed pkg with this cert */
    private static final int PAST_CERT_INSTALLED_DATA = 1;

    /** accept sharedUserId with pkg with this cert */
    private static final int PAST_CERT_SHARED_USER_ID = 2;

    /** grant SIGNATURE permissions to pkgs with this cert */
    private static final int PAST_CERT_PERMISSION = 4;

    /**
     * Enable updates back to this certificate.  WARNING: this effectively removes any benefit of
     * signing certificate changes, since a compromised key could retake control of an app even
     * after change, and should only be used if there is a problem encountered when trying to ditch
     * an older cert.
     */
    private static final int PAST_CERT_ROLLBACK = 8;

    /**
     * Preserve authenticator module-based access in AccountManager gated by signing certificate.
     */
    private static final int PAST_CERT_AUTH = 16;

    private final int mMinSdkVersion;

    /**
     * The signing lineage is just a list of nodes, with the first being the original signing
     * certificate and the most recent being the one with which the APK is to actually be signed.
     */
    private final List<SigningCertificateNode> mSigningLineage;

    private SigningCertificateLineage(int minSdkVersion, List<SigningCertificateNode> list) {
        mMinSdkVersion = minSdkVersion;
        mSigningLineage = list;
    }

    private static SigningCertificateLineage createSigningLineage(
            int minSdkVersion, SignerConfig parent, SignerCapabilities parentCapabilities,
            SignerConfig child, SignerCapabilities childCapabilities)
            throws CertificateEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            SignatureException {
        SigningCertificateLineage signingCertificateLineage =
                new SigningCertificateLineage(minSdkVersion, new ArrayList<>());
        signingCertificateLineage =
                signingCertificateLineage.spawnFirstDescendant(parent, parentCapabilities);
        return signingCertificateLineage.spawnDescendant(parent, child, childCapabilities);
    }

    public static SigningCertificateLineage readFromFile(File file)
            throws IOException {
        if (file == null) {
            throw new NullPointerException("file == null");
        }
        RandomAccessFile inputFile = new RandomAccessFile(file, "r");
        return readFromDataSource(DataSources.asDataSource(inputFile));
    }

    public static SigningCertificateLineage readFromDataSource(DataSource dataSource)
            throws IOException {
        if (dataSource == null) {
            throw new NullPointerException("dataSource == null");
        }
        ByteBuffer inBuff = dataSource.getByteBuffer(0, (int) dataSource.size());
        inBuff.order(ByteOrder.LITTLE_ENDIAN);
        return read(inBuff);
    }

    /**
     * Extracts a Signing Certificate Lineage from a v3 signer proof-of-rotation attribute.
     *
     * <note>
     *     this may not give a complete representation of an APK's signing certificate history,
     *     since the APK may have multiple signers corresponding to different platform versions.
     *     Use <code> readFromApkFile</code> to handle this case.
     * </note>
     * @param attrValue
     */
    public static SigningCertificateLineage readFromV3AttributeValue(byte[] attrValue)
            throws IOException {
        List<SigningCertificateNode> parsedLineage =
                V3SigningCertificateLineage.readSigningCertificateLineage(ByteBuffer.wrap(
                        attrValue));
        int minSdkVersion = calculateMinSdkVersion(parsedLineage);
        return  new SigningCertificateLineage(minSdkVersion, parsedLineage);
    }

    public static SigningCertificateLineage readFromApkFile(File apkFile) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void writeToFile(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("file == null");
        }
        RandomAccessFile outputFile = new RandomAccessFile(file, "rw");
        writeToDataSink(new RandomAccessFileDataSink(outputFile));
    }

    public void writeToDataSink(DataSink dataSink) throws IOException {
        if (dataSink == null) {
            throw new NullPointerException("dataSink == null");
        }
        dataSink.consume(write());
    }

    /**
     * Add a new signing certificate to the lineage.  This effectively creates a signing certificate
     * rotation event, forcing APKs which include this lineage to be signed by the new signer. The
     * flags associated with the new signer are set to a default value.
     *
     * @param parent current signing certificate of the containing APK
     * @param child new signing certificate which will sign the APK contents
     */
    public SigningCertificateLineage spawnDescendant(SignerConfig parent, SignerConfig child)
            throws CertificateEncodingException, InvalidKeyException, NoSuchAlgorithmException,
            SignatureException {
        if (parent == null || child == null) {
            throw new NullPointerException("can't add new descendant to lineage with null inputs");
        }
        SignerCapabilities signerCapabilities = new SignerCapabilities.Builder().build();
        return spawnDescendant(parent, child, signerCapabilities);
    }

    /**
     * Add a new signing certificate to the lineage.  This effectively creates a signing certificate
     * rotation event, forcing APKs which include this lineage to be signed by the new signer.
     *
     * @param parent current signing certificate of the containing APK
     * @param child new signing certificate which will sign the APK contents
     * @param childCapabilities flags
     */
    public SigningCertificateLineage spawnDescendant(
            SignerConfig parent, SignerConfig child, SignerCapabilities childCapabilities)
            throws CertificateEncodingException, InvalidKeyException,
            NoSuchAlgorithmException, SignatureException {
        if (parent == null) {
            throw new NullPointerException("parent == null");
        }
        if (child == null) {
            throw new NullPointerException("child == null");
        }
        if (childCapabilities == null) {
            throw new NullPointerException("childCapabilities == null");
        }
        if (mSigningLineage.isEmpty()) {
            throw new IllegalArgumentException("Cannot spawn descendant signing certificate on an"
                    + " empty SigningCertificateLineage: no parent node");
        }

        // make sure that the parent matches our newest generation (leaf node/sink)
        SigningCertificateNode currentGeneration = mSigningLineage.get(mSigningLineage.size() - 1);
        if (!Arrays.equals(currentGeneration.signingCert.getEncoded(),
                parent.getCertificate().getEncoded())) {
            throw new IllegalArgumentException("SignerConfig Certificate containing private key"
                    + " to sign the new SigningCertificateLineage record does not match the"
                    + " existing most recent record");
        }

        // create data to be signed, including the algorithm we're going to use
        SignatureAlgorithm signatureAlgorithm = getSignatureAlgorithm(parent);
        ByteBuffer prefixedSignedData = ByteBuffer.wrap(
                V3SigningCertificateLineage.encodeSignedData(
                        child.getCertificate(), signatureAlgorithm.getId()));
        prefixedSignedData.position(4);
        ByteBuffer signedDataBuffer = ByteBuffer.allocate(prefixedSignedData.remaining());
        signedDataBuffer.put(prefixedSignedData);
        byte[] signedData = signedDataBuffer.array();

        // create SignerConfig to do the signing
        List<X509Certificate> certificates = new ArrayList<>(1);
        certificates.add(parent.getCertificate());
        ApkSigningBlockUtils.SignerConfig newSignerConfig =
                new ApkSigningBlockUtils.SignerConfig();
        newSignerConfig.privateKey = parent.getPrivateKey();
        newSignerConfig.certificates = certificates;
        newSignerConfig.signatureAlgorithms = Collections.singletonList(signatureAlgorithm);

        // sign it
        List<Pair<Integer, byte[]>> signatures =
                ApkSigningBlockUtils.generateSignaturesOverData(newSignerConfig, signedData);

        // finally, add it to our lineage
        SignatureAlgorithm sigAlgorithm = SignatureAlgorithm.findById(signatures.get(0).getFirst());
        byte[] signature = signatures.get(0).getSecond();
        currentGeneration.sigAlgorithm = sigAlgorithm;
        SigningCertificateNode childNode =
                new SigningCertificateNode(
                        child.getCertificate(), sigAlgorithm, null,
                        signature, childCapabilities.getFlags());
        List<SigningCertificateNode> lineageCopy = new ArrayList<>(mSigningLineage);
        lineageCopy.add(childNode);
        return new SigningCertificateLineage(mMinSdkVersion, lineageCopy);
    }

    /**
     * The number of signing certificates in the lineage, including the current signer, which means
     * this value can also be used to V2determine the number of signing certificate rotations by
     * subtracting 1.
     */
    public int size() {
        return mSigningLineage.size();
    }

    private SignatureAlgorithm getSignatureAlgorithm(SignerConfig parent)
            throws InvalidKeyException {
        PublicKey publicKey = parent.getCertificate().getPublicKey();

        // TODO switch to one signature algorithm selection, or add support for multiple algorithms
        List<SignatureAlgorithm> algorithms = V3SchemeSigner.getSuggestedSignatureAlgorithms(
                publicKey, mMinSdkVersion, false /* padding support */);
        return algorithms.get(0);
    }

    private SigningCertificateLineage spawnFirstDescendant(
            SignerConfig parent, SignerCapabilities signerCapabilities) {
        if (!mSigningLineage.isEmpty()) {
            throw new IllegalStateException("SigningCertificateLineage already has its first node");
        }

        // check to make sure that the public key for the first node is acceptable for our minSdk
        try {
            getSignatureAlgorithm(parent);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Algorithm associated with first signing certificate"
                    + " invalid on desired platform versions", e);
        }

        // create "fake" signed data (there will be no signature over it, since there is no parent
        SigningCertificateNode firstNode = new SigningCertificateNode(
                parent.getCertificate(), null, null, new byte[0], signerCapabilities.getFlags());
        return new SigningCertificateLineage(mMinSdkVersion, Collections.singletonList(firstNode));
    }

    private static SigningCertificateLineage read(ByteBuffer inputByteBuffer)
            throws IOException {
        ApkSigningBlockUtils.checkByteOrderLittleEndian(inputByteBuffer);
        if (inputByteBuffer.remaining() < 8) {
            throw new IllegalArgumentException(
                    "Improper SigningCertificateLineage format: insufficient data for header.");
        }

        if (inputByteBuffer.getInt() != MAGIC) {
            throw new IllegalArgumentException(
                    "Improper SigningCertificateLineage format: MAGIC header mismatch.");
        }
        return read(inputByteBuffer, inputByteBuffer.getInt());
    }

    private static SigningCertificateLineage read(ByteBuffer inputByteBuffer, int version)
            throws IOException {
        switch (version) {
            case FIRST_VERSION:
                try {
                    List<SigningCertificateNode> nodes =
                            V3SigningCertificateLineage.readSigningCertificateLineage(
                                    getLengthPrefixedSlice(inputByteBuffer));
                    int minSdkVersion = calculateMinSdkVersion(nodes);
                    return new SigningCertificateLineage(minSdkVersion, nodes);
                } catch (ApkFormatException e) {
                    // unable to get a proper length-prefixed lineage slice
                    throw new IOException("Unable to read list of signing certificate nodes in "
                            + "SigningCertificateLineage", e);
                }
            default:
                throw new IllegalArgumentException(
                        "Improper SigningCertificateLineage format: unrecognized version.");
        }
    }

    private static int calculateMinSdkVersion(List<SigningCertificateNode> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Can't calculate minimum SDK version of null nodes");
        }
        int minSdkVersion = AndroidSdkVersion.P; // lineage introduced in P
        for (SigningCertificateNode node : nodes) {
            if (node.sigAlgorithm != null) {
                int nodeMinSdkVersion = node.sigAlgorithm.getMinSdkVersion();
                if (nodeMinSdkVersion > minSdkVersion) {
                    minSdkVersion = nodeMinSdkVersion;
                }
            }
        }
        return minSdkVersion;
    }

    private ByteBuffer write() {
        byte[] encodedLineage =
                V3SigningCertificateLineage.encodeSigningCertificateLineage(mSigningLineage);
        int payloadSize = 4 + 4 + 4 + encodedLineage.length;
        ByteBuffer result = ByteBuffer.allocate(payloadSize);
        result.order(ByteOrder.LITTLE_ENDIAN);
        result.putInt(MAGIC);
        result.putInt(CURRENT_VERSION);
        result.putInt(encodedLineage.length);
        result.put(encodedLineage);
        return result;
    }

    public byte[] generateV3SignerAttribute() {
        // FORMAT (little endian):
        // * length-prefixed bytes: attribute pair
        //   * uint32: ID
        //   * bytes: value - encoded V3 SigningCertificateLineage
        byte[] encodedLineage =
                V3SigningCertificateLineage.encodeSigningCertificateLineage(mSigningLineage);
        int payloadSize = 4 + 4 + encodedLineage.length;
        ByteBuffer result = ByteBuffer.allocate(payloadSize);
        result.order(ByteOrder.LITTLE_ENDIAN);
        result.putInt(4 + encodedLineage.length);
        result.putInt(V3SchemeSigner.PROOF_OF_ROTATION_ATTR_ID);
        result.put(encodedLineage);
        return result.array();
    }

    public List<DefaultApkSignerEngine.SignerConfig> sortSignerConfigs(
            List<DefaultApkSignerEngine.SignerConfig> signerConfigs) {
        if (signerConfigs == null) {
            throw new NullPointerException("signerConfigs == null");
        }

        // not the most elegant sort, but we expect signerConfigs to be quite small (1 or 2 signers
        // in most cases) and likely already sorted, so not worth the overhead of doing anything
        // fancier
        List<DefaultApkSignerEngine.SignerConfig> sortedSignerConfigs =
                new ArrayList<>(signerConfigs.size());
        for (int i = 0; i < mSigningLineage.size(); i++) {
            for (int j = 0; j < signerConfigs.size(); j++) {
                DefaultApkSignerEngine.SignerConfig config = signerConfigs.get(j);
                if (mSigningLineage.get(i).signingCert.equals(config.getCertificates().get(0))) {
                    sortedSignerConfigs.add(config);
                    break;
                }
            }
        }
        if (sortedSignerConfigs.size() != signerConfigs.size()) {
            throw new IllegalArgumentException("SignerConfigs supplied which are not present in the"
                    + " SigningCertificateLineage");
        }
        return sortedSignerConfigs;
    }

    // TODO add API to return all signing certificate(s)

    // TODO add API to query if given signing certificate is in set of signing certificates

    // TODO add API to modify flags corresponding to a given signing certificate

    private static int calculateDefaultFlags() {
        return PAST_CERT_INSTALLED_DATA | PAST_CERT_PERMISSION
                | PAST_CERT_SHARED_USER_ID | PAST_CERT_AUTH;
    }

    /**
     * Returns a new SigingCertificateLineage which terminates at the node corresponding to the
     * given certificate.  This is useful in the event of rotating to a new signing algorithm that
     * is only supported on some platform versions.  It enables a v3 signature to be generated using
     * this signing certificate and the shortened proof-of-rotation record from this sub lineage in
     * conjunction with the appropriate SDK version values.
     *
     * @param x509Certificate the signing certificate for which to search
     * @return A new SigningCertificateLineage if present, or null otherwise.
     */
    public SigningCertificateLineage getSubLineage(X509Certificate x509Certificate) {
        if (x509Certificate == null) {
            throw new NullPointerException("x509Certificate == null");
        }
        for (int i = 0; i < mSigningLineage.size(); i++) {
            if (mSigningLineage.get(i).signingCert.equals(x509Certificate)) {
                return new SigningCertificateLineage(
                        mMinSdkVersion, new ArrayList<>(mSigningLineage.subList(0, i + 1)));
            }
        }

        // looks like we didn't find the cert,
        throw new IllegalArgumentException("Certificate not found in SigningCertificateLineage");
    }

    /**
     * Consolidates all of the lineages found in an APK into one lineage, which is the longest one.
     * In so doing, it also checks that all of the smaller lineages are contained in the largest,
     * and that they properly cover the desired platform ranges.
     *
     * An APK may contain multiple lineages, one for each signer, which correspond to different
     * supported platform versions.  In this event, the lineage(s) from the earlier platform
     * version(s) need to be present in the most recent (longest) one to make sure that when a
     * platform version changes.
     *
     * <note> This does not verify that the largest lineage corresponds to the most recent supported
     * platform version.  That check requires is performed during v3 verification. </note>
     */
    public static SigningCertificateLineage consolidateLineages(
            List<SigningCertificateLineage> lineages) {
        if (lineages == null || lineages.isEmpty()) {
            return null;
        }
        int largestIndex = 0;
        int maxSize = 0;

        // determine the longest chain
        for (int i = 0; i < lineages.size(); i++) {
            int curSize = lineages.get(i).size();
            if (curSize > maxSize) {
                largestIndex = i;
                maxSize = curSize;
            }
        }

        List<SigningCertificateNode> largestList = lineages.get(largestIndex).mSigningLineage;
        // make sure all other lineages fit into this one, with the same capabilities
        for (int i = 0; i < lineages.size(); i++) {
            if (i == largestIndex) {
                continue;
            }
            List<SigningCertificateNode> underTest = lineages.get(i).mSigningLineage;
            if (!underTest.equals(largestList.subList(0, underTest.size()))) {
                throw new IllegalArgumentException("Inconsistent SigningCertificateLineages. "
                        + "Not all lineages are subsets of each other.");
            }
        }

        // if we've made it this far, they all check out, so just return the largest
        return lineages.get(largestIndex);
    }

    /**
     * Representation of the capabilities the APK would like to grant to its old signing
     * certificates.  The {@code SigningCertificateLineage} provides two conceptual data structures.
     *   1) proof of rotation - Evidence that other parties can trust an APK's current signing
     *      certificate if they trust an older one in this lineage
     *   2) self-trust - certain capabilities may have been granted by an APK to other parties based
     *      on its own signing certificate.  When it changes its signing certificate it may want to
     *      allow the other parties to retain those capabilities.
     * {@code SignerCapabilties} provides a representation of the second structure.
     *
     * <p>Use {@link Builder} to obtain configuration instances.
     */
    public static class SignerCapabilities {
        private final int mFlags;

        private SignerCapabilities(int flags) {
            mFlags = flags;
        }

        private int getFlags() {
            return mFlags;
        }

        /**
         * Builder of {@link SignerCapabilities} instances.
         */
        public static class Builder {
            private int mFlags;

            /**
             * Constructs a new {@code Builder}.
             */
            public Builder() {
                mFlags = calculateDefaultFlags();
            }

            /**
             * Set the {@code PAST_CERT_INSTALLED_DATA} flag in this capabilities object.  This flag
             * is used by the platform to determine if installed data associated with previous
             * signing certificate should be trusted.  In particular, this capability is required to
             * perform signing certificate rotation during an upgrade on-device.  Without it, the
             * platform will not permit the app data from the old signing certificate to
             * propagate to the new version.  Typically, this flag should be set to enable signing
             * certificate rotation, and may be unset later when the app developer is satisfied that
             * their install base is as migrated as it will be.
             */
            public Builder setInstalledData(boolean enabled) {
                if (enabled) {
                    mFlags |= PAST_CERT_INSTALLED_DATA;
                } else {
                    mFlags &= ~PAST_CERT_INSTALLED_DATA;
                }
                return this;
            }

            /**
             * Set the {@code PAST_CERT_SHARED_USER_ID} flag in this capabilities object.  This flag
             * is used by the platform to determine if this app is willing to be sharedUid with
             * other apps which are still signed with the associated signing certificate.  This is
             * useful in situations where sharedUserId apps would like to change their signing
             * certificate, but can't guarantee the order of updates to those apps.
             */
            public Builder setSharedUid(boolean enabled) {
                if (enabled) {
                    mFlags |= PAST_CERT_SHARED_USER_ID;
                } else {
                    mFlags &= ~PAST_CERT_SHARED_USER_ID;
                }
                return this;
            }

            /**
             * Set the {@code PAST_CERT_PERMISSION} flag in this capabilities object.  This flag
             * is used by the platform to determine if this app is willing to grant SIGNATURE
             * permissions to apps signed with the associated signing certificate.  Without this
             * capability, an application signed with the older certificate will not be granted the
             * SIGNATURE permissions defined by this app.  In addition, if multiple apps define the
             * same SIGNATURE permission, the second one the platform sees will not be installable
             * if this capability is not set and the signing certificates differ.
             */
            public Builder setPermission(boolean enabled) {
                if (enabled) {
                    mFlags |= PAST_CERT_PERMISSION;
                } else {
                    mFlags &= ~PAST_CERT_PERMISSION;
                }
                return this;
            }

            /**
             * Set the {@code PAST_CERT_ROLLBACK} flag in this capabilities object.  This flag
             * is used by the platform to determine if this app is willing to upgrade to a new
             * version that is signed by one of its past signing certificates.
             *
             * <note> WARNING: this effectively removes any benefit of signing certificate changes,
             * since a compromised key could retake control of an app even after change, and should
             * only be used if there is a problem encountered when trying to ditch an older cert
             * </note>
             */
            public Builder setRollback(boolean enabled) {
                if (enabled) {
                    mFlags |= PAST_CERT_ROLLBACK;
                } else {
                    mFlags &= ~PAST_CERT_ROLLBACK;
                }
                return this;
            }

            /**
             * Set the {@code PAST_CERT_AUTH} flag in this capabilities object.  This flag
             * is used by the platform to determine whether or not privileged access based on
             * authenticator module signing certificates should be granted.
             */
            public Builder setAuth(boolean enabled) {
                if (enabled) {
                    mFlags |= PAST_CERT_AUTH;
                } else {
                    mFlags &= ~PAST_CERT_AUTH;
                }
                return this;
            }

            /**
             * Returns a new {@code SignerConfig} instance configured based on the configuration of
             * this builder.
             */
            public SignerCapabilities build() {
                return new SignerCapabilities(mFlags);
            }
        }
    }

    /**
     * Configuration of a signer.  Used to add a new entry to the {@link SigningCertificateLineage}
     *
     * <p>Use {@link Builder} to obtain configuration instances.
     */
    public static class SignerConfig {
        private final PrivateKey mPrivateKey;
        private final X509Certificate mCertificate;

        private SignerConfig(
                PrivateKey privateKey,
                X509Certificate certificate) {
            mPrivateKey = privateKey;
            mCertificate = certificate;
        }

        /**
         * Returns the signing key of this signer.
         */
        public PrivateKey getPrivateKey() {
            return mPrivateKey;
        }

        /**
         * Returns the certificate(s) of this signer. The first certificate's public key corresponds
         * to this signer's private key.
         */
        public X509Certificate getCertificate() {
            return mCertificate;
        }

        /**
         * Builder of {@link SignerConfig} instances.
         */
        public static class Builder {
            private final PrivateKey mPrivateKey;
            private final X509Certificate mCertificate;

            /**
             * Constructs a new {@code Builder}.
             *
             * @param privateKey signing key
             * @param certificate the X.509 certificate with a subject public key of the
             * {@code privateKey}.
             */
            public Builder(
                    PrivateKey privateKey,
                    X509Certificate certificate) {
                mPrivateKey = privateKey;
                mCertificate = certificate;
            }

            /**
             * Returns a new {@code SignerConfig} instance configured based on the configuration of
             * this builder.
             */
            public SignerConfig build() {
                return new SignerConfig(
                        mPrivateKey,
                        mCertificate);
            }
        }
    }

    /**
     * Builder of {@link SigningCertificateLineage} instances.
     */
    public static class Builder {
        private final SignerConfig mOriginalSignerConfig;
        private final SignerConfig mNewSignerConfig;
        private SignerCapabilities mOriginalCapabilities;
        private SignerCapabilities mNewCapabilities;
        private int mMinSdkVersion;
        /**
         * Constructs a new {@code Builder}.
         *
         * @param originalSignerConfig first signer in this lineage, parent of the next
         * @param newSignerConfig new signer in the lineage; the new signing key that the APK will
         *                        use
         */
        public Builder(
                SignerConfig originalSignerConfig,
                SignerConfig newSignerConfig) {
            if (originalSignerConfig == null || newSignerConfig == null) {
                throw new NullPointerException("Can't pass null SignerConfigs when constructing a "
                        + "new SigningCertificateLineage");
            }
            mOriginalSignerConfig = originalSignerConfig;
            mNewSignerConfig = newSignerConfig;
        }

        /**
         * Sets the minimum Android platform version (API Level) on which this lineage is expected
         * to validate.  It is possible that newer signers in the lineage may not be recognized on
         * the given platform, but as long as an older signer is, the lineage can still be used to
         * sign an APK for the given platform.
         *
         * <note> By default, this value is set to the value for the
         * P release, since this structure was created for that release, and will also be set to
         * that value if a smaller one is specified. </note>
         */
        public Builder setMinSdkVersion(int minSdkVersion) {
            mMinSdkVersion = minSdkVersion;
            return this;
        }

        /**
         * Sets capabilities to give {@code mOriginalSignerConfig}. These capabilities allow an
         * older signing certificate to still be used in some situations on the platform even though
         * the APK is now being signed by a newer signing certificate.
         */
        public Builder setOriginalCapabilities(SignerCapabilities signerCapabilities) {
            if (signerCapabilities == null) {
                throw new NullPointerException("signerCapabilities == null");
            }
            mOriginalCapabilities = signerCapabilities;
            return this;
        }

        /**
         * Sets capabilities to give {@code mNewSignerConfig}. These capabilities allow an
         * older signing certificate to still be used in some situations on the platform even though
         * the APK is now being signed by a newer signing certificate.  By default, the new signer
         * will have all capabilities, so when first switching to a new signing certificate, these
         * capabilities have no effect, but they will act as the default level of trust when moving
         * to a new signing certificate.
         */
        public Builder setNewCapabilities(SignerCapabilities signerCapabilities) {
            if (signerCapabilities == null) {
                throw new NullPointerException("signerCapabilities == null");
            }
            mNewCapabilities = signerCapabilities;
            return this;
        }

        public SigningCertificateLineage build()
                throws CertificateEncodingException, InvalidKeyException, NoSuchAlgorithmException,
                SignatureException {
            if (mMinSdkVersion < AndroidSdkVersion.P) {
                mMinSdkVersion = AndroidSdkVersion.P;
            }

            if (mOriginalCapabilities == null) {
                mOriginalCapabilities = new SignerCapabilities.Builder().build();
            }

            if (mNewCapabilities == null) {
                mNewCapabilities = new SignerCapabilities.Builder().build();
            }

            return createSigningLineage(
                    mMinSdkVersion, mOriginalSignerConfig, mOriginalCapabilities,
                    mNewSignerConfig, mNewCapabilities);
        }
    }
}
