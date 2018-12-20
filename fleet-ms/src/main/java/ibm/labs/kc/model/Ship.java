package ibm.labs.kc.model;

import java.util.ArrayList;

public class Ship {
	protected String name;
	protected String latitude;
	protected String longitude;
	protected String status;
	protected String port;
	protected String type;
	protected int maxRow;
	protected int maxColumn;
	protected int numberOfContainers;
	protected ArrayList<ArrayList<Container>> containers;
	
	public Ship(String name) {
		this.name = name;
		containers = new ArrayList<ArrayList<Container>>();
	}

	public String toString() {
		String s = getName() + " " + getStatus() + " " + getLatitude() + " " + getLongitude();
		if (numberOfContainers > 0) {
			
		}
		return s;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	public int getMaxColumn() {
		return maxColumn;
	}

	public void setMaxColumn(int maxColumn) {
		this.maxColumn = maxColumn;
	}

	public int getNumberOfContainers() {
		return numberOfContainers;
	}

	public void setNumberOfContainers(int numberOfContainer) {
		this.numberOfContainers = numberOfContainer;
	}

	public ArrayList<ArrayList<Container>> getContainers() {
		if (containers == null) {
			containers = new ArrayList<ArrayList<Container>>();
			for (int i = 0; i < this.maxRow; i++) {
				containers.add( new ArrayList<Container>());
			}
		}
		return containers;
	}

	public void setContainers(ArrayList<ArrayList<Container>> containers) {
		this.containers = containers;
	}

}
