package helloWorldRing;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;

public class HelloWorld implements EDProtocol {
    
    //identifiant de la couche transport
    private int transportPid;

    //objet couche transport
    private MatrixTransport transport;

    //identifiant de la couche courante (la couche applicative)
    private int mypid;

    //le numero de noeud
    private int nodeId;

    //prefixe de la couche (nom de la variable de protocole du fichier de config)
    private String prefix;

    // On veut faire tourner le jeton dans l'anneau plusieurs fois
    private int stepCpt = 0;
    private int myNbStep;

    public HelloWorld(String prefix) {
	this.prefix = prefix;
	//initialisation des identifiants a partir du fichier de configuration
	this.transportPid = Configuration.getPid(prefix + ".transport");
	this.mypid = Configuration.getPid(prefix + ".myself");
	this.transport = null;
	// on récupère le nombre de tours
	this.myNbStep = Configuration.getInt(prefix + ".myNbStep");
    }

    //methode appelee lorsqu'un message est recu par le protocole HelloWorld du noeud
    public void processEvent( Node node, int pid, Object event ) {
	this.receive((Message)event);
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
	this.transport = (MatrixTransport)Network.get(this.nodeId).getProtocol(this.transportPid);
    }

    //envoi d'un message (l'envoi se fait via la couche transport)
    public void send(Message msg, Node dest) {
	this.transport.send(getMyNode(), dest, msg, this.mypid);
    }

    //affichage a la reception
    private void receive(Message msg) {
	System.out.println(this + ": Received " + msg.getContent() + " at " + CommonState.getTime());

	switch(msg.getType()){
	case Message.HELLOWORLD:
	    if(this.nodeId == 0){
		this.stepCpt++;
	    }
	    if(this.stepCpt <= this.myNbStep){
		Node dest = Network.get((this.nodeId+1)%Network.size());
		this.send(new Message(Message.HELLOWORLD, "Hello !!"), dest);
	    }
	    break;
	case Message.KILL:
	    this.getMyNode().setFailState(Fallible.DEAD);
	    break;
	default: break;
	}
	
	/*
	 * Plus besoin de ça à la question 3, mais je garde le code :-)
	if(this.nodeId != 0){
	    Node dest = Network.get((this.nodeId+1)%Network.size());
	    this.send(new Message(Message.HELLOWORLD, "Hello !!"), dest);

	    /* génération d'une faute statique 
	    //if(this.nodeId == 7) {
	    //Network.get(8).setFailState(Fallible.DEAD);
	    //}
	} */
    }

    //retourne le noeud courant
    private Node getMyNode() {
	return Network.get(this.nodeId);
    }

    public String toString() {
	return "Node "+ this.nodeId;
    }
    
 }
