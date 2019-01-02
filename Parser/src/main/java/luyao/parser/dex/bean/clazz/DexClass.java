package luyao.parser.dex.bean.clazz;

/**
 * Created by luyao
 * on 2018/12/18 16:21
 */
public class DexClass {

    public int class_idx;
    public int access_flags;
    public int superclass_idx;
    public int interfaces_off;
    public int source_file_idx;
    public int annotations_off;
    public int class_data_off;
    public int staticValuesOff;

    public DexClass(int class_idx, int access_flags, int superclass_idx, int interfaces_off, int source_file_idx, int annotations_off, int class_data_off, int staticValuesOff) {
        this.class_idx = class_idx;
        this.access_flags = access_flags;
        this.superclass_idx = superclass_idx;
        this.interfaces_off = interfaces_off;
        this.source_file_idx = source_file_idx;
        this.annotations_off = annotations_off;
        this.class_data_off = class_data_off;
        this.staticValuesOff = staticValuesOff;
    }

    @Override
    public String toString() {
        return "DexClass{" +
                "class_idx=" + class_idx +
                ", access_flags=" + access_flags +
                ", superclass_idx=" + superclass_idx +
                ", interfaces_off=" + interfaces_off +
                ", source_file_idx=" + source_file_idx +
                ", annotations_off=" + annotations_off +
                ", class_data_off=" + class_data_off +
                ", staticValuesOff=" + staticValuesOff +
                '}';
    }
}
