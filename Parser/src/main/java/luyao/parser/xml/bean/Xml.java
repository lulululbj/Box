package luyao.parser.xml.bean;

import luyao.parser.xml.bean.chunk.Chunk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyao
 * on 2018/5/2 13:48
 */
public class Xml {


    public static final int START_NAMESPACE_CHUNK_TYPE = 0x00100100;
    public static final int END_NAMESPACE_CHUNK_TYPE = 0x00100101;
    public static final int START_TAG_CHUNK_TYPE = 0x00100102;
    public static final int END_TAG_CHUNK_TYPE = 0x00100103;
    public static final int TEXT_CHUNK_TYPE = 0x00100104;

    public List<String> stringChunkList;
    public List<String> tagNameList;
    public List<Chunk> chunkList;
    public static Map<String, String> nameSpaceMap = new HashMap<>();
    public static StringBuilder BLANK = new StringBuilder("    ");
    public static String blank = "    ";

    public Xml(List<String> stringChunkList, List<String> tagNameList, List<Chunk> chunkList) {
        this.stringChunkList = stringChunkList;
        this.tagNameList = tagNameList;
        this.chunkList = chunkList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

        for (Chunk chunk : chunkList) {
            builder.append(chunk.toXmlString());
        }
        return builder.toString();
    }
}
