package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/20 13:47
 */
public class ResChunkHeader {

    public int resType;
    public int headerSize;
    public int size;

    public  ResChunkHeader(BytesReader reader) throws IOException {
        resType=reader.readShort();
        headerSize=reader.readUnsignedShort();
        size=reader.readInt();
    }

    @Override
    public String toString() {
        return "ResChunkHeader{" +
                "resType=" + resType +
                ", headerSize=" + headerSize +
                ", size=" + size +
                '}';
    }
}
