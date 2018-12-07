package ibm.labs.kc.app.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import com.google.gson.Gson;

public abstract class Publisher {
	 protected ApplicationConfig  config = new ApplicationConfig();;
	 protected  KafkaProducer<String, String> kafkaProducer;
	 protected  String topic;
	 protected  Gson parser = new Gson();
	 
	public ApplicationConfig getConfig() {
		return config;
	}
	public void setConfig(ApplicationConfig config) {
		this.config = config;
	}
	public KafkaProducer<String, String> getKafkaProducer() {
		return kafkaProducer;
	}
	public void setKafkaProducer(KafkaProducer<String, String> kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public Gson getParser() {
		return parser;
	}
	public void setParser(Gson parser) {
		this.parser = parser;
	};
}
