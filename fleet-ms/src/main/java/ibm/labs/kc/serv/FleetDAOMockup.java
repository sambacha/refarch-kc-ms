package ibm.labs.kc.serv;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;

import ibm.labs.kc.model.Fleet;
import ibm.labs.kc.model.Ship;

public class FleetDAOMockup implements FleetDAO {
	
	private static HashMap<String,Fleet> fleet = new HashMap<String,Fleet>();

	public FleetDAOMockup() {
		Fleet f = createNorth();
		fleet.put(f.getName(),f);
		Fleet f2 = new Fleet("KC- Fleet South");
		fleet.put(f2.getName(),f2);
	}
	
	public FleetDAOMockup(String fleetFileName) {
		InputStream fin= getClass().getClassLoader().getResourceAsStream(fleetFileName);
		Reader json = new InputStreamReader(fin);
		Fleet [] fleets = new Gson().fromJson(json, Fleet[].class);
		for (Fleet f : fleets) {
			fleet.put(f.getName(),f);
		}
	}
	
	private Fleet createNorth() {
		Fleet f = new Fleet("KC-Fleet North");
		Ship s = new Ship("Marie Rose");
		s.setStatus("Docked");
		s.setPort("Oakland");
		s.setId("s1");
		s.setLatitude("37.8044");
		s.setLongitude("-122.2711");
		s.setType("Carrier");
		f.getShips().add(s);
		s = new Ship("BlackBear");
		s.setStatus("AtSea");
		s.setPort(null);
		s.setId("s2");
		s.setLatitude("36.8044");
		s.setLongitude("-140.2711");
		s.setType("Carrier");
		f.getShips().add(s);
		return f;
	}
	
	
	@Override
	public Collection<Fleet> getFleets() {
		return fleet.values();
	}

	@Override
	public Fleet getFleetByName(String fleetName) {	
		return fleet.get(fleetName);
	}

}
