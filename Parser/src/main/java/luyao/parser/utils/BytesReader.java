package luyao.parser.utils;

import java.io.IOException;

import static luyao.parser.utils.TransformUtils.reverseBytes;

/**
 * Created by luyao
 * on 2018/12/20 15:00
 */
public class BytesReader {

    private int position = 0;
    private byte[] data;
    private boolean isLittleEndian = true;

    public BytesReader(byte[] data) {
        this.data = data;
    }

    public BytesReader(byte[] data, boolean isLittleEndian) {
        this.data = data;
        this.isLittleEndian = isLittleEndian;
    }

    public static byte[] copy(byte[] stringContent, int start, int length) {
        byte[] b = new byte[length];
        System.arraycopy(stringContent, start, b, 0, length);
        return b;
    }


    public byte[] read(int count) {
        byte[] b = copy(data, position, count);
        position += count;
        if (isLittleEndian) return b;
        else {
            return reverseBytes(b);
        }
    }

    public byte[] readBig(int count) {
        byte[] b = copy(data, position, count);
        position += count;
        if (!isLittleEndian) return b;
        else {
            return reverseBytes(b);
        }
    }

    public byte[] read(byte[] buffer) throws IOException {
        buffer = copy(data, position, buffer.length);
        position += buffer.length;
        if (isLittleEndian) return buffer;
        else {
            return reverseBytes(buffer);
        }
    }

    public byte[] readOrigin(int count) throws IOException {
        byte[] b = copy(data, position, count);
        position += count;
        return b;
    }

    public byte readByte() throws IOException {
        byte b = copy(data, position, 1)[0];
        position++;
        return b;
    }

    public int readUnsignedByte() throws IOException {
        byte b = copy(data, position, 1)[0];
        position++;
        return b;
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
        return data.length - position;
    }

    public void skip(long count) throws IOException {
        if (count > 0)
            position += count;
    }

    public void reset() throws IOException {
        position = 0;
    }


    public int getCurrentPosition() {
        return position;
    }

    public byte[] read(int start, int len) {
        position += len;
        return copy(data, start, len);
    }

    public void moveTo(int i) {
        if (i <= data.length)
            position = i;
    }
}
