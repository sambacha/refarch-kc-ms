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

public class PositionPublisher extends Publisher{

	 
	 public PositionPublisher() {
		 Properties p = config.buildProducerProperties(config.getProperties().getProperty(ApplicationConfig.KAFKA_PRODUCER_CLIENTID));
		 kafkaProducer = new KafkaProducer<String, String>(p);
	     topic = config.getProperties().getProperty(ApplicationConfig.KAFKA_SHIP_TOPIC_NAME);
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
                        System.out.println("The offset of the record we just sent is: " + metadata.offset());
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
