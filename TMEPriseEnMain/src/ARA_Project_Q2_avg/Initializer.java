package ARA_Project_Q2_avg;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;

/*
  Module d'initialisation de helloWorld: 
  Fonctionnement:
    pour chaque noeud, le module fait le lien entre la couche transport et la couche applicative
    ensuite, il fait envoyer au noeud 0 un message "Hello" a tous les autres noeuds
 */
public class Initializer implements peersim.core.Control {
    
    private int helloWorldPid;

    public Initializer(String prefix) {
	//recuperation du pid de la couche applicative
	this.helloWorldPid = Configuration.getPid(prefix + ".helloWorldProtocolPid");
    }

    public boolean execute() {
	int nodeNb;
	HelloWorld emitter, current;
	Node dest;
	Message helloMsg;

	//recuperation de la taille du reseau
	nodeNb = Network.size();
        
	if (nodeNb < 1) {
	    System.err.println("Network size is not positive");
	    System.exit(1);
	}

	//recuperation de la couche applicative de l'emetteur (le noeud 0)
	emitter = (HelloWorld)Network.get(0).getProtocol(this.helloWorldPid);
	emitter.setTransportLayer(0);

	//pour chaque noeud, on fait le lien entre la couche applicative et la couche transport
	//puis on fait envoyer au noeud 0 un message "Hello"
	for (int i = 1; i < nodeNb; i++) {
	    dest = Network.get(i);
	    current = (HelloWorld)dest.getProtocol(this.helloWorldPid);
	    current.setTransportLayer(i);
	}

	/* on a besoin d'au moins deux nodes pour le ring */
	if(nodeNb < 2){
	    System.err.println("2 or more nodes are needed for this version");
	    System.exit(1);
	}

	/* send du message au premier node du ring */
	emitter.send(new Message(Message.HELLOWORLD, "Init Hello !!", 0),
                     Network.get(1));
	
	System.out.println("Initialization completed");
	return false;
    }
}
