package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/24 15:37
 */
public class ResTableConfig {
    public int size;
    public char[] data;

    public void parse(BytesReader reader) throws IOException {
        this.size = reader.readInt();
        this.data = new char[size];
        reader.skip(size - 4);
//        for (int i = 4; i < size-4; i++) {
//            data[i] = (char) reader.readByte();
//        }
    }
}
