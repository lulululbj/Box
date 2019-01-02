package luyao.parser.classes.bean;



import luyao.parser.utils.Reader;

import java.io.IOException;

/**
 * Created by luyao
 * on 2018/8/27 15:52
 */
public class ConstantUtf8 extends Constant {

    public int length;
    public String content;

    public ConstantUtf8() {
        super(ConstantTag.UTF8);
    }

    @Override
    public void read(Reader reader) {
        try {
            this.length = reader.readUnsignedShort();
            this.content = new String(reader.readOrigin(length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConstantUtf8{" +
                "tag=" + tag +
                ", length=" + length +
                ", content='" + content + '\'' +
                '}';
    }
}
