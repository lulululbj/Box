package luyao.parser.xml.bean.chunk;

import luyao.parser.xml.bean.Xml;

/**
 * Created by luyao
 * on 2018/12/14 16:17
 */
public abstract class Chunk {

    public int chunkType;
    public int chunkSize;
    public int lineNumber;

    public Chunk(int chunkType){
        this.chunkType=chunkType;
    }

    public abstract String toXmlString(Xml xml);
}
