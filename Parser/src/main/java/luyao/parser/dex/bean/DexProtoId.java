package luyao.parser.dex.bean;

/**
 * Created by luyao
 * on 2018/12/18 15:40
 */
public class DexProtoId {

    public int shorty_idx;
    public int return_type_idx;
    public int parameters_off;


    public DexProtoId(int shorty_idx, int return_type_idx, int parameters_off) {
        this.shorty_idx = shorty_idx;
        this.return_type_idx = return_type_idx;
        this.parameters_off = parameters_off;
    }

    @Override
    public String toString() {
        return "DexProtoId{" +
                "shorty_idx=" + shorty_idx +
                ", return_type_idx=" + return_type_idx +
                ", parameters_off=" + parameters_off +
                '}';
    }
}
