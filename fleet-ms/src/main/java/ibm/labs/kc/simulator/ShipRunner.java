package ibm.labs.kc.simulator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ibm.labs.kc.app.kafka.ContainerMetricsProducer;
import ibm.labs.kc.app.kafka.ShipPositionProducer;
import ibm.labs.kc.event.model.ContainerMetric;
import ibm.labs.kc.event.model.ShipPosition;
import ibm.labs.kc.model.Container;
import ibm.labs.kc.model.Position;
import ibm.labs.kc.model.Ship;

/**
 * This is the Ship Simulator. For each ship it sends position and its containers states to
 * remote topic. 
 * It uses thread to execute the ship movement in parallel. THIS is not mandatory it could have been done
 * sequentially
 * @author jerome boyer
 *
 */
public class ShipRunner implements Runnable {
	public static final long WORLD_TIME_STEP = 45; // in minutes
	
	protected ShipPositionProducer positionPublisher;
	protected ContainerMetricsProducer containerPublisher;
	protected static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	protected Thread t;
	protected String shipName;
	protected Ship ship;
	protected List<Position> positions;
	protected double numberOfMinutes;
	
	public ShipRunner() {
		 this.positionPublisher = ShipPositionProducer.getInstance();
	     this.containerPublisher = ContainerMetricsProducer.getInstance();
	}
	
	public ShipRunner(ShipPositionProducer pb,ContainerMetricsProducer cb) {
		this.positionPublisher = pb;
		this.containerPublisher = cb;
	}
	
	public void init(Ship s, List<Position> list, double numberOfMinutes) {
		this.shipName = s.getName();
		this.positions = list;
		this.ship = s;
		this.numberOfMinutes = numberOfMinutes;
	}

	public void start() {
		System.out.println("Starting " +  shipName );
	    if (t == null) {
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
				positionPublisher.publishShipPosition(sp);
				
				// Then publish the state of their containers
				for (List<Container> row :  ship.getContainers()) {
					for (Container c : row) {
						ContainerMetric cm = BadEventSimulator.buildContainerMetric(this.shipName,c,dateFormat.format(currentWorldTime));
						containerPublisher.publishContainerMetric(cm);
					}
				}
				currentWorldTime=modifyTime(currentWorldTime);
	            // Thread.sleep(100);
				Thread.sleep(Math.round(this.numberOfMinutes*60000/this.positions.size()));
				
			}
        } catch (InterruptedException e) { 
            System.out.println ("ShipRunner stopped"); 
        } finally {
        	if (containerPublisher != null)
        		containerPublisher.close();
        	if (positionPublisher != null)
        		positionPublisher.close();
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

