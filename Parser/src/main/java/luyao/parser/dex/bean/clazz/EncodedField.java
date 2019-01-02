package luyao.parser.dex.bean.clazz;

/**
 * Created by luyao
 * on 2018/12/19 14:48
 */
public class EncodedField {

    public int field_idx;
    public int access_flags;

    public EncodedField(int field_idx, int access_flags) {
        this.field_idx = field_idx;
        this.access_flags = access_flags;
    }

    public int getField_idx() {
        return field_idx;
    }

    public void setField_idx(int field_idx) {
        this.field_idx = field_idx;
    }

    public int getAccess_flags() {
        return access_flags;
    }

    public void setAccess_flags(int access_flags) {
        this.access_flags = access_flags;
    }
}
