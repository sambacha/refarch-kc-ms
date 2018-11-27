package ibm.labs.kc.simulator;

import java.text.SimpleDateFormat;
import java.util.Date;

import ibm.labs.kc.event.model.ContainerMetric;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.Ship;

public class BadEventSimulator {	
	public static void fireContainers(Ship s, int numberOfContainers) {
	    if (numberOfContainers >= 4) {
	    	Container c = s.getContainers().get(0).get(2);
			c.setStatus(Container.STATUS_FIRE);
			c.setTemperature(150);
			c = s.getContainers().get(0).get(3);
			c.setStatus(Container.STATUS_FIRE);
			c.setTemperature(150);
			c = s.getContainers().get(0).get(4);
			c.setStatus(Container.STATUS_FIRE);
			c.setTemperature(150);
			c = s.getContainers().get(1).get(2);
			c.setStatus(Container.STATUS_FIRE);
			c.setTemperature(150);
	    } else {
	    	Container c = s.getContainers().get(0).get(2);
	    	c.setStatus(Container.STATUS_FIRE);
	    	c.setTemperature(150);
			c = s.getContainers().get(0).get(3);
			c.setStatus(Container.STATUS_FIRE);
			c.setTemperature(150);
	    }
	}
	    
	
	public static ContainerMetric buildContainerMetric(Container c, String currentWorldTime) {
		switch(c.getStatus()) {
			case Container.STATUS_FIRE:
				c.setTemperature(c.getTemperature() + 50);
				break;
			case Container.STATUS_HEAT:
				c.setTemperature(c.getTemperature() + 2);
				break;
			case Container.STATUS_DOWN:
				c.setAmp(0);
				break;
		}
	
		ContainerMetric cm = new ContainerMetric(c.getId(),c.getTemperature(),c.getAmp(),currentWorldTime);
		return cm;
	}
}
