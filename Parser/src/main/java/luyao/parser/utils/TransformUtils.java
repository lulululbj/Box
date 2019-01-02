package luyao.parser.utils;

import java.nio.charset.Charset;

/**
 * Created by luyao
 * on 2017/11/13 14:20
 */

public class TransformUtils {

    private static final String hexString = "0123456789ABCDEF";
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * little endian
     * byte[] to int
     * this is for little endian
     */
    public static int bytes2Int(byte[] bytes) {
        if (null == bytes || bytes.length == 0) return 0;
        return (bytes[3] & 0XFF) << 24
                | (bytes[2] & 0xFF) << 16
                | (bytes[1] & 0xFF) << 8
                | bytes[0] & 0xFF;
    }

    public static long bytes2UnsignedInt(byte[] bytes) {
        if (null == bytes || bytes.length == 0) return 0;
        return (long)(bytes[3] & 0XFF) << 24
                | (bytes[2] & 0xFF) << 16
                | (bytes[1] & 0xFF) << 8
                | bytes[0] & 0xFF;
    }

    /**
     * int to byte[]
     */
    public static byte[] int2Bytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((i >> 24) & 0xFF);
        bytes[1] = (byte) ((i >> 16) & 0xFF);
        bytes[2] = (byte) ((i >> 8) & 0xFF);
        bytes[3] = (byte) (i & 0xFF);
        return bytes;
    }

    /**
     * byte[] to short
     */
    public static short bytes2Short(byte[] bytes) {
        if (null == bytes || bytes.length == 0) return 0;
        return (short) ((bytes[0] & 0xff) |
                ((bytes[1] & 0xff)) << 8);
    }

    /**
     * byte[] to unsigned short
     */
    public static int bytes2UnsignedShort(byte[] bytes) {
        if (null == bytes || bytes.length == 0) return 0;
        return ((bytes[0] & 0xff) |
                ((bytes[1] & 0xff)) << 8);
    }

    /**
     * short to byte[]
     */
    public static byte[] short2Bytes(short s) {
        int temp = s;
        byte[] b = new byte[2];
        for (int i = 0; i < b.length; i++) {
            b[i] = Integer.valueOf(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return b;
    }

    /**
     * byte[] to long
     */
    public static long bytes2Long(byte[] bytes) {
        if (null == bytes || bytes.length == 0) return 0;
        long s0 = bytes[0] & 0xff;
        long s1 = bytes[1] & 0xff;
        long s2 = bytes[2] & 0xff;
        long s3 = bytes[3] & 0xff;
        long s4 = bytes[4] & 0xff;
        long s5 = bytes[5] & 0xff;
        long s6 = bytes[6] & 0xff;
        long s7 = bytes[7] & 0xff;
        s0 <<= 0;
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 32;
        s5 <<= 40;
        s6 <<= 48;
        s7 <<= 56;
        return s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
    }


    /**
     * long to byte[]
     */
    public static byte[] long2Bytes(long l) {
        long temp = l;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return b;
    }


    /**
     * byte[] to hex String
     */
    public static String byte2HexStr(byte[] bytes) {
        if (null == bytes || bytes.length == 0) return "";
        StringBuilder sb = new StringBuilder("0x");
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * hex String to byte[]
     */
    public static byte[] hexStr2Bytes(String hexStr) {
        if (null == hexStr || hexStr.length() == 0) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * normal string to hex string
     */
    public static String string2Hex(String str) {
        StringBuilder builder = new StringBuilder();
        byte[] bytes = str.getBytes(Charset.forName("utf-8"));
        int bit;
        for (byte b : bytes) {
            bit = (b & 0x0F0) >> 4;
            builder.append(hexCode[bit]);
            bit = b & 0x0F;
            builder.append(hexCode[bit]);
        }
        return builder.toString().trim();
    }

    /**
     * hex string to normal string
     */
    public static String hex2String(String hex) {
        char[] hexs = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = hexString.indexOf(hexs[2 * i]) * 16;
            n += hexString.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xFF);
        }
        return new String(bytes, Charset.forName("utf-8"));
    }

    /**
     * unsigned byte[] to int[]
     */
    public static int[] bytes2Ints(byte[] b) {
        int[] ints = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            ints[i] = b[i] & 0xff;
        }
        return ints;
    }

    /**
     * reverse byte[]
     */
    public static byte[] reverseBytes(byte[] bytes) {
        int length = bytes.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length / 2; i++) {
            result[i] = bytes[length - i - 1];
            result[length - i - 1] = bytes[i];
        }
        return result;
    }

}
