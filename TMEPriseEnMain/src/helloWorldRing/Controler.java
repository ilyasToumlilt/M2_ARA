package helloWorldRing;

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
	this.stepCounter++;
	System.out.println("++> Step " + this.stepCounter);
	return false;
    }
}
