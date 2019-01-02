package luyao.parser.arsc.bean;


import luyao.parser.utils.BytesReader;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by luyao
 * on 2018/12/21 14:22
 */
public class ResTableTypeSpec {

    public ResChunkHeader resChunkHeader;
    public int id;
    public int res0;
    public int res1;
    public int entryCount;
    public int[] configMask;

    public ResTableTypeSpec(ResChunkHeader resChunkHeader) {
        this.resChunkHeader = resChunkHeader;
    }

    public void parse(BytesReader reader) throws IOException {
        this.id = reader.readByte();
        this.res0 = reader.readByte();
        this.res1 = reader.readUnsignedShort();
        this.entryCount = reader.readInt();
        this.configMask=new int[entryCount];
        for (int i = 0; i < entryCount; i++) {
            configMask[i] = reader.readInt();
        }
    }

    @Override
    public String toString() {
        return "ResTableTypeSpec{" +
                "resChunkHeader=" + resChunkHeader +
                ", id=" + id +
                ", res0=" + res0 +
                ", res1=" + res1 +
                ", entryCount=" + entryCount +
                ", configMask=" + Arrays.toString(configMask) +
                '}';
    }
}
