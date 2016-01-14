package helloWorldTp2;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;

public class HelloWorld implements EDProtocol {
    
    //identifiant de la couche transport
    private int transportPid;

    //objet couche transport
    private HWTransport transport;

    //identifiant de la couche courante (la couche applicative)
    private int mypid;

    //le numero de noeud
    private int nodeId;

    //prefixe de la couche (nom de la variable de protocole du fichier de config)
    private String prefix;

    private long state;

    /* failure detection */
    private final int PING = 16;
    private final int PONG = 61;
    private final int CORRECT   = 0;
    private final int SUSPECTED = 1;
    private final int FAILED    = 2;
    private int[] jeanFailure;
    private int deadline = 0;
    private final int SUSPECT_DURATION = 10;

    /* failure messages */
    private final int KILL = 666;
    
    public HelloWorld(String prefix) {
	this.prefix = prefix;
	//initialisation des identifiants a partir du fichier de configuration
	this.transportPid = Configuration.getPid(prefix + ".transport");
	this.mypid = Configuration.getPid(prefix + ".myself");
	this.transport = null;
        this.state = 0;

        
        jeanFailure = new int[Network.size()];
        int i;
        for(i=0; i<Network.size(); i++)
            jeanFailure[i] = CORRECT;
            this.deadline = SUSPECT_DURATION;
    }

    private int getRandomCorrectPid()
    {
        int ret;
        while(true){
            ret = CommonState.r.nextInt(Network.size());
            if(jeanFailure[ret] == CORRECT)
                return ret;
        }
    }
    
    //methode appelee lorsqu'un message est recu par le protocole HelloWorld du noeud
    public void processEvent( Node node, int pid, Object event ) {
        int i;
        
        if( event instanceof Integer ){
            this.state++;
            EDSimulator.add(CommonState.r.nextLong(10)+10, new Integer(42), Network.get(this.nodeId), this.mypid);
            if(CommonState.r.nextLong(10) < 5){
                EDSimulator.add(CommonState.r.nextLong(10)+10,
                                new Message(mypid, Message.HELLOWORLD, "Ilyas !"),
                                Network.get(this.getRandomCorrectPid()),
                                this.mypid);
            }
            if(CommonState.r.nextLong(1000) < 5){
                for(i=0; i<Network.size(); i++)
                    if(jeanFailure[i]==CORRECT)
                        EDSimulator.add(CommonState.r.nextLong(10)+10,
                                        new Message(mypid, Message.HELLOWORLD, "Alexandra !"),
                                        Network.get(i),
                                        this.mypid);
            }
            
            for(i=0; i<Network.size(); i++)
                if(jeanFailure[i] != FAILED )
                    EDSimulator.add(1,
                                    new Message(mypid, this.PING, ""),
                                    Network.get(i),
                                    this.mypid);
            /* failure detection */
            if( this.deadline == this.state ){
                for(i=0; i<Network.size(); i++){
                    jeanFailure[i] = (jeanFailure[i]==SUSPECTED) ? FAILED : jeanFailure[i];
                }
                this.deadline += SUSPECT_DURATION;
                }
          }
        
        else if(event instanceof Message){
            
            if(((Message)event).getType() == this.PING){
                EDSimulator.add(1,
                                new Message(this.mypid, this.PONG, ""),
                                Network.get(((Message)event).getSrcPid()),
                                this.mypid);
            }
            else if(((Message)event).getType() == this.PONG){
                jeanFailure[((Message)event).getSrcPid()] = CORRECT;
            }
            else if(((Message)event).getType() == this.KILL){
                Network.get(this.nodeId).setFailState(Fallible.DEAD);
            }
            else
                this.receive((Message)event);
        }
    }
    //methode necessaire pour la creation du reseau (qui se fait par clonage d'un prototype)
    public Object clone() {

	HelloWorld dolly = new HelloWorld(this.prefix);

	return dolly;
    }

    //liaison entre un objet de la couche applicative et un 
    //objet de la couche transport situes sur le meme noeud
    public void setTransportLayer(int nodeId) {
	this.nodeId = nodeId;
	this.transport = (HWTransport) Network.get(this.nodeId).getProtocol(this.transportPid);
        long delay = CommonState.r.nextLong(10)+10;
        EDSimulator.add(delay, new Integer(42), Network.get(this.nodeId), this.mypid);

        if(this.nodeId == 7){
            EDSimulator.add(500, new Message(this.mypid, this.KILL, ""),
                            Network.get(7), this.mypid);
        }
    }

    //envoi d'un message (l'envoi se fait via la couche transport)
    public void send(Message msg, Node dest) {
	this.transport.send(getMyNode(), dest, msg, this.mypid);
    }

    //affichage a la reception
    private void receive(Message msg) {
	System.out.println(this + ": State " + this.state + " Received " + msg.getContent() + " at " + CommonState.getTime());
    }

    //retourne le noeud courant
    private Node getMyNode() {
	return Network.get(this.nodeId);
    }

    public String toString() {
	return "Node "+ this.nodeId;
    }

    
}
