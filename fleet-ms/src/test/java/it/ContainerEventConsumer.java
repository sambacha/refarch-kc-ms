package it;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import ibm.labs.kc.app.kafka.ApplicationConfig;
import ibm.labs.kc.event.model.ContainerMetric;

public class ContainerEventConsumer extends BaseKafkaConsumer {
    
    public ContainerEventConsumer() {
    	 super();
    	 prepareConsumer(getConfig().getProperties().getProperty(ApplicationConfig.KAFKA_CONTAINER_TOPIC_NAME),
    			 getConfig().getProperties().getProperty(ApplicationConfig.KAFKA_CONSUMER_CLIENTID)+"_container");
	}

    
    public List<ContainerMetric>  consume() {
    	List<ContainerMetric> buffer = new ArrayList<ContainerMetric>();
    	ConsumerRecords<String, String> records = kafkaConsumer.poll(
    			Long.parseLong(config.getProperties().getProperty(ApplicationConfig.KAFKA_POLL_DURATION)));
    	
	    for (ConsumerRecord<String, String> record : records) {
	    	ContainerMetric a = gson.fromJson(record.value(), ContainerMetric.class);
	    	buffer.add(a);
	    }
    	return buffer;
    }
    
}
