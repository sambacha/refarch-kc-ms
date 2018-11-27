package ibm.labs.kc.serv.ut;

import ibm.labs.kc.dao.DAOFactory;
import ibm.labs.kc.dao.FleetDAO;
import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.simulator.FleetSimulator;

/**
 * Move only the ships of the north fleet, do not use the publishing of events to kafka
 * @author jerome boyer
 *
 */
public class TestFleetSimulation {

	public static void main(String args[]) throws InterruptedException {
		FleetSimulator fleetSimulator = new FleetSimulator(false);
		FleetDAO dao = DAOFactory.buildOrGetFleetDAO("Fleet.json");
		Fleet f = dao.getFleetByName("KC-FleetNorth");
		fleetSimulator.start(f, .25);
		fleetSimulator.stop(f);
	}
}
