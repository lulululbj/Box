package luyao.parser.arsc.bean;

import luyao.parser.utils.BytesReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luyao
 * on 2018/12/24 16:00
 */
public class ResTableMapEntry extends ResTableEntry {

    public ResTableRefParent resTableRefParent;
    public int count;
    public List<ResTableMap> resTableMapList;

    public ResTableMapEntry(int size, int flags, int string_pool_index) {
        super(size, flags, string_pool_index);
    }

    public void parse(BytesReader reader) throws IOException {
        this.resTableRefParent = new ResTableRefParent(reader.readInt());
        this.count = reader.readInt();
        resTableMapList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ResTableMap resTableMap = new ResTableMap();
            resTableMap.parse(reader);
            resTableMapList.add(resTableMap);
        }
    }
}
