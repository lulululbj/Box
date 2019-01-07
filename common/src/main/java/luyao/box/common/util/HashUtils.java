package luyao.box.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by luyao
 * on 2018/3/28 10:00
 */
public class HashUtils {

    public enum Hash {

        MD5("MD5"),

        SHA1("SHA-1"),

        SHA256("SHA-256");

        private String algorithm;

        Hash(String algorithm) {
            this.algorithm = algorithm;
        }

        public String getAlgorithm() {
            return algorithm;
        }
    }

    public static byte[] hash(byte[] data, Hash algorithm) {
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm.getAlgorithm());
            result = digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String hash(String data, Hash algorithm) {
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm.getAlgorithm());
            result = digest.digest(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TransformUtils.byte2HexStr(result);
    }

    public static String getFileHash(String filePath, Hash algorithm) {
        return getFileHash(new File(filePath), algorithm);
    }

    /**
     * 获取文件 hash
     */
    public static String getFileHash(File file, Hash algorithm) {
        if (!file.exists() || !file.isFile()) return "";
        FileInputStream fin;
        MessageDigest messageDigest;
        byte buffer[] = new byte[1024];
        int len;
        try {
            messageDigest = MessageDigest.getInstance(algorithm.algorithm);
            fin = new FileInputStream(file);
            while ((len = fin.read(buffer, 0, 1024)) != -1) {
                messageDigest.update(buffer, 0, len);
            }
            fin.close();
            BigInteger bigInt = new BigInteger(1, messageDigest.digest());
            return bigInt.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
