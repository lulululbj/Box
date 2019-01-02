package luyao.parser.arsc.bean;

/**
 * Created by luyao
 * on 2018/12/24 15:40
 */
public class ResTableEntry {

    public int size;
    public int flags=0;
    public int string_pool_index;


    public ResTableEntry(int size, int flags, int string_pool_index) {
        this.size = size;
        this.flags = flags;
        this.string_pool_index = string_pool_index;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getString_pool_index() {
        return string_pool_index;
    }

    public void setString_pool_index(int string_pool_index) {
        this.string_pool_index = string_pool_index;
    }
}
