package ibm.labs.kc.app.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ibm.labs.kc.dao.DAOFactory;
import ibm.labs.kc.dao.ShipDAO;
import ibm.labs.kc.dto.model.ShipSimulationControl;
import ibm.labs.kc.model.Ship;
import ibm.labs.kc.simulator.BadEventSimulator;
import ibm.labs.kc.simulator.ShipSimulator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("ships")
public class ShipService {
	protected ShipDAO dao;
	protected boolean usePublish = true;
	
	public ShipService() {
	  dao = DAOFactory.buildOrGetShipDAOInstance();	
	}
	
	public ShipService(String fileName) {
	   dao = DAOFactory.buildOrGetShipDAOInstance(fileName);	
	}
	
	/**
	 * Given the ship name perform moving the ship and event on container
	 * @param simulation controller
	 * @return the ship
	 */
	@POST
	@Path(value="/simulate")
	@ApiOperation(value = "Start a simulation on the given ship name")
	@ApiResponses({ @ApiResponse(code = 200, message = "simulation started", response = Ship.class),
	@ApiResponse(code = 404, message = "Ship not found") })
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response performSimulation(ShipSimulationControl ctl) {
		Ship s = dao.getShipByName(ctl.getShipName());
		if (s == null) {
			return Response.status(404).build();
		}
		s.loadContainers(s.getNumberOfContainers());
		if (ShipSimulationControl.CONTAINER_FIRE.equals(ctl.getCommand())) {
			BadEventSimulator.fireContainers(s, ctl.getNumberOfContainers());
		}
		if (ShipSimulationControl.HEAT_WAVE.equals(ctl.getCommand())) {
			BadEventSimulator.heatWave(s);
		}
		if (ShipSimulationControl.REEFER_DOWN.equals(ctl.getCommand())) {
			BadEventSimulator.reeferDown(s);
		}
		ShipSimulator simulator = new ShipSimulator(this.usePublish);
		simulator.start(s,ctl.getNumberOfMinutes());
		return Response.ok().entity(s).build();
	}
	
}
