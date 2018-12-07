package ibm.labs.kc.app.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import ibm.labs.kc.dao.DAOFactory;
import ibm.labs.kc.dao.ShipDAO;
import ibm.labs.kc.dto.model.ShipSimulationControl;
import ibm.labs.kc.model.Ship;
import ibm.labs.kc.simulator.BadEventSimulator;
import ibm.labs.kc.simulator.ShipSimulator;

/**
 * REST resource for ships
 * @author jeromeboyer
 *
 */

@Path("ships")
public class ShipService {
	protected ShipDAO dao;
	protected ShipSimulator simulator;
	
	public ShipService() {
	  dao = DAOFactory.buildOrGetShipDAOInstance();	
	  simulator = new ShipSimulator();
	}
	
	public ShipService(ShipDAO d,ShipSimulator s) {
		simulator = s;
		dao = d;	
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
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	 @Operation(summary = "Start a simulation on the given ship name",description="Start a ship simulation by moving the ship and send container metrics events according to the scenarion selected")
    @APIResponses(
            value = {
            	@APIResponse(
                    responseCode = "404", 
                    description = "ship not found",
                    content = @Content(mediaType = "text/plain")),
                @APIResponse(
                    responseCode = "200",
                    description = "simulation started",
                    content = @Content(mediaType = "application/json"))
            	})
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
		
		simulator.start(s,ctl.getNumberOfMinutes());
		return Response.ok().entity(s).build();
	}
	
}
