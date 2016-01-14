package helloWorldTp2;

import peersim.edsim.*;

public class Message {

    public final static int HELLOWORLD = 0;

    private int srcPid;
    private int type;
    private String content;

    Message(int type, String content)
    {
        this(0, type, content);
    }

    Message(int srcPid, int type, String content) {
	this.type = type;
	this.content = content;
        this.srcPid = srcPid;
    }

    public String getContent() {
	return this.content;
    }

    public int getType() {
	return this.type;
    }

    public int getSrcPid() {
        return this.srcPid;
    }
}
