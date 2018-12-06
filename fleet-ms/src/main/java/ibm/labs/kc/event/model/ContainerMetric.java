package ibm.labs.kc.event.model;

import com.google.gson.annotations.SerializedName;

public class ContainerMetric {
	// used when the event does not match the Java Bean used.
	@SerializedName("containerId")
	protected String id;
	@SerializedName("tempC")
	protected long temperature;
	protected long amp;
	@SerializedName("ts")
	protected String timeStamp;
	protected String shipId;
	
	public ContainerMetric(String shipId,String id, long t, long a, String ts) {
		this.id = id;
		this.shipId = shipId;
		this.temperature = t;
		this.amp = a;
		this.timeStamp = ts;
	}

	public String toString() {
		return getId() + " " + getTemperature() + " " + getAmp() + " " + getTimeStamp();	
	}
	
	public String getId() {
		return id;
	}

	public long getTemperature() {
		return temperature;
	}

	public long getAmp() {
		return amp;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getShipId() {
		return shipId;
	}

	public void setShipId(String shipId) {
		this.shipId = shipId;
	}

}
