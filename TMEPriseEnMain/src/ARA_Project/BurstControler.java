package ARA_Project;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Fallible;
import peersim.core.Network;
import peersim.edsim.EDSimulator;

public class BurstControler implements peersim.core.Control {

	// Args passés par le fichier de config :
	private int transportPid; // pid de la couche transport
	private MatrixTransport transport;
	private int stepCounter;
	private int coef;
	
	private long[][] defaultLatency;

	public BurstControler(String prefix) {
		// On récupère les args du fichier de config :
		//this.helloWorldPid = Configuration.getPid(prefix
		//		+ ".helloWorldProtocolPid");
		//this.nbStep = Configuration.getInt(prefix + ".step");
		this.coef = Configuration.getInt(prefix + ".coef");
		this.transportPid = Configuration.getPid(prefix + ".transport");

		// Compteur de tours
		this.stepCounter = this.coef;
		
		this.transport = null;
		
		// Initializing default latencies
		this.defaultLatency = new long[Network.size()][Network.size()];
		for(int i=0; i<Network.size(); i++){
			this.transport = (MatrixTransport)Network.get(i)
					.getProtocol(this.transportPid);
			for(int j = 0; j<Network.size(); j++){
				this.defaultLatency[i][j] = transport.getLatency(Network.get(i), 
						Network.get(j));
			}
		}
			
	}

	public boolean execute() {
		
		long tmp;
		
		if(this.stepCounter > 0){
			for(int i=0; i<Network.size(); i++){
				this.transport = (MatrixTransport)Network.get(i)
						.getProtocol(this.transportPid);
				for(int j=0; j<Network.size(); j++) {
					tmp = CommonState.r.nextLong(this.defaultLatency[i][j]*stepCounter-this.defaultLatency[i][j])
							+this.defaultLatency[i][j];
					this.transport.setLatency(Network.get(i), 
							Network.get(j), 
							tmp);
				}
			}
			
			this.stepCounter--;
		}
		
		return false;
	}
}
