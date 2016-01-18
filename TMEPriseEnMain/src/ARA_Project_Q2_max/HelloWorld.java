package ARA_Project_Q2_max;

import java.util.ArrayList;

import peersim.edsim.*;
import peersim.core.*;
import peersim.config.*;

public class HelloWorld implements EDProtocol {

	// identifiant de la couche transport
	private int transportPid;

	// objet couche transport
	private MatrixTransport transport;

	// identifiant de la couche courante (la couche applicative)
	private int mypid;

	// le numero de noeud
	private int nodeId;

	// prefixe de la couche (nom de la variable de protocole du fichier de
	// config)
	private String prefix;

	private long state = 0;

	/* failure detector */
	private long deadline;
	private long suspect_duration; // recue en arg dans le fichier de conf
	private long[] suspicion_array;
	private long[] expectation_hb;
	private ArrayList<ArrayList<Long>> avg;

	// Node states
	private final int CORRECT = 0;
	private final int SUSPECTED = 1;
	private final int FAILED = 2;

	public HelloWorld(String prefix) {
		this.prefix = prefix;
		// initialisation des identifiants a partir du fichier de configuration
		this.transportPid = Configuration.getPid(prefix + ".transport");
		this.mypid = Configuration.getPid(prefix + ".myself");
		this.transport = null;
		// on récupère le nombre de tours
		// this.myNbMsg = Configuration.getInt(prefix + ".myNbMsg");
		this.suspect_duration = Configuration.getInt(prefix
				+ ".suspectDuration");

		suspicion_array = new long[Network.size()];
		int i;
		for (i = 0; i < Network.size(); i++) {
			suspicion_array[i] = CORRECT;
		}
		this.deadline = suspect_duration;

		expectation_hb = new long[Network.size()];
		avg = new ArrayList<ArrayList<Long>>();

		ArrayList<Long> tmp;
		for (i = 0; i < Network.size(); i++) {
			tmp = new ArrayList<Long>();
			avg.add(tmp);
			expectation_hb[i] = -1;
		}
	}

	// methode appelee lorsqu'un message est recu par le protocole HelloWorld du
	// noeud
	public void processEvent(Node node, int pid, Object event) {
		this.receive((Message) event);
	}

	// methode necessaire pour la creation du reseau (qui se fait par clonage
	// d'un prototype)
	public Object clone() {

		HelloWorld dolly = new HelloWorld(this.prefix);

		return dolly;
	}

	// liaison entre un objet de la couche applicative et un
	// objet de la couche transport situes sur le meme noeud
	public void setTransportLayer(int nodeId) {
		this.nodeId = nodeId;
		this.transport = (MatrixTransport) Network.get(this.nodeId)
				.getProtocol(this.transportPid);

		long delay = CommonState.r.nextLong(10) + 10;
		Message msg = new Message(Message.STATE, "Inc", this.nodeId);
		EDSimulator.add(delay, msg, this.getMyNode(), this.mypid);
	}

	// envoi d'un message (l'envoi se fait via la couche transport)
	public void send(Message msg, Node dest) {
		this.transport.send(getMyNode(), dest, msg, this.mypid);
	}

	private void broadcastSend(Message msg) {
		for (int i = 0; i < Network.size(); i++)
			if (suspicion_array[i] == CORRECT)
				this.send(msg, Network.get(i));
	}

	// affichage a la reception
	private void receive(Message msg) {

		switch (msg.getType()) {
		case Message.HELLOWORLD:
			System.out.println("[" + CommonState.getTime() + "]" + "[" + this
					+ "]" + " Received " + msg.getContent() + " from "
					+ msg.getIdSender());

			break;
		case Message.KILL:
			System.out.println("[" + CommonState.getTime() + "] " + "[" + this
					+ "]" + " received kill" + " from "
					+ msg.getIdSender());
			this.getMyNode().setFailState(Fallible.DEAD);
			break;
		case Message.STATE:
			this.state++;
			EDSimulator.add(CommonState.r.nextLong(10) + 10, new Message(
					Message.STATE, "Inc", this.nodeId), this.getMyNode(),
					this.mypid);
			if (CommonState.r.nextLong(10) < 5) {

				this.send(new Message(Message.HELLOWORLD, "Hello",
						this.nodeId), this.getRandomAliveNode());
			}
			if (CommonState.r.nextLong(1000) < 5) {
				this.broadcastSend(new Message(Message.HELLOWORLD,
						"Broadcast hello", this.nodeId));
			}
			/* first failure detection */
			if (this.deadline == this.state) {
				for (int i = 0; i < Network.size(); i++) {
					suspicion_array[i] = this.SUSPECTED;
					this.send(new Message(Message.HB, "Heartbeat", this.nodeId),
							Network.get(i));
				}
				this.deadline += suspect_duration;
			}
			break;
		case Message.HB:
			/* updating expectation deadline */
			long tmp = 0;
			if (expectation_hb[msg.getIdSender()] == -1) {
				avg.get(msg.getIdSender()).add(this.state);
				tmp = this.state;
			} else {
				avg.get(msg.getIdSender()).add(
						this.state
								- this.expectation_hb[msg.getIdSender()]
								- avg.get(msg.getIdSender()).get(
										avg.get(msg.getIdSender()).size() - 1));
				for (int i = 0; i < avg.get(msg.getIdSender()).size(); i++)
					tmp = (tmp < avg.get(msg.getIdSender()).get(i)) ? avg.get(
							msg.getIdSender()).get(i) : tmp;
			}
			this.expectation_hb[msg.getIdSender()] = this.state + tmp;
			
			if (suspicion_array[msg.getIdSender()] == this.SUSPECTED) {
				System.out.println("==>[" + CommonState.getTime() + "]"
						+ "[" + this + "] wrong detection node: "
						+ msg.getIdSender());
			}
			
			suspicion_array[msg.getIdSender()] = this.CORRECT;
		default:
			break;
		}
	}

	private Node getRandomAliveNode() {
		return Network.get(this.getRandomAliveNodeId());
	}

	private int getRandomAliveNodeId() {
		int ret = 0;
		do {
			ret = (ret + 1) % Network.size();
		} while (suspicion_array[ret] != CORRECT);
		return ret;
	}

	// retourne le noeud courant
	private Node getMyNode() {
		return Network.get(this.nodeId);
	}

	public String toString() {
		return "Node " + this.nodeId;
	}

}
