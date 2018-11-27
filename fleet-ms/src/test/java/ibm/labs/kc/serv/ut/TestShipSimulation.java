package ibm.labs.kc.serv.ut;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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
    
	 public static TestShipSimulation serv;
	
	 public TestShipSimulation() {
		 super("Fleet.json");
	 }
	 
	public Response performSimulation(ShipSimulationControl ctl, boolean publish) {
		this.usePublish = publish;
		return performSimulation(ctl);
	}
	
	@BeforeClass
	public static void init() {
		 serv =  new TestShipSimulation();
	}
	
	@Test
	public void validateContainerFire() {
		System.out.println("Validate  containers fire");
		ShipSimulationControl ctl = new ShipSimulationControl("JimminyCricket", ShipSimulationControl.CONTAINER_FIRE);
		ctl.setNumberOfContainers(4);
		ctl.setNumberOfMinutes(1);
		Response res = serv.performSimulation(ctl,false);
		Ship s = (Ship)res.getEntity();
		for (List<Container> row : s.getContainers()) {
			for (Container c : row) {
				System.out.print(c.toString() + " --- ");
			}
			System.out.println("\n---------------------");
		}
		Assert.assertTrue(s.getContainers().get(0).get(2).getStatus().equals(Container.STATUS_FIRE));
	}

	@Test
	public void validateContainerDown() {
		System.out.println("Validate  containers down");
		ShipSimulationControl ctl = new ShipSimulationControl("JimminyCricket", ShipSimulationControl.REEFER_DOWN);
		ctl.setNumberOfMinutes(1);
		Response res = serv.performSimulation(ctl,false);
		Ship s = (Ship)res.getEntity();
		for (List<Container> row : s.getContainers()) {
			for (Container c : row) {
				System.out.print(c.toString() + " --- ");
			}
			System.out.println("\n---------------------");
		}
		Assert.assertTrue(s.getContainers().get(0).get(3).getStatus().equals(Container.STATUS_DOWN));
	}
	
	@Test
	public void validateHeatWave() {
		System.out.println("Validate  heat wave on top containers");
		ShipSimulationControl ctl = new ShipSimulationControl("JimminyCricket", ShipSimulationControl.HEAT_WAVE);
		ctl.setNumberOfMinutes(1);
		Response res = serv.performSimulation(ctl,false);
		Ship s = (Ship)res.getEntity();
		for (List<Container> row : s.getContainers()) {
			for (Container c : row) {
				System.out.print(c.toString() + " --- ");
			}
			System.out.println("\n---------------------");
		}
		Assert.assertTrue(s.getContainers().get(s.getMaxRow()).get(0).getStatus().equals(Container.STATUS_HEAT));
	}
}
