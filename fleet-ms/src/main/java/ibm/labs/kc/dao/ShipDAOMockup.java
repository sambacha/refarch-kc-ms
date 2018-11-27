package ibm.labs.kc.dao;

import java.util.HashMap;

import ibm.labs.kc.model.Ship;

public class ShipDAOMockup implements ShipDAO {

	private static HashMap<String,Ship> ships = new HashMap<String,Ship>();
	
	public ShipDAOMockup() {
	}
	
	@Override
	public Ship getShipByName(String shipName) {
		return ships.get(shipName);
	}

	@Override
	public void save(Ship s) {
		ships.put(s.getName(), s);
	}

}
