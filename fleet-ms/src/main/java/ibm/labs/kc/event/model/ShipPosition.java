package ibm.labs.kc.event.model;

import com.google.gson.annotations.SerializedName;

public class ShipPosition {
	@SerializedName("shipId")
	protected String shipID;
	protected String latitude;
	protected String longitude;
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
	
}
