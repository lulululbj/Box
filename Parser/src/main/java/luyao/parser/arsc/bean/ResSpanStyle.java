package luyao.parser.arsc.bean;

/**
 * Created by luyao
 * on 2018/12/20 14:47
 */
public class ResSpanStyle {

    public int index;
    public int firstChar;
    public int lastChar;

    public ResSpanStyle(int index, int firstChar, int lastChar) {
        this.index = index;
        this.firstChar = firstChar;
        this.lastChar = lastChar;
    }


    @Override
    public String toString() {
        return "ResSpanStyle{" +
                "index=" + index +
                ", firstChar=" + firstChar +
                ", lastChar=" + lastChar +
                '}';
    }
}
