package ibm.labs.kc.app.kafka;

import com.google.gson.Gson;

public abstract class BaseProducer {
	 protected ApplicationConfig  config = new ApplicationConfig();;
	
	 protected  String topic;
	 protected  Gson parser = new Gson();
	 
	public ApplicationConfig getConfig() {
		return config;
	}
	public void setConfig(ApplicationConfig config) {
		this.config = config;
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
