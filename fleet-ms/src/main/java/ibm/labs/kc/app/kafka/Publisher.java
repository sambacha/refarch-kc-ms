package ibm.labs.kc.app.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import com.google.gson.Gson;

public abstract class Publisher {
	 protected ApplicationConfig  config = new ApplicationConfig();;
	 protected  KafkaProducer<String, String> kafkaProducer;
	 protected  String topic;
	 protected  Gson parser = new Gson();;
}
