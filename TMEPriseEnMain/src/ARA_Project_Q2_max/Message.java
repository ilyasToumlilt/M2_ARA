package ARA_Project_Q2_max;

import peersim.edsim.*;

public class Message {

    public final static int HELLOWORLD = 0;
    public final static int KILL       = 1;
    public final static int STATE      = 2;
    public final static int HB         = 3;
    
    private int type;
    private String content;
    private int idSender;

    Message(int type, String content) {
        this(type, content, -1);
    }

    Message(int type, String content, int idSender){
        this.idSender = idSender;
        this.type = type;
        this.content = content;
    }

    public String getContent() {
	return this.content;
    }

    public int getType() {
	return this.type;
    }

    public int getIdSender() {
        return this.idSender;
    }
}
