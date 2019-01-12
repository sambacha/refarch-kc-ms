package ibm.labs.kc.app.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import ibm.labs.kc.event.model.ShipPosition;

public class ShipPositionProducer extends BaseProducer{
	 private static  KafkaProducer<String, String> kafkaProducer;
	 private static ShipPositionProducer instance;
	 
	 private ShipPositionProducer() {
		 Properties p = getConfig().buildProducerProperties(getConfig().getProperties().getProperty(ApplicationConfig.KAFKA_PRODUCER_CLIENTID));
		 kafkaProducer = new KafkaProducer<String, String>(p);
	     topic = getConfig().getProperties().getProperty(ApplicationConfig.KAFKA_SHIP_TOPIC_NAME);
	 }
	 
	public static ShipPositionProducer getInstance() {
		if (instance == null) instance = new ShipPositionProducer();
		return instance;
	}

	public void publishShipPosition(ShipPosition sp) {
		 try {
			 String eventAsJson = parser.toJson(sp);
			 ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                topic,null,eventAsJson);
        
			 // Send record asynchronously, process acknowledge within callback
			 Future<RecordMetadata> future = kafkaProducer.send(record, new Callback() {
                 public void onCompletion(RecordMetadata metadata, Exception e) {
                     if(e != null) {
                        e.printStackTrace();
                     } else {
                        System.out.println("@@@@ The offset: " + metadata.offset() + " on partition:" + metadata.partition() );
                     }
                 }
             });
       
			 RecordMetadata recordMetadata = future.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		} 
	}
	
	public void close() {
		kafkaProducer.close(5000, TimeUnit.MILLISECONDS);
	}


}
