package it;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.gson.Gson;

import ibm.labs.kc.app.kafka.ApplicationConfig;
import ibm.labs.kc.app.kafka.PositionPublisher;
import ibm.labs.kc.event.model.ShipPosition;



public class ShipPositionConsumer {


	private static KafkaConsumer<String, String> kafkaConsumer;
    
    private Gson gson = new Gson();
    public ApplicationConfig config;
    
    public ShipPositionConsumer(ApplicationConfig cfg) {
     this.config = cfg;	
     prepareConsumer();
    }
    
    public ShipPositionConsumer() {
    	 this.config = new ApplicationConfig();
    	 prepareConsumer();
	}

	private void prepareConsumer() {
		Properties properties = this.config.buildConsumerProperties();
        
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(config.getProperties().getProperty(ApplicationConfig.KAFKA_SHIP_TOPIC_NAME)));
	}
    
    public List<ShipPosition>  consume() {
    	List<ShipPosition> buffer = new ArrayList<ShipPosition>();
    	ConsumerRecords<String, String> records = kafkaConsumer.poll(
    			Long.parseLong(config.getProperties().getProperty(ApplicationConfig.KAFKA_POLL_DURATION)));
    	
	    for (ConsumerRecord<String, String> record : records) {
	    	ShipPosition a = gson.fromJson(record.value(), ShipPosition.class);
	    	buffer.add(a);
	    }
    	return buffer;
    }
    
    public void commitOffset() {
    	kafkaConsumer.commitSync();
    }
    
    public void close() {
    	kafkaConsumer.close();
    }
    
	public static void main(String[] args) {
		
		ShipPosition sp = new ShipPosition("MyBoat","45.0900","-122.15050");
		PositionPublisher positionPublisher = new PositionPublisher();
		positionPublisher.publishShipPosition(sp);
		
		ShipPositionConsumer consumer = new ShipPositionConsumer();
		for ( ShipPosition p : consumer.consume()) {
			System.out.println(p.toString());
		};
	}

}
