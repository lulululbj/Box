package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/12/21 10:57
 */
public class ResTablePackageHeader {

    public ResChunkHeader chunkHeader;
    public int id;
    public String name;
    public int typeStrings;
    public int lastPublicType;
    public int keyStrings;
    public int lastPublicKey;


    public ResTablePackageHeader(BytesReader reader) throws IOException {
        chunkHeader = new ResChunkHeader(reader);
        id=reader.readInt();
        name = new String(reader.read(256));
        typeStrings = reader.readInt();
        lastPublicType = reader.readInt();
        keyStrings = reader.readInt();
        lastPublicKey = reader.readInt();
    }

    @Override
    public String toString() {
        return "ResTablePackageHeader{" +
                "\nchunkHeader=" + chunkHeader.toString() +
                ", \nid=" + id +
                ", \nname='" + name + '\'' +
                ", \ntypeStrings=" + typeStrings +
                ", \nlastPublicType=" + lastPublicType +
                ", \nkeyStrings=" + keyStrings +
                ", \nlastPublicKey=" + lastPublicKey +
                '}';
    }
}
