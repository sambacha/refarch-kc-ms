package ibm.labs.kc.simulator;

import java.util.HashMap;
import java.util.List;

import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

/**
 * Focus on one ship and move it and play with its containers
 * @author jerome boyer
 *
 */
public class ShipSimulator extends KCSimulator{
	HashMap<String,ShipRunner> shipThreads;
	private boolean usePublish = false;

	public ShipSimulator(boolean b) {
		this.usePublish = b;
	}
	
	
	/**
	 * Simulate the ship movement for n minutes. Each 5 s represents 2 hours of boat running
	 * @param ship
	 * @param numberOfMinutes
	 */
	public void start(Ship s, double numberOfMinutes) {
		List<Position> shipPositions = readShipPositions(s.getName());
		// start a thread per ship for the duration specified in number of minutes
		shipThreads = new HashMap<String,ShipRunner>(); 
		ShipRunner runner = new ShipRunner(s,shipPositions,numberOfMinutes,usePublish);
		shipThreads.put(s.getName(), runner);
		runner.start();
	}
	
	public void stop(Ship s) {
		if (shipThreads != null) {
				shipThreads.get(s.getName()).stop();
		}
	}
}
