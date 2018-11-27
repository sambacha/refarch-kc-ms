package ibm.labs.kc.app.kafka;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

public class ApplicationConfig {
		
	public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";
	public static final String KAFKA_SHIP_TOPIC_NAME = "kafka.ship.topic.name";
	public static final String KAFKA_CONTAINER_TOPIC_NAME = "kafka.container.topic.name";
	public static final String KAFKA_GROUPID = "kafka.groupid";
	public static final String KAFKA_CONSUMER_CLIENTID = "kafka.consumer.clientid";
	private static final String KAFKA_PRODUCER_CLIENTID = "kafka.producer.clientid";
	public static final String KAFKA_USER = "kafka.user";
	public static final String KAFKA_PWD = "kafka.password";
	public static final String KAFKA_APIKEY = "kafka.api_key";
	public static final String KAFKA_POLL_DURATION = "kafka.poll.duration";
	public static final String VERSION = "version";
	
	
	private Properties p;
		
	public ApplicationConfig() {
		InputStream input = null;
		p = new Properties();
		try {
			input = getClass().getClassLoader().getResourceAsStream("config.properties");
			p.load(input);
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
	
	public Properties getProperties() {
		return p;
	}

	public Properties buildConsumerProperties() {
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
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, getProperties().getProperty(ApplicationConfig.KAFKA_CONSUMER_CLIENTID));
       return properties;
	}
	
	public Properties buildProducerProperties() {
		Properties properties = buildCommonProperties();
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, getProperties().getProperty(ApplicationConfig.KAFKA_PRODUCER_CLIENTID));
		properties.put(ProducerConfig.ACKS_CONFIG, "-1");
        return properties;
	}
	
	
	private Properties buildCommonProperties() {
		Properties properties = new Properties();
        properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, 
        		getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS));
        if (! getProperties().getProperty(ApplicationConfig.KAFKA_BOOTSTRAP_SERVERS).startsWith("gc-kafka")) {
        	properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            properties.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"" 
               + getProperties().getProperty(ApplicationConfig.KAFKA_APIKEY)+ "\";");
            properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
            properties.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
            properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        }
        
        return properties;
	}
	
	private  void setDefaults() {	 
		 p.setProperty(KAFKA_BOOTSTRAP_SERVERS, "gc-kafka-0.gc-kafka-hl-svc.greencompute.svc.cluster.local:32224");
		 p.setProperty(KAFKA_SHIP_TOPIC_NAME,"ship");
		 p.setProperty(KAFKA_GROUPID,"kcgroup");
		 p.setProperty(KAFKA_POLL_DURATION, "10000");
		 p.setProperty(VERSION, "v0.0.1");
	}
}
