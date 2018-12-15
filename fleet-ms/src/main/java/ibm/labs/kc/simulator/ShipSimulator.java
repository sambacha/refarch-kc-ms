package ibm.labs.kc.simulator;

import java.util.List;

import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

/**
 * Focus on one ship and move it and play with its containers
 * @author jerome boyer
 *
 */
public class ShipSimulator extends KCSimulator{

	ShipRunner shipRunner;
	
	public ShipSimulator() {
		shipRunner = new ShipRunner();
	}
	
	
	public ShipSimulator(ShipRunner r) {
		this.shipRunner = r;
	}


	/**
	 * Simulate the ship movement for n minutes. Each 5 s represents 2 hours of boat running
	 * @param ship
	 * @param numberOfMinutes
	 */
	public void start(Ship s, double numberOfMinutes) {
		
		List<Position> shipPositions = readShipPositions(s.getName());
		// start a thread per ship for the duration specified in number of minutes
		shipRunner.init(s,shipPositions,numberOfMinutes);
		shipRunner.start();
	}
	
	public void stop(Ship s) {
		if (shipRunner != null) {
			shipRunner.stop();
		}
	}
}
