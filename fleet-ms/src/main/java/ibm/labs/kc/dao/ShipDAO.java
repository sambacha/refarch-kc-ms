package ibm.labs.kc.dao;

import ibm.labs.kc.model.Ship;

public interface ShipDAO {

	Ship getShipByName(String shipName);

	void save(Ship s);

}
