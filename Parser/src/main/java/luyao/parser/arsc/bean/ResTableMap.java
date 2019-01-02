package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/24 16:04
 */
public class ResTableMap {

    public int resTableRefName;
    public ResValue resValue;


    public void parse(BytesReader reader) throws IOException {
        this.resTableRefName = reader.readInt();

        resValue = new ResValue();
        resValue.parse(reader);
    }
}
