package luyao.parser.classes.bean;


import luyao.parser.utils.Reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Field {

    public int access_flag;
    public int name_index;
    public int descriptor_index;
    public int attributes_count;
    public List<Attribute> attributeList;

    public void read(Reader reader) {
        try {
            this.access_flag = reader.readUnsignedShort();
            this.name_index = reader.readUnsignedShort();
            this.descriptor_index = reader.readUnsignedShort();
            this.attributes_count = reader.readUnsignedShort();

            List<Attribute> attributeList = new ArrayList<>();
            for (int i = 0; i < attributes_count; i++) {
                Attribute attribute=new Attribute();
                attribute.read(reader);
                attributeList.add(attribute);
            }
            this.attributeList = attributeList;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Field{" +
                "access_flag=" + access_flag +
                ", name_index=" + name_index +
                ", descriptor_index=" + descriptor_index +
                ", attributes_count=" + attributes_count +
                '}';
    }
}
