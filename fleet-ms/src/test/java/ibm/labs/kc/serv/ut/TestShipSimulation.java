package ibm.labs.kc.serv.ut;

import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.model.FleetControl;
import ibm.labs.kc.serv.FleetDAO;
import ibm.labs.kc.serv.FleetDAOMockup;
import ibm.labs.kc.serv.FleetService;
import ibm.labs.kc.serv.FleetSimulator;

public class TestShipSimulation {
	
	public static void pureSimulator() {
		FleetSimulator fleetSimulator = new FleetSimulator();
		FleetDAO dao = new FleetDAOMockup("Fleet.json");
		Fleet f = dao.getFleetByName("KC-Fleet North");
		fleetSimulator.start(f, .25);
		fleetSimulator.stop(f);
	}

	public static void main(String args[]) throws InterruptedException {
		// pureSimulator();
		
		// Via Service 
		FleetService serv = new FleetService();
		
		
		Fleet f = serv.getFleetByName("KC-Fleet North");
		FleetControl ctl = new FleetControl();
		ctl.setCommand("START");
		ctl.setFleetName("KC-Fleet North");
		ctl.setNumberOfMinutes(.5);
		serv.simulateFleet(ctl);
		ctl.setCommand("STOP");
		serv.simulateFleet(ctl);

	}
}
