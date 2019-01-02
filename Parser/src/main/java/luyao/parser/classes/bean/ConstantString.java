package luyao.parser.classes.bean;



import luyao.parser.utils.Reader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/8/27 15:43
 */
public class ConstantString extends Constant {

    public int string_index;

    public ConstantString() {
        super(ConstantTag.METHOD_REF);
    }

    public ConstantString(int string_index) {
        super(ConstantTag.METHOD_REF);
        this.string_index = string_index;
    }

    @Override
    public void read(Reader reader) {
        try {
            this.string_index=reader.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConstantString{" +
                "tag=" + tag +
                ", string_index=" + string_index +
                '}';
    }
}
