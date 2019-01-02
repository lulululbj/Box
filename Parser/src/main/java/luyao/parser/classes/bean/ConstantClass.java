package luyao.parser.classes.bean;



import luyao.parser.utils.Reader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/8/27 15:49
 */
public class ConstantClass extends Constant {
    public int name_index;

    public ConstantClass() {
        super(ConstantTag.CLASS);
    }

    public ConstantClass(int name_index) {
        super(ConstantTag.CLASS);
        this.name_index = name_index;
    }

    @Override
    public void read(Reader reader) {
        try {
            this.name_index=reader.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConstantClass{" +
                "tag=" + tag +
                ", name_index=" + name_index +
                '}';
    }
}
