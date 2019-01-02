package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/20 14:04
 */
public class ResTableHeader {

    public ResChunkHeader resChunkHeader;
    public int packageCount;

    public void parse(BytesReader reader){
        try {
            resChunkHeader=new ResChunkHeader(reader);
            packageCount=reader.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ResTableHeader{" +
                "resChunkHeader=" + resChunkHeader.toString() +
                ", packageCount=" + packageCount +
                '}';
    }
}
