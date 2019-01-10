package ibm.labs.kc.app.kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.google.gson.Gson;

/**
 * Common to all consumers
 * @author jeromeboyer
 *
 */
public class BaseKafkaConsumer {
	protected static KafkaConsumer<String, String> kafkaConsumer;
    
	protected Gson gson = new Gson();
	protected ApplicationConfig config;
    
	public BaseKafkaConsumer() {
   	   this.config = new ApplicationConfig();
	}
	
	protected void prepareConsumer(String topicName,String clientID) {
		Properties properties = this.config.buildConsumerProperties(clientID);
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topicName));
	}
    
    public void commitOffset() {
    	kafkaConsumer.commitSync();
    }
    
    public void close() {
    	kafkaConsumer.close();
    }

	public static KafkaConsumer<String, String> getKafkaConsumer() {
		return kafkaConsumer;
	}

	public Gson getGson() {
		return gson;
	}

	public ApplicationConfig getConfig() {
		return config;
	}
}
