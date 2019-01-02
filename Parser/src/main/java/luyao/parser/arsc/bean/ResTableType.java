package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static luyao.parser.utils.Reader.log;

/**
 * Created by luyao
 * on 2018/12/24 15:16
 */
public class ResTableType {

    public ResChunkHeader chunkHeader;
    public int id;
    public int res0;
    public int res1;
    public int entryCount;
    public int entriesStart;
    public ResTableConfig config;
    public int[] entryOffsets;
    public List<ResTableEntry> resTableEntryList = new ArrayList<>();

    public void parse(BytesReader reader) {
        try {
            this.id = reader.readByte();
            this.res0 = reader.readByte();
            this.res1 = reader.readUnsignedShort();
            this.entryCount = reader.readInt();
            this.entriesStart = reader.readInt();
            this.config = new ResTableConfig();
            config.parse(reader);

            entryOffsets = new int[entryCount];
            for (int i = 0; i < entryCount; i++) {
                entryOffsets[i] = reader.readInt();
            }

            for (int i = 0; i < entryCount; i++) {
                int size = reader.readUnsignedShort();
                int flags = reader.readUnsignedShort();
                int resStringPoolIndex = reader.readInt();
                if (flags == 1) {
                    ResTableMapEntry resTableMapEntry = new ResTableMapEntry(size, flags, resStringPoolIndex);
                    resTableMapEntry.parse(reader);
                    resTableEntryList.add(resTableMapEntry);
                } else if (flags == 0) {
                    ResValue resValue = new ResValue();
                    resValue.parse(reader);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(List<String> keyStringList) {
        for (ResTableEntry resTableEntry : resTableEntryList) {
            log("keyString: %s", keyStringList.get(resTableEntry.string_pool_index));
            if (resTableEntry instanceof ResTableMapEntry) {

            } else {

            }
        }
        return null;
    }
}
