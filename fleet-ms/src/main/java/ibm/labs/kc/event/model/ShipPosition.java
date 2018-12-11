package ibm.labs.kc.event.model;

import com.google.gson.annotations.SerializedName;

/**
 * This is an ship position event with a light adaptation of the following structure
 * https://www.navcen.uscg.gov/?pageName=AISMessage27
 * 
 * @author jerome boyer
 *
 */
public class ShipPosition {
	@SerializedName("shipId")
	protected String shipID;
	protected String latitude;
	protected String longitude;
	protected int speed;
	protected float ambiantTemperature;
	protected int compass;
	@SerializedName("ts")
	protected String timeStamp;
	
	public ShipPosition() {
		
	}
	
	public ShipPosition(String shipName, String latitude, String longitude) {
		this.shipID = shipName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String toString() {
		return getShipID() + " " + getLatitude() + " " + getLongitude() + " @ " + getTimeStamp();
	}

	public String getShipID() {
		return shipID;
	}

	public void setShipID(String shipID) {
		this.shipID = shipID;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public float getAmbiantTemperature() {
		return ambiantTemperature;
	}

	public void setAmbiantTemperature(float ambiantTemperature) {
		this.ambiantTemperature = ambiantTemperature;
	}

	public int getCompass() {
		return compass;
	}

	public void setCompass(int compass) {
		this.compass = compass;
	}
	
}
