package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/20 14:13
 */
public class ResStringPoolHeader {

    public ResChunkHeader resChunkHeader;
    public int stringCount;
    public int styleCount;
    public int flags;
    public int stringsStart;
    public int stylesStart;

    public void parse(BytesReader reader) throws IOException {
        resChunkHeader = new ResChunkHeader(reader);
        stringCount = reader.readInt();
        styleCount = reader.readInt();
        flags = reader.readInt();
        stringsStart = reader.readInt();
        stylesStart = reader.readInt();
    }
}
