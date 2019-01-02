package luyao.parser.xml.bean;

/**
 * Created by luyao
 * on 2018/12/14 14:00
 */
public class Attribute {

    private String namespaceUri;
    private String name;
    private int valueStr;
    private int type;
    private String data;


    public Attribute(String namespaceUri, String name, int valueStr, int type, String data) {
        this.namespaceUri = namespaceUri;
        this.name = name;
        this.valueStr = valueStr;
        this.type = type;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "namespaceUri='" + namespaceUri + '\'' +
                ", name='" + name + '\'' +
                ", valueStr='" + valueStr + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public  String getData(){
        return data;
    }

    public String getNamespaceUri() {
        return namespaceUri;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValueStr() {
        return valueStr;
    }

    public void setValueStr(int valueStr) {
        this.valueStr = valueStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setData(String data) {
        this.data = data;
    }
}
