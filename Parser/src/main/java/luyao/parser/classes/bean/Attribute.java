package luyao.parser.classes.bean;


import luyao.parser.utils.Reader;

import java.io.IOException;

public class Attribute {

    public int attribute_name;
    public int attribute_length;

    public void read(Reader reader) {
        try {
            this.attribute_name = reader.readUnsignedShort();
            this.attribute_length = reader.readInt();
            reader.skip(attribute_length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "attribute_name=" + attribute_name +
                ", attribute_length=" + attribute_length +
                '}';
    }
}
