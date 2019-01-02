package luyao.parser.classes.bean;



import luyao.parser.utils.Reader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/8/27 14:54
 */
public class ConstantMethodref extends Constant {

    public int class_index;
    public int name_and_type_index;

    public ConstantMethodref() {
        super(ConstantTag.METHOD_REF);
    }

    public ConstantMethodref(int class_index, int name_and_type_index) {
        super(ConstantTag.METHOD_REF);
        this.class_index = class_index;
        this.name_and_type_index = name_and_type_index;
    }

    @Override
    public void read(Reader reader) {
        try {
            this.class_index = reader.readUnsignedShort();
            this.name_and_type_index = reader.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConstantMethodref{" + "tag=" + tag +
                ", class_index=" + class_index +
                ", name_and_type_index=" + name_and_type_index +
                '}';
    }
}
