package ibm.labs.kc.app.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import ibm.labs.kc.event.model.ContainerMetric;

public class ContainerPublisher extends Publisher{

	public ContainerPublisher() {
		 Properties p = config.buildProducerProperties();
		 kafkaProducer = new KafkaProducer<String, String>(p);
	     topic = config.getProperties().getProperty(ApplicationConfig.KAFKA_CONTAINER_TOPIC_NAME);
	}
	
	
	public void publishContainerMetric(ContainerMetric c) {
		 try {
			 ProducerRecord<String, String> record = new ProducerRecord<String, String>(
               topic,null,parser.toJson(c));
       
			 // Send record asynchronously
			 Future<RecordMetadata> future = kafkaProducer.send(record);
      
			 RecordMetadata recordMetadata = future.get(5000, TimeUnit.MILLISECONDS);
			 System.out.println(" -> sent" + recordMetadata.offset());
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		} finally {
           kafkaProducer.close(5000, TimeUnit.MILLISECONDS);
       }
	}
}
