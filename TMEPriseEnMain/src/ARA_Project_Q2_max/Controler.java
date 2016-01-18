package ARA_Project_Q2_max;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;

/*
 * Notre controleur pour la gestion des fautes.
 * Question 3, TP-01.
 */
public class Controler implements peersim.core.Control {

    // Args passés par le fichier de config :
    private int helloWorldPid; // pid de la couche applicative
    private int nbStep;        // nombre de tours
    private int stepCounter;
    private double probFailure;
    private int nbKilled = 0;
    
    public Controler(String prefix) {
	// On récupère les args du fichier de config :
	this.helloWorldPid = Configuration.getPid(prefix +
						  ".helloWorldProtocolPid");
	this.nbStep = Configuration.getInt(prefix + ".step");
	this.probFailure = Configuration.getDouble(prefix + ".probFailure");
	
	// Compteur de tours
	this.stepCounter = 0;
    }

    public boolean execute() {
	System.out.println("++> Step " + (++this.stepCounter));

        // si tous les noeuds sont morts on arrete l'exec
        if(nbKilled >= Network.size()) {
            System.out.println("++> Controler: they are all dead !! abort.");
            return true;
        }
	// Proba uniforme, si un noeud doit tomber
	else if(CommonState.r.nextDouble() < this.probFailure){
	    // Tirage uniforme dans combien de temps il va tomber
	    int killTime = CommonState.r.nextInt(this.nbStep);
	    // Tirage uniforme du noeud à kill
	    // qui ne doit pas être mort
	    int killTarget;
	    do {
		killTarget = CommonState.r.nextInt((int)Network.size());
	    } while(Network.get(killTarget).getFailState()==Fallible.DEAD);
	    nbKilled++;

	    // On send le message
	    Message msg = new Message(Message.KILL, "BAM !!");
	    EDSimulator.add(killTime, msg, Network.get(killTarget),
			    helloWorldPid);

	    System.out.println("+++++> Controler: Node " + killTarget +
			       " at time " + killTime);
	} else {
	    System.out.println("+++++> Controler: No kill this time");
	}
	
	return false;
    }
}
