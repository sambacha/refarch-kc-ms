package ibm.labs.kc.app.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ibm.labs.kc.dao.DAOFactory;
import ibm.labs.kc.dao.FleetDAO;
import ibm.labs.kc.dao.FleetDAOMockup;
import ibm.labs.kc.dto.model.FleetControl;
import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.simulator.FleetSimulator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("fleets")
public class FleetService {
	
	protected FleetDAO dao;
	protected static FleetSimulator fleetSimulator = new FleetSimulator(true);
	
	public FleetService() {
		dao = DAOFactory.buildOrGetFleetDAO("Fleet.json");	
	}
	
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public List<Fleet> getFleets() {
		// TODO decide to remove ship or not
		return new ArrayList<Fleet>(dao.getFleets());
	}

    @GET
	 @Path(value="/{fleetName}")
	 @ApiOperation(value = "Get fleet by fleet name")
	 @ApiResponses({ @ApiResponse(code = 200, message = "fleet retrieved", response = Fleet.class),
	 @ApiResponse(code = 404, message = "fleet not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Fleet getFleetByName(@PathParam("fleetName") String fleetName) {
		return dao.getFleetByName(fleetName);
	}

    @POST
    @Path(value="/simulate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response simulateFleet(FleetControl ctl) {
    	if ("START".equals(ctl.getCommand().toUpperCase())) {
    		Fleet f = dao.getFleetByName(ctl.getFleetName());
    		getFleetSimulator().start(f,ctl.getNumberOfMinutes());
    		return  Response.ok("{\"status\":\"Started\"}").build();
    	}
       	if ("STOP".equals(ctl.getCommand().toUpperCase())) {
    		Fleet f = dao.getFleetByName(ctl.getFleetName());
    		getFleetSimulator().stop(f);
    		return  Response.ok("{\"status\":\"Stopped\"}").build();
    	}
    	return  Response.ok("{\"status\":\"Nothing done\"}").build();
    }

	public static FleetSimulator getFleetSimulator() {
		return fleetSimulator;
	}
   
    
}
