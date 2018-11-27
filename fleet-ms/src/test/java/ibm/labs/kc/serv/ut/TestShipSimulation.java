package ibm.labs.kc.serv.ut;

import java.util.List;

import javax.ws.rs.core.Response;

import ibm.labs.kc.app.rest.ShipService;
import ibm.labs.kc.dto.model.ShipSimulationControl;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.Ship;

/**
 * simulate the ship movement and the container
 * @author jeromeboyer
 *
 */
public class TestShipSimulation extends ShipService {
    
	
	 public TestShipSimulation(String fn) {
		 super(fn);
	 }
	 
	public Response performSimulation(ShipSimulationControl ctl, boolean publish) {
		this.usePublish = publish;
		return performSimulation(ctl);
	}
	
	
	public static void main(String[] args) {
		TestShipSimulation serv =  new TestShipSimulation("Fleet.json");
		ShipSimulationControl ctl = new ShipSimulationControl("JimminyCricket", ShipSimulationControl.CONTAINER_FIRE);
		ctl.setNumberOfContainers(4);
		ctl.setNumberOfMinutes(1);
		Response res = serv.performSimulation(ctl,false);
		Ship s = (Ship)res.getEntity();
		/*
		BadEventSimulator simul = new BadEventSimulator();
		FleetDAO dao = new FleetDAOMockup("Fleet.json");
		Fleet f = dao.getFleetByName("KC-FleetSouth");
		Ship s = f.getShips().get(0);
		s.loadContainers(s.getNumberOfContainers());
		simul.fireContainers(s, 4);
		*/
		for (List<Container> row : s.getContainers()) {
			for (Container c : row) {
				System.out.print(c.toString() + " --- ");
			}
			System.out.println("\n---------------------");
		}
	}

}
