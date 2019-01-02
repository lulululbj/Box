package luyao.parser.dex.bean.clazz;

import java.util.Arrays;

/**
 * Created by luyao
 * on 2018/12/19 15:02
 */
public class DexCode {

    public int registers_size;
    public int ins_size;
    public int outs_size;
    public int tries_size;
    public int debug_info_off;
    public int insns_size;
    public String[] insns;

    public DexCode(int registers_size, int ins_size, int outs_size, int tries_size, int debug_info_off, int insns_size, int[] insns) {
        this.registers_size = registers_size;
        this.ins_size = ins_size;
        this.outs_size = outs_size;
        this.tries_size = tries_size;
        this.debug_info_off = debug_info_off;
        this.insns_size = insns_size;
        this.insns = transform(insns);
    }

    private String[] transform(int[] insns) {
        String[] result = new String[insns.length];
        for (int i = 0; i < insns.length; i++) {
            result[i] = "0x"+Integer.toHexString(insns[i]);
        }
        return result;
    }


    @Override
    public String toString() {
        return "DexCode{" +
                "registers_size=" + registers_size +
                ", ins_size=" + ins_size +
                ", outs_size=" + outs_size +
                ", tries_size=" + tries_size +
                ", debug_info_off=" + debug_info_off +
                ", insns_size=" + insns_size +
                ", insns=" + Arrays.toString(insns) +
                '}';
    }
}
