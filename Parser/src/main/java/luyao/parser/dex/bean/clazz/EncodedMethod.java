package luyao.parser.dex.bean.clazz;

/**
 * Created by luyao
 * on 2018/12/19 15:00
 */
public class EncodedMethod {

    public int method_idx;
    public int access_flags;
    public int code_off;
    public DexCode dexCode;

    public EncodedMethod(int method_idx, int access_flags, int code_off) {
        this.method_idx = method_idx;
        this.access_flags = access_flags;
        this.code_off = code_off;
    }

    public DexCode getDexCode() {
        return dexCode;
    }

    public void setDexCode(DexCode dexCode) {
        this.dexCode = dexCode;
    }
}
