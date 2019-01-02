package luyao.parser.xml.bean.chunk;

import luyao.parser.xml.bean.Xml;

/**
 * Created by luyao
 * on 2018/12/14 16:23
 */
public class EndNameSpaceChunk extends Chunk {

    private int prefix;
    private int uri;

    public EndNameSpaceChunk(int chunkSize, int lineNumber, int prefix, int uri) {
        super(Xml.END_NAMESPACE_CHUNK_TYPE);
        this.chunkType = chunkType;
        this.chunkSize = chunkSize;
        this.lineNumber = lineNumber;
        this.prefix = prefix;
        this.uri = uri;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getUri() {
        return uri;
    }

    public void setUri(int uri) {
        this.uri = uri;
    }

    @Override
    public String toXmlString() {
        return "";
    }
}
