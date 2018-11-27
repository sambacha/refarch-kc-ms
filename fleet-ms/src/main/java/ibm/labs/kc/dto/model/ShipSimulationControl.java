package ibm.labs.kc.dto.model;

/**
 * Exchange control information for ship simulation
 * @author jerome boyer
 *
 */
public class ShipSimulationControl {
	public static final String CONTAINER_FIRE = "CONTAINER_FIRE";
	protected String shipName;
	protected String command;
	private int numberOfContainers;
	public double numberOfMinutes;

	public  ShipSimulationControl(String name, String command) {
		this.shipName = name;
		this.command = command;
	}
	
	public String getShipName() {
		return shipName;
	}

	public String getCommand() {
		return command;
	}

	public int getNumberOfContainers() {
		return numberOfContainers;
	}

	public void setNumberOfContainers(int numberOfContainers) {
		this.numberOfContainers = numberOfContainers;
	}

	public double getNumberOfMinutes() {
		return numberOfMinutes;
	}

	public void setNumberOfMinutes(double numberOfMinutes) {
		this.numberOfMinutes = numberOfMinutes;
	}
	
}
