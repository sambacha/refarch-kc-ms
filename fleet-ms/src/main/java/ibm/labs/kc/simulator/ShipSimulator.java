package ibm.labs.kc.simulator;

import java.util.List;

import ibm.labs.kc.dto.model.ShipSimulationControl;
import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

/**
 * Focus on one ship and move it and play with its containers
 * @author jerome boyer
 *
 */
public class ShipSimulator extends KCSimulator {

	ShipRunner shipRunner;
	
	public ShipSimulator() {
		shipRunner = new ShipRunner();
	}
	
	
	public ShipSimulator(ShipRunner r) {
		this.shipRunner = r;
	}


	/**
	 * Simulate the ship movement for n minutes. Each 5 s represents 2 hours of boat running, an one step in the 
	 * csv file for the position
	 * @param ship
	 * @param simulation controller
	 */
	public void start(Ship s, ShipSimulationControl ctl) {
		List<Position> shipPositions = readShipPositions(s.getName());
		shipRunner.init(s,shipPositions,ctl);
		shipRunner.start();
	}

	
	public void stop(Ship s) {
		if (shipRunner != null) {
			shipRunner.stop();
		}
	}



}
