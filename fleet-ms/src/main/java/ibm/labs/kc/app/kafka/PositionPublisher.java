package ibm.labs.kc.app.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.google.gson.Gson;

import ibm.labs.kc.event.model.ShipPosition;

public class PositionPublisher extends Publisher{

	 
	 public PositionPublisher() {
		 Properties p = config.buildProducerProperties();
		 kafkaProducer = new KafkaProducer<String, String>(p);
	     topic = config.getProperties().getProperty(ApplicationConfig.KAFKA_SHIP_TOPIC_NAME);
	 }

	public void publishShipPosition(ShipPosition sp) {
		 try {
			 String eventAsJson = parser.toJson(sp);
			 ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                topic,null,eventAsJson);
        
			 // Send record asynchronously
			 Future<RecordMetadata> future = kafkaProducer.send(record);
       
			 RecordMetadata recordMetadata = future.get(5000, TimeUnit.MILLISECONDS);
			 System.out.println(eventAsJson + " event sent -> offet: " + recordMetadata.offset());
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		} 
	}
	
	public void close() {
		kafkaProducer.close(5000, TimeUnit.MILLISECONDS);
	}
}
