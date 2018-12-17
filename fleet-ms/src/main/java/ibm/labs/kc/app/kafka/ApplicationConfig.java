package ibm.labs.kc.app.kafka;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * This class is to read configuration from properties file and keep in a properties object.
 * It also provides a set of method to define kafka config parameters 
 * 
 * @author jerome boyer
 *
 */
public class ApplicationConfig {
	public static Logger logger = Logger.getLogger(ApplicationConfig.class.getName());
		
	public static final String KAFKA_ENV = "kafka.env";
	public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";
	public static final String KAFKA_SHIP_TOPIC_NAME = "kafka.ship.topic.name";
	public static final String KAFKA_CONTAINER_TOPIC_NAME = "kafka.container.topic.name";
	public static final String KAFKA_BW_PROBLEM_TOPIC_NAME = "kafka.bw.problem.topic.name";
	public static final String KAFKA_GROUPID = "kafka.groupid";
	public static final String KAFKA_CONSUMER_CLIENTID = "kafka.consumer.clientid";
	public static final String KAFKA_PRODUCER_CLIENTID = "kafka.producer.clientid";
	public static final String KAFKA_ACK = "kafka.ack";
	public static final String KAFKA_RETRIES = "kafka.retries";
	public static final String KAFKA_USER = "kafka.user";
	public static final String KAFKA_PWD = "kafka.password";
	public static final String KAFKA_APIKEY = "kafka.api_key";
	public static final String KAFKA_POLL_DURATION = "kafka.poll.duration";
	public static final String VERSION = "version";
	
	private Properties properties = new Properties();
		
	public ApplicationConfig() {
		loadProperties();
	}
	
	public ApplicationConfig(String fileName) {
		loadProperties(fileName);
	}
	
	public void loadProperties() {
		loadPropertiesFromStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
	}
	
	private void loadPropertiesFromStream(InputStream input){
		try {
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
			setDefaults();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadProperties(String fn) {
		try {
			loadPropertiesFromStream(new FileInputStream(fn));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			setDefaults();
		}
	}
	
	public Properties getProperties() {
		return properties;
	}

	public Properties buildConsumerProperties(String clientID) {
		String clientId = clientID;
		if (clientId == null ) {
			clientId = getProperties().getProperty(ApplicationConfig.KAFKA_CONSUMER_CLIENTID);
		}
		Properties properties = buildCommonProperties();
        
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, 
        		getProperties().getProperty(ApplicationConfig.KAFKA_GROUPID));
        // offsets are committed automatically with a frequency controlled by the config auto.commit.interval.ms
        // here we want to use manual commit 
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
       return properties;
	}
	
	public Properties buildProducerProperties(String clientID) {
		String clientId = clientID;
		if (clientId == null ) {
			clientId = getProperties().getProperty(ApplicationConfig.KAFKA_PRODUCER_CLIENTID);
		}
		Properties properties = buildCommonProperties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		properties.put(ProducerConfig.ACKS_CONFIG, getProperties().getProperty(ApplicationConfig.KAFKA_ACK));
		properties.put(ProducerConfig.RETRIES_CONFIG,getProperties().getProperty(ApplicationConfig.KAFKA_RETRIES));
        return properties;
	}
	
	
	/**
	 * Take into account the environment variables if set
	 * @return common kafka properties
	 */
	private Properties buildCommonProperties() {
		Properties properties = new Properties();
		Map<String,String> env=System.getenv();
		if (env.get("KAFKA_BROKERS") != null) {
			getProperties().setProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS,env.get("KAFKA_BROKERS"));
		} 
		properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, 
	        		getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		
		if (env.get("KAFKA_APIKEY") != null) {
				getProperties().setProperty(ApplicationConfig.KAFKA_APIKEY, env.get("KAFKA_APIKEY"));
		} 
		if ("IBMCLOUD".equals(env.get("KAFKA_ENV")) || "ICP".equals(env.get("KAFKA_ENV"))) {
        	properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            properties.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"" 
               + getProperties().getProperty(ApplicationConfig.KAFKA_APIKEY)+ "\";");
            properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
            properties.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
            properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        }
		logger.info("Brokers " + getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
        return properties;
	}
	
	private  void setDefaults() {	 
		properties.setProperty(KAFKA_BOOTSTRAP_SERVERS, "gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224");
		properties.setProperty(KAFKA_SHIP_TOPIC_NAME,"ship");
		properties.setProperty(KAFKA_GROUPID,"kcgroup");
		properties.setProperty(KAFKA_POLL_DURATION, "10000");
		properties.setProperty(VERSION, "v0.0.1");
	}
}
