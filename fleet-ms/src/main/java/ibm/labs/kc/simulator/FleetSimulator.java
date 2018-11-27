package ibm.labs.kc.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

public class FleetSimulator extends KCSimulator {
	HashMap<String,ShipRunner> shipThreads;
	private boolean usePublish = false;

	public FleetSimulator(boolean b) {
		this.usePublish = b;
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
			ShipRunner runner = new ShipRunner(s,shipsPositions.get(s.getName()),d,usePublish);
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
