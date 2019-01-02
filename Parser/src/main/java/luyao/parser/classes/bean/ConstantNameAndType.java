package luyao.parser.classes.bean;



import luyao.parser.utils.Reader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/8/27 16:11
 */
public class ConstantNameAndType extends Constant {

    public int name_index;
    public int descriptor_index;

    public ConstantNameAndType() {
        super(ConstantTag.NAME_AND_TYPE);
    }

    @Override
    public void read(Reader reader) {
        try {
            this.name_index = reader.readUnsignedShort();
            this.descriptor_index = reader.readUnsignedShort();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConstantNameAndType{" +
                "tag=" + tag +
                ", name_index=" + name_index +
                ", descriptor_index=" + descriptor_index +
                '}';
    }
}
