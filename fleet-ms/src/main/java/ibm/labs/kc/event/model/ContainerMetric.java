package ibm.labs.kc.event.model;

import com.google.gson.annotations.SerializedName;

public class ContainerMetric {
	// used when the event does not match the Java Bean used.
	@SerializedName("containerId")
	protected String id;
	@SerializedName("tempC")
	protected long temperature;
	protected long amp;
	protected float cumulativePowerConsumption;
	protected int contentType;
	protected float humidity;
	protected float co2;
 	protected long	Tproduce;
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

	public float getCumulativePowerConsumption() {
		return cumulativePowerConsumption;
	}

	public void setCumulativePowerConsumption(float cumulativePowerConsumption) {
		this.cumulativePowerConsumption = cumulativePowerConsumption;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public long getTproduce() {
		return Tproduce;
	}

	public void setTproduce(long tproduce) {
		Tproduce = tproduce;
	}

	public void setTemperature(long temperature) {
		this.temperature = temperature;
	}

	public void setAmp(long amp) {
		this.amp = amp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public float getCo2() {
		return co2;
	}

	public void setCo2(float co2) {
		this.co2 = co2;
	}

}
