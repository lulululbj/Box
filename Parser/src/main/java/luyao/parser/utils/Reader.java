package luyao.parser.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static luyao.parser.utils.TransformUtils.reverseBytes;


/**
 * Created by luyao
 * on 2018/8/27 10:26
 */
public class Reader {

    private volatile InputStream in;
    private byte[] buffer = new byte[8];
    private static boolean showLog = true;
    private boolean isLittleEndian = true;

    public Reader(InputStream in) {
        this(in, true);
    }

    public Reader(InputStream in, boolean isLittleEndian) {
        this.in = in;
        this.isLittleEndian = isLittleEndian;
    }

    public byte[] read(int count) throws IOException {
        byte[] b = new byte[count];
        int read = in.read(b);
        if (read == -1) throw new EOFException();
        if (isLittleEndian) return b;
        else {
            return reverseBytes(b);
        }
    }

    public byte[] readBig(int count) throws IOException {
        byte[] b = new byte[count];
        int read = in.read(b);
        if (read == -1) throw new EOFException();
        if (!isLittleEndian) return b;
        else {
            return reverseBytes(b);
        }
    }


    public byte[] read(byte[] buffer) throws IOException {
        int read = in.read(buffer);
        if (read == -1) throw new EOFException();
        if (isLittleEndian) return buffer;
        else {
            buffer = reverseBytes(buffer);
            return buffer;
        }
    }

    public byte[] readOrigin(int count) throws IOException {
        byte[] b = new byte[count];
        int read = in.read(b);
        if (read == -1) throw new EOFException();
        else return b;
    }

    public byte readByte() throws IOException {
        int b = in.read();
        if (-1 == b) throw new EOFException();
        return (byte) b;
    }

    public int readUnsignedByte() throws IOException {
        int b = in.read();
        if (-1 == b) throw new EOFException();
        return (byte) b;
    }

    public short readShort() throws IOException {
        return TransformUtils.bytes2Short(read(2));
    }

    public int readUnsignedShort() throws IOException {
        return TransformUtils.bytes2UnsignedShort(read(2));
    }

    public int readInt() throws IOException {
        byte[] ints = read(4);
        return TransformUtils.bytes2Int(ints);
    }

    public long readUnsignedInt() throws IOException {
        byte[] ints = read(4);
        return TransformUtils.bytes2UnsignedInt(ints);
    }

    public long readLong() throws IOException {
        return TransformUtils.bytes2Long(read(8));
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public String readHexString(int count) throws IOException {
        return TransformUtils.byte2HexStr(readBig(count));
    }

    public int avaliable() throws IOException {
        return in.available();
    }

    public void skip(long count) throws IOException {
        if (count > 0)
            in.skip(count);
    }

    public void reset() throws IOException {
        in.reset();
    }

    public static void log(String format, Object... params) {
        try {
            if (showLog)
                System.out.printf(format, params);
            System.out.println();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
