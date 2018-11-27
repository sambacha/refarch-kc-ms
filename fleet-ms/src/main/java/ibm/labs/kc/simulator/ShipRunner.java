package ibm.labs.kc.simulator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ibm.labs.kc.app.kafka.ContainerPublisher;
import ibm.labs.kc.app.kafka.PositionPublisher;
import ibm.labs.kc.event.model.ContainerMetric;
import ibm.labs.kc.event.model.ShipPosition;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

/**
 * This is the Ship Simulator. For each ship it send position and its containers state
 * @author jerome boyer
 *
 */
public class ShipRunner implements Runnable {
	public static final long WORLD_TIME_STEP = 45; // in minutes
	
	protected PositionPublisher positionPublisher;
	protected ContainerPublisher containerPublisher;
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected Thread t;
	protected String shipName;
	protected Ship ship;
	protected List<Position> positions;
	protected double numberOfMinutes;
	protected boolean usePublish = false;
	
	public ShipRunner(Ship s, List<Position> list, double numberOfMinutes, boolean usePublish) {
		this.shipName = s.getName();
		this.positions = list;
		this.ship = s;
		this.numberOfMinutes = numberOfMinutes;
		this.usePublish = usePublish;
	}

	public void start() {
		System.out.println("Starting " +  shipName );
	      if (t == null) {
	    	  if ( this.usePublish) {
	    		 positionPublisher = new PositionPublisher();
	 	    	 containerPublisher = new ContainerPublisher();
	    	  }
	    	
	         t = new Thread (this, shipName);
	         t.start ();
	         
	      }
	}
	
	@Override
	public void run() {		
		System.out.println("Running " +  shipName );
		Date currentWorldTime = new Date();
		try  { 
			for (Position p : this.positions) {
				// ships publish their position to a queue 
				ShipPosition sp = new ShipPosition(this.shipName,p.getLatitude(),p.getLongitude());
				if ( this.usePublish)
					positionPublisher.publishShipPosition(sp);
				else 
					System.out.println (this.shipName +  
			                  " position " + p.getLatitude() + " " + p.getLongitude());
				
				// Then publish the state of their containers
				for (List<Container> row :  ship.getContainers()) {
					for (Container c : row) {
						ContainerMetric cm = BadEventSimulator.buildContainerMetric(c,dateFormat.format(currentWorldTime));
						if ( this.usePublish)
							containerPublisher.publishContainerMetric(cm);
						else 
							System.out.println (cm.toString());
					}
				}
				currentWorldTime=modifyTime(currentWorldTime);
	            // Thread.sleep(100);
				Thread.sleep(Math.round(this.numberOfMinutes*60000/this.positions.size()));
			}
        } catch (InterruptedException e) { 
            System.out.println ("ShipRunner stopped"); 
        } 
	}
	
	
	private Date modifyTime(Date currentWorldTime) {
		Date d = new Date(currentWorldTime.getTime() + WORLD_TIME_STEP * 60000);
		return d;
	}

	public void stop() {
		System.out.println("Stopping " +  shipName );
	      if (t == null) {
	         t.interrupt();
	      }
	}
	
	
} // class ShipRunner

