package luyao.parser.dex.bean;

/**
 * Created by luyao
 * on 2018/12/18 15:49
 */
public class DexFieldId {

    public int class_idx;
    public int type_idx;
    public int name_idx;

    public DexFieldId(int class_idx, int type_idx, int name_idx) {
        this.class_idx = class_idx;
        this.type_idx = type_idx;
        this.name_idx = name_idx;
    }

    @Override
    public String toString() {
        return "DexFieldId{" +
                "class_idx=" + class_idx +
                ", type_idx=" + type_idx +
                ", name_idx=" + name_idx +
                '}';
    }
}
