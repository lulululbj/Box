package luyao.parser.utils;

import luyao.parser.dex.DexParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luyao
 * on 2018/4/27 14:43
 */
public class Utils {

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String bytes2hex(byte[] bytes) {
        StringBuilder r = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
        return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | (b4 & 0xFF);
    }

    public static int fromBytes(byte b1, byte b2) {
        return b1 & 0xff | (b2 & 0xFF) << 8;
    }

    public static byte[] copy(byte[] stringContent, int start, int length) {
        byte[] b = new byte[length];
        System.arraycopy(stringContent, start, b, 0, length);
        return b;
    }

    public static int byte2Short(byte[] bytes) {
        return fromBytes(bytes[0], bytes[1]);
    }

    public static int byte2Int(byte[] bytes) {
        return fromBytes(bytes[3], bytes[2], bytes[1], bytes[0]);
    }

    public static byte[] reverseByte(byte[] bytes) {
        int length = bytes.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length / 2; i++) {
            result[i] = bytes[length - i - 1];
            result[length - i - 1] = bytes[i];
        }
        return result;
    }

    public static byte[] moveBlank(byte[] data) {
        List<Byte> byteList = new ArrayList<>();
        for (Byte b : data) {
            if (b != 0) byteList.add(b);
        }
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = byteList.get(i);
        return result;
    }

    public static byte[] readAll(File file) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream in = new FileInputStream(file);
            BufferedInputStream bi = new BufferedInputStream(in);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = bi.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readAll(InputStream in) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedInputStream bi = new BufferedInputStream(in);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = bi.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int readUnsignedLeb128(byte[] src, int offset) {
        int result = 0;
        int count = 0;
        int cur;
        do {
            cur = copy(src, offset, 1)[0];
            cur &= 0xff;
            result |= (cur & 0x7f) << count * 7;
            count++;
            offset++;
            DexParser.POSITION++;
        } while ((cur & 0x80) == 128 && count < 5);
        return result;
    }

}
