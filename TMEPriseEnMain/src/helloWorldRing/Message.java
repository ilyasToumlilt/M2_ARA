package helloWorldRing;

import peersim.edsim.*;

public class Message {

    public final static int HELLOWORLD = 0;
    public final static int KILL = 1;

    private int type;
    private String content;

    Message(int type, String content) {
	this.type = type;
	this.content = content;
    }

    public String getContent() {
	return this.content;
    }

    public int getType() {
	return this.type;
    }
    
}
