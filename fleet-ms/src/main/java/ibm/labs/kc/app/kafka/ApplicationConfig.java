package ibm.labs.kc.app.kafka;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
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
	
    public static final long PRODUCER_TIMEOUT_SECS = 10;
    public static final String CONSUMER_GROUP_ID = "order-command-grp";
    public static final Duration CONSUMER_POLL_TIMEOUT = Duration.ofSeconds(10);
    public static final Duration CONSUMER_CLOSE_TIMEOUT = Duration.ofSeconds(10);
	
	private static Properties properties ;
		
	
	
	private static void loadPropertiesFromStream(InputStream input){
		try {
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
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
	

	public static Properties getProperties() {
		if (properties == null) {
			properties = new Properties();
			loadPropertiesFromStream(ApplicationConfig.class.getClassLoader().getResourceAsStream("config.properties"));
		}
		return properties;
	}

	public static Properties buildConsumerProperties(String clientID) {
		String clientId = clientID;
		if (clientId == null ) {
			clientId = ApplicationConfig.getProperties().getProperty(ApplicationConfig.KAFKA_CONSUMER_CLIENTID);
		}
		ApplicationConfig.buildCommonProperties();
        
		getProperties().put(ConsumerConfig.GROUP_ID_CONFIG, 
        		getProperties().getProperty(ApplicationConfig.KAFKA_GROUPID));
        // offsets are committed automatically with a frequency controlled by the config auto.commit.interval.ms
        // here we want to use manual commit 
		getProperties().put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"false");
		getProperties().put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		getProperties().put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
		getProperties().put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
		getProperties().put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		getProperties().put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		getProperties().put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
       return getProperties();
	}
	
	public static Properties buildProducerProperties(String clientID) {
		String clientId = clientID;
		if (clientId == null ) {
			clientId = getProperties().getProperty(ApplicationConfig.KAFKA_PRODUCER_CLIENTID);
		}
		buildCommonProperties();
		getProperties().put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		getProperties().put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		getProperties().put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		getProperties().put(ProducerConfig.ACKS_CONFIG, getProperties().getProperty(ApplicationConfig.KAFKA_ACK));
		getProperties().put(ProducerConfig.RETRIES_CONFIG,getProperties().getProperty(ApplicationConfig.KAFKA_RETRIES));
		return getProperties();
	}
	
	
	/**
	 * Take into account the environment variables if set
	 * @return common kafka properties
	 */
	private static void buildCommonProperties() {
		Map<String,String> env = System.getenv();
		if (env.get("KAFKA_BROKERS") != null) {
			getProperties().setProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS,env.get("KAFKA_BROKERS"));
		} 
		properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, 
	        		getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		
		if (env.get("KAFKA_APIKEY") != null) {
				getProperties().setProperty(ApplicationConfig.KAFKA_APIKEY, env.get("KAFKA_APIKEY"));
		} 
		if (env.get("KAFKA_ENV") != null) {
			getProperties().setProperty(ApplicationConfig.KAFKA_ENV, env.get("KAFKA_ENV"));
		} 
		if ("IBMCLOUD".equals(env.get("KAFKA_ENV")) || "ICP".equals(env.get("KAFKA_ENV"))) {
			getProperties().put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
			getProperties().put(SaslConfigs.SASL_MECHANISM, "PLAIN");
			getProperties().put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"" 
               + getProperties().getProperty(ApplicationConfig.KAFKA_APIKEY)+ "\";");
			getProperties().put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
			getProperties().put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
			getProperties().put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        }
		System.out.println("Brokers " + getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
		logger.info("Env " + getProperties().getProperty(ApplicationConfig.KAFKA_ENV));
		logger.info("apikey " + getProperties().getProperty(ApplicationConfig.KAFKA_APIKEY));
	}
}
