package ibm.labs.kc.serv;

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

public class FleetSimulator {
	HashMap<String,ShipRunner> shipThreads;
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
			ShipRunner runner = new ShipRunner(s.getName(),shipsPositions.get(s.getName()),d);
			
			shipThreads.put(s.getId(), runner);
			runner.start();
		}
		
	}

	/**
	 * load for each ship of the fleet their positions for their journey from csv file
	 * @param f
	 * @return
	 */
	private HashMap<String, List<Position>> readShipsPositions(Fleet f) {
		HashMap<String,List<Position>> shipsPositions  = new HashMap<String,List<Position>>();
		for (Ship s : f.getShips()) {
			List<Position> LP = new ArrayList<Position>();
			shipsPositions.put(s.getName(),LP);
			InputStream fin = null;
			try {
				fin= getClass().getClassLoader().getResourceAsStream(s.getName()+".csv");
				BufferedReader br = new BufferedReader(new InputStreamReader(fin));
		        String line = "";
		        String cvsSplitBy = ",";	        
				while((line = br.readLine()) != null) {
					String[] positions = line.split(cvsSplitBy);
				    Position p = new Position(positions[0],positions[1]);
				    LP.add(p);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return shipsPositions;
	}

	public void stop(Fleet f) {
		if (shipThreads != null) {
			for (Ship s : f.getShips()) {
				shipThreads.get(s.getId()).stop();
			}
		}
	}

	private class ShipRunner implements Runnable {
		Thread t;
		String shipName;
		List<Position> positions;
		double numberOfMinutes;
		
		public ShipRunner(String name, List<Position> list, double numberOfMinutes) {
			this.shipName = name;
			this.positions = list;
			this.numberOfMinutes = numberOfMinutes;
		}

		public void start() {
			System.out.println("Starting " +  shipName );
		      if (t == null) {
		         t = new Thread (this, shipName);
		         t.start ();
		      }
		}
		
		@Override
		public void run() {		
			System.out.println("Running " +  shipName );
			try  { 
				for (Position p : this.positions) {
					// ships publish their position to a queue 
		            System.out.println (this.shipName +  
		                  " position " + p.getLatitude() + " " + p.getLongitude());
					Thread.sleep(Math.round(this.numberOfMinutes*60*1000/this.positions.size()));
		            // Thread.sleep(100);
				}
	        } catch (InterruptedException e) { 
	            System.out.println ("ShipRunner stopped"); 
	        } 
		}
		
		
		public void stop() {
			System.out.println("Stopping " +  shipName );
		      if (t == null) {
		         t.interrupt();
		      }
		}
	} // class ShipRunner
}
