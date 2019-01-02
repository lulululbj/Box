package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;
import luyao.parser.utils.Reader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/24 16:53
 */
public class ResValue {

    public int size;
    public int res0;
    public int dataType;
    public int data;

    public void parse(BytesReader reader) throws IOException {
        this.size = reader.readUnsignedShort();
        this.res0=reader.readUnsignedByte();
        this.dataType = reader.readByte();
        this.data = reader.readInt();
    }
}