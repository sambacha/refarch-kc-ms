package ibm.labs.kc.event.model;

import com.google.gson.annotations.SerializedName;

public class BluewaterProblem {
	protected String issue;
	protected long weatherC;
	protected String containerId;
	protected String status;
	@SerializedName("tempC")
	protected long temperature;
	protected long amp;
	protected String shipId;
	protected String latitude;
	protected String longitude;
	protected String tag;
	protected String severity;
	@SerializedName("ts")
	protected String timeStamp;
	
   public BluewaterProblem() {
   }

public String getIssue() {
	return issue;
}

public void setIssue(String issue) {
	this.issue = issue;
}

public long getWeatherC() {
	return weatherC;
}

public void setWeatherC(long weatherC) {
	this.weatherC = weatherC;
}

public String getContainerId() {
	return containerId;
}

public void setContainerId(String containerId) {
	this.containerId = containerId;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public long getTemperature() {
	return temperature;
}

public void setTemperature(long temperature) {
	this.temperature = temperature;
}

public long getAmp() {
	return amp;
}

public void setAmp(long amp) {
	this.amp = amp;
}

public String getShipId() {
	return shipId;
}

public void setShipId(String shipId) {
	this.shipId = shipId;
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

public String getTag() {
	return tag;
}

public void setTag(String tag) {
	this.tag = tag;
}

public String getSeverity() {
	return severity;
}

public void setSeverity(String severity) {
	this.severity = severity;
}

public String getTimeStamp() {
	return timeStamp;
}

public void setTimeStamp(String timeStamp) {
	this.timeStamp = timeStamp;
}
   
   
}
