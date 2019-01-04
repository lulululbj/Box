package luyao.parser.xml;

import android.util.TypedValue;
import luyao.parser.utils.BytesReader;
import luyao.parser.utils.Utils;
import luyao.parser.xml.bean.Attribute;
import luyao.parser.xml.bean.Xml;
import luyao.parser.xml.bean.chunk.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static luyao.parser.utils.Reader.log;

/**
 * Created by luyao
 * on 2018/12/14 10:00
 */
public class XmlParser {

    private BytesReader reader;

    private List<String> stringChunkList = new ArrayList<>();
    private List<Chunk> chunkList = new ArrayList<>();

    public XmlParser(InputStream in) {
        this.reader = new BytesReader(Utils.readAll(in), true);
    }

    public Xml parse() {
        parseHeader();
        parseStringChunk();
        parseResourceIdChunk();
        parseXmlContentChunk();
        return generateXml();
    }

    private void parseHeader() {
        try {
            Xml.nameSpaceMap.clear();
            String magicNumber = reader.readHexString(4);
            log("magic number: %s", magicNumber);

            int fileSize = reader.readInt();
            log("file size: %d", fileSize);
        } catch (IOException e) {
            e.printStackTrace();
            log("parse header error!");
        }
    }

    private void parseStringChunk() {
        try {
            String chunkType = reader.readHexString(4);
            log("chunk type: %s", chunkType);

            int chunkSize = reader.readInt();
            log("chunk size: %d", chunkSize);

            int stringCount = reader.readInt();
            log("string count: %d", stringCount);

            int styleCount = reader.readInt();
            log("style count: %d", styleCount);

            reader.skip(4);  // unknown

            int stringPoolOffset = reader.readInt();
            log("string pool offset: %d", stringPoolOffset);

            int stylePoolOffset = reader.readInt();
            log("style pool offset: %d", stylePoolOffset);

            // 每个 string 的偏移量
            List<Integer> stringPoolOffsets = new ArrayList<>(stringCount);
            for (int i = 0; i < stringCount; i++) {
                stringPoolOffsets.add(reader.readInt());
            }

            // 每个 style 的偏移量
            List<Integer> stylePoolOffsets = new ArrayList<>(styleCount);
            for (int i = 0; i < styleCount; i++) {
                stylePoolOffsets.add(reader.readInt());
            }

            log("string pool:");
            for (int i = 1; i <= stringCount; i++) { // 没有读最后一个字符串
                String string;
                if (i == stringCount) {
                    int lastStringLength = reader.readShort() * 2;
                    string = new String(moveBlank(reader.readOrigin(lastStringLength)));
                    reader.skip(2);
                } else {
                    reader.skip(2);
                    byte[] content = reader.readOrigin(stringPoolOffsets.get(i) - stringPoolOffsets.get(i - 1) - 4);
                    reader.skip(2);
                    string = new String(moveBlank(content));

                }
                log("   %s", string);
                stringChunkList.add(string);
            }


            log("style pool:");
            for (int i = 1; i < styleCount; i++) {
                reader.skip(2);
                byte[] content = reader.readOrigin(stylePoolOffsets.get(i) - stylePoolOffsets.get(i - 1) - 4);
                reader.skip(2);
                String string = new String(content);
                log("   %s", string);
            }

            reader.moveTo(chunkSize + 8);

        } catch (IOException e) {
            e.printStackTrace();
            log("parse StringChunk error!");
        }
    }

