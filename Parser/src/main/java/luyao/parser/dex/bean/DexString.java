package luyao.parser.dex.bean;

/**
 * Created by luyao
 * on 2018/12/19 10:01
 */
public class DexString {

    public int string_data_off;
    public String string_data;

    public DexString(int string_data_off, String string_data) {
        this.string_data_off = string_data_off;
        this.string_data = string_data;
    }

    @Override
    public String toString() {
        return "DexString{" +
                "string_data_off=" + string_data_off +
                ", string_data=" + string_data +
                '}';
    }
}
