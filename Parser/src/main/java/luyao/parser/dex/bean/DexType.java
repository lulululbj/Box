package luyao.parser.dex.bean;

/**
 * Created by luyao
 * on 2018/12/19 10:09
 */
public class DexType {

    public int descriptor_idx;
    public String string_data;

    public DexType(int descriptor_idx, String string_data) {
        this.descriptor_idx = descriptor_idx;
        this.string_data = string_data;
    }

    @Override
    public String toString() {
        return "DexType{" +
                "descriptor_idx=" + descriptor_idx +
                ", string_data='" + string_data + '\'' +
                '}';
    }
}
