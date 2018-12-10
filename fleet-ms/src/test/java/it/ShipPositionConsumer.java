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
import ibm.labs.kc.app.kafka.ShipPositionProducer;
import ibm.labs.kc.event.model.ShipPosition;
import util.BaseKafkaConsumer;



public class ShipPositionConsumer extends BaseKafkaConsumer {

    
    public ShipPositionConsumer() {
    	 super();
    	 prepareConsumer(getConfig().getProperties().getProperty(ApplicationConfig.KAFKA_SHIP_TOPIC_NAME),
    			 "ship-consumer");
	}
    
    public List<ShipPosition>  consume() {
    	List<ShipPosition> buffer = new ArrayList<ShipPosition>();
    	ConsumerRecords<String, String> records = kafkaConsumer.poll(
    			Long.parseLong(getConfig().getProperties().getProperty(ApplicationConfig.KAFKA_POLL_DURATION)));
    	
	    for (ConsumerRecord<String, String> record : records) {
	    	ShipPosition a = gson.fromJson(record.value(), ShipPosition.class);
	    	buffer.add(a);
	    }
    	return buffer;
    }
    
    
	public static void main(String[] args) {
		
		ShipPosition sp = new ShipPosition("MyBoat","45.0900","-122.15050");
		ShipPositionProducer positionPublisher = ShipPositionProducer.getInstance();
		positionPublisher.publishShipPosition(sp);
		
		ShipPositionConsumer consumer = new ShipPositionConsumer();
		for ( ShipPosition p : consumer.consume()) {
			System.out.println(p.toString());
		};
	}

}
