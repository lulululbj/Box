package luyao.parser.xml.bean.chunk;

import luyao.parser.xml.XmlParser;
import luyao.parser.xml.bean.Attribute;
import luyao.parser.xml.bean.Xml;

import java.util.List;

import static luyao.parser.xml.bean.Xml.blank;

/**
 * Created by luyao
 * on 2018/12/14 16:24
 */
public class StartTagChunk extends Chunk {

    private int nameSpaceUri;
    private String name;
    private List<Attribute> attributeList;



    public StartTagChunk(int nameSpaceUri, String name, List<Attribute> attributeList) {
        super(Xml.START_TAG_CHUNK_TYPE);
        this.nameSpaceUri = nameSpaceUri;
        this.name = name;
        this.attributeList = attributeList;
    }

    public int getNameSpaceUri() {
        return nameSpaceUri;
    }

    public void setNameSpaceUri(int nameSpaceUri) {
        this.nameSpaceUri = nameSpaceUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    @Override
    public String toXmlString() {
        StringBuilder builder=new StringBuilder();
        System.out.println("test "+name+name.length());
        if (name.replace(" ","").equals("manifest")) {
            builder.append("<manifest");
            for (String key : Xml.nameSpaceMap.keySet()) {
                Xml.nameSpaceMap.put(Xml.nameSpaceMap.get(key), key);
                builder.append(XmlParser.format(" xmlns:%s=\"%s\"", key, Xml.nameSpaceMap.get(key)));
            }
        } else {
            builder.append(XmlParser.format("\n%s<%s", Xml.BLANK, name));
        }
        Xml.BLANK.append(blank);
        for (int i = 0; i < attributeList.size(); i++) {
            Attribute attribute = attributeList.get(i);

            String prefix = XmlParser.getNamespacePrefix(Xml.nameSpaceMap.get(attribute.getNamespaceUri()));
            builder.append(XmlParser.format("\n%s%s%s=\"%s\"", Xml.BLANK.toString(), prefix, attribute.getName(), attribute.getData()));

            if (i == attributeList.size() - 1) builder.append(">");
        }
        return builder.toString();
    }
}