    private void parseResourceIdChunk() {
        try {
            String chunkType = reader.readHexString(4);
//            if (!chunkType.equals("0x00080180"))
//                chunkType = reader.readHexString(4); // 有时候 chunkType 前面会多一个 0000
            log("chunk type: %s", chunkType);

            int chunkSize = reader.readInt();
            log("chunk size: %d", chunkSize);

            int resourcesIdChunkCount = (chunkSize - 8) / 4;
            for (int i = 0; i < resourcesIdChunkCount; i++) {
                String resourcesId = reader.readHexString(4);
                log("resource id: %s", resourcesId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseXmlContentChunk() {

        try {
            while (reader.avaliable() > 0) {
                int chunkType = reader.readInt();
                switch (chunkType) {
                    case Xml.START_NAMESPACE_CHUNK_TYPE:
                        parseStartNamespaceChunk();
                        break;
                    case Xml.START_TAG_CHUNK_TYPE:
                        parseStartTagChunk();
                        break;
                    case Xml.END_TAG_CHUNK_TYPE:
                        parseEndTagChunk();
                        break;
                    case Xml.END_NAMESPACE_CHUNK_TYPE:
                        parseEndNamespaceChunk();
                        break;
                    case Xml.TEXT_CHUNK_TYPE:
                        parseTextChunk();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("parse XmlContentChunk error!");
        }

    }

    private void parseStartNamespaceChunk() {
        log("\nparse Start NameSpace Chunk");
        log("chunk type: 0x%x", Xml.START_NAMESPACE_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            log("chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            log("line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int prefix = reader.readInt();
            log("prefix: %s", stringChunkList.get(prefix));

            int uri = reader.readInt();
            log("uri: %s", stringChunkList.get(uri));

            StartNameSpaceChunk startNameSpaceChunk = new StartNameSpaceChunk(chunkSize, lineNumber, prefix, uri);
            chunkList.add(startNameSpaceChunk);

            Xml.nameSpaceMap.put(stringChunkList.get(prefix), stringChunkList.get(uri));
        } catch (IOException e) {
            e.printStackTrace();
            log("parse Start NameSpace Chunk error!");
        }
    }

    private void parseStartTagChunk() {
        log("\nparse Start Tag Chunk");
        log("chunk type: 0x%x", Xml.START_TAG_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            log("chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            log("line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int namespaceUri = reader.readInt();
            if (namespaceUri == -1)
                log("namespace uri: null");
            else
                log("namespace uri: %s", stringChunkList.get(namespaceUri));

            int name = reader.readInt();
            log("name: %s", stringChunkList.get(name));

            reader.skip(4); // flag 0x00140014

            int attributeCount = reader.readInt();
            log("attributeCount: %d", attributeCount);

            int classAttribute = reader.readInt();
            log("class attribute: %s", classAttribute);

            List<Attribute> attributes = new ArrayList<>();

            for (int i = 0; i < attributeCount; i++) {

                log("Attribute[%d]", i);

                int namespaceUriAttr = reader.readInt();
                if (namespaceUriAttr == -1)
                    log("   namespace uri: null");
                else
                    log("   namespace uri: %s", stringChunkList.get(namespaceUriAttr));

                int nameAttr = reader.readInt();
                if (nameAttr == -1)
                    log("   name: null");
                else
                    log("   name: %s", stringChunkList.get(nameAttr));

                int valueStr = reader.readInt();
                if (valueStr == -1)
                    log("   valueStr: null");
                else
                    log("   valueStr: %s", stringChunkList.get(valueStr));

                int type = reader.readInt() >> 24;
                log("   type: %d", type);

                int data = reader.readInt();
                String dataString = type == TypedValue.TYPE_STRING ? stringChunkList.get(data) : TypedValue.coerceToString(type, data);
                log("   data: %s", dataString);

                Attribute attribute = new Attribute(namespaceUriAttr == -1 ? null : stringChunkList.get(namespaceUriAttr),
                        stringChunkList.get(nameAttr), valueStr, type, dataString);
                attributes.add(attribute);
            }
            StartTagChunk startTagChunk = new StartTagChunk(namespaceUri, stringChunkList.get(name), attributes);
            chunkList.add(startTagChunk);
        } catch (IOException e) {
            e.printStackTrace();
            log("parse Start NameSpace Chunk error!");
        }
    }

    private void parseEndTagChunk() {
        log("\nparse End Tag Chunk");
        log("chunk type: 0x%x", Xml.END_TAG_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            log("chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            log("line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int namespaceUri = reader.readInt();
            if (namespaceUri == -1)
                log("namespace uri: null");
            else
                log("namespace uri: %s", stringChunkList.get(namespaceUri));

            int name = reader.readInt();
            log("name: %s", stringChunkList.get(name));

            EndTagChunk endTagChunk = new EndTagChunk(namespaceUri, stringChunkList.get(name));
            chunkList.add(endTagChunk);
        } catch (IOException e) {
            e.printStackTrace();
            log("parse End Tag Chunk error!");
        }
    }

    private void parseEndNamespaceChunk() {
        log("\nparse End NameSpace Chunk");
        log("chunk type: 0x%x", Xml.END_NAMESPACE_CHUNK_TYPE);

        try {
            int chunkSize = reader.readInt();
            log("chunk size: %d", chunkSize);

            int lineNumber = reader.readInt();
            log("line number: %d", lineNumber);

            reader.skip(4); // 0xffffffff

            int prefix = reader.readInt();
            log("prefix: %s", stringChunkList.get(prefix));

            int uri = reader.readInt();
            log("uri: %s", stringChunkList.get(uri));

            EndNameSpaceChunk endNameSpaceChunk = new EndNameSpaceChunk(chunkSize, lineNumber, prefix, uri);
            chunkList.add(endNameSpaceChunk);

            Xml.nameSpaceMap.put(stringChunkList.get(prefix), stringChunkList.get(uri));
        } catch (IOException e) {
            e.printStackTrace();
            log("parse End NameSpace Chunk error!");
        }
    }

    private void parseTextChunk() {
        log("\nparse Text Chunk");
    }

    private Xml generateXml() {
        Xml xml = new Xml(stringChunkList, null, chunkList);
        log(xml.toString());
        return xml;
    }

    public static String format(String format, Object... params) {
        return String.format(format, params);
    }

    public static String getNamespacePrefix(String prefix) {
        if (prefix == null || prefix.length() == 0) {
            return "";
        }
        return prefix + ":";
    }

    public static byte[] moveBlank(byte[] data) {
        List<Byte> byteList = new ArrayList<>();
        for (Byte b : data) {
            if (b != 0) byteList.add(b);
        }
        byte[] result = new byte[byteList.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = byteList.get(i);
        return result;
    }

}
