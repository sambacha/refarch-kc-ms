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
	 * Simulate the ship movement for n minutes. Each 5 s represents 2 hours of boat running
	 * @param ship
	 * @param simulation controller
	 */
	public void start(Ship s, ShipSimulationControl ctl) {
		if (ShipSimulationControl.CONTAINER_FIRE.equals(ctl.getCommand())) {
			BadEventSimulator.fireContainers(s, ctl.getNumberOfContainers());
		}
		if (ShipSimulationControl.HEAT_WAVE.equals(ctl.getCommand())) {
			BadEventSimulator.heatWave(s);
		}
		if (ShipSimulationControl.REEFER_DOWN.equals(ctl.getCommand())) {
			BadEventSimulator.reeferDown(s);
		}
		List<Position> shipPositions = readShipPositions(s.getName());
		shipRunner.init(s,shipPositions,ctl.getNumberOfMinutes());
		shipRunner.start();
	}

	
	public void stop(Ship s) {
		if (shipRunner != null) {
			shipRunner.stop();
		}
	}



}
