package ibm.labs.kc.simulator;

import java.util.HashMap;
import java.util.List;

import ibm.labs.kc.app.kafka.ContainerMetricsProducer;
import ibm.labs.kc.app.kafka.EventEmitter;
import ibm.labs.kc.app.kafka.ShipPositionProducer;
import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

/**
 * Simulate all the ships of a fleet. This means it keeps a map of ship, thread
 * 
 * @author jerome boyer
 *
 */
public class FleetSimulator extends KCSimulator {
	HashMap<String,ShipRunner> shipThreads;
	private EventEmitter positionPublisher;
	private EventEmitter containerPublisher;
	
	public FleetSimulator() {
		 this.positionPublisher = ShipPositionProducer.getInstance();
	     this.containerPublisher = ContainerMetricsProducer.getInstance();
	}
	
	public FleetSimulator(ShipPositionProducer pb,ContainerMetricsProducer cb) {
		this.positionPublisher = pb;
		this.containerPublisher = cb;
	}
	
	/**
	 * Simulate the ship movement for n minutes. Each 5 s represents 2 hours of boat running
	 * @param fleet
	 * @param d
	 */
	public void start(Fleet f, double d) {
		HashMap<String,List<Position>> shipsPositions = readShipsPositions(f);
		// start a thread per ship for the duration specified in number of minutes
		shipThreads = new HashMap<String,ShipRunner>(); 
		for (Ship s : f.getShips()) {
			ShipRunner runner = new ShipRunner(this.positionPublisher,this.containerPublisher);
			runner.init(s,shipsPositions.get(s.getName()),d);
			shipThreads.put(s.getName(), runner);
			runner.start();
		}
		
	}

	
	public void stop(Fleet f) {
		if (shipThreads != null) {
			for (Ship s : f.getShips()) {
				shipThreads.get(s.getName()).stop();
			}
		}
	}
}
