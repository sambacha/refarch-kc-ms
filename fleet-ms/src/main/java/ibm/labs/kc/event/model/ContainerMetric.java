package ibm.labs.kc.event.model;

public class ContainerMetric {
	protected String id;
	protected long temperature;
	protected long amp;
	protected String timeStamp;
	
	public ContainerMetric(String id, long t, long a, String ts) {
		this.id = id;
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

}
