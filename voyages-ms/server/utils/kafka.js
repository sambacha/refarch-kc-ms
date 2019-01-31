var kafka = require('node-rdkafka');
var config = require('../utils/config.js')

const getCloudConfig = () => {
    return {
        'security.protocol': 'sasl_ssl',
        'ssl.ca.location': config.getCertsPath(),
        'sasl.mechanisms': 'plain',
        'sasl.username': 'token',
        'sasl.password': config.getKafkaApiKey()
    };
}

const getProducerConfig = () => {
    console.log('in getProducerConfig');
    var producerConfig = {
        'debug': 'all',
        'metadata.broker.list': config.getKafkaBrokers(),
        'broker.version.fallback': '0.10.2.1',
        'log.connection.close' : false,
        'client.id': 'voyage-producer',
        'dr_msg_cb': true // Enable delivery reports with message payload
    };
    if (config.getKafkaEnvironment() == "IBMCLOUD") {
        eventStreamsConfig = getCloudConfig()
        for (var key in eventStreamsConfig) { 
            producerConfig[key] = eventStreamsConfig[key];
        }
    }
    console.log('producer configs' + JSON.stringify(producerConfig));
    return producerConfig;
}

const getConsumerConfig = () => {
    console.log('in getConsumerConfig');
    var consumerConfig = {
        'debug': 'all',
        'metadata.broker.list': config.getKafkaBrokers(),
        'broker.version.fallback': '0.10.2.1',
        'log.connection.close' : false,
        'client.id': 'voyage-consumer',
        'group.id': 'voyage-consumer-group'
    };
    if (config.getKafkaEnvironment() == "IBMCLOUD") {
        eventStreamsConfig = getCloudConfig()
        for (var key in eventStreamsConfig) { 
            consumerConfig[key] = eventStreamsConfig[key];
        }
    }
    console.log('consumer configs' + JSON.stringify(consumerConfig));
    return consumerConfig;
}

var producer = new kafka.Producer(getProducerConfig(), {
    'request.required.acks': -1,
    'produce.offset.report': true,
    'message.timeout.ms' : 10000  //speeds up a producer error response
});

producer.setPollInterval(100);
producer.connect();
var ready = false;
producer.on('ready', async () => {
    console.log('Producer connected to Kafka');
    ready = true;
});

producer.on('delivery-report', (err, report) => {
    if (typeof report.opaque === 'function') {
        report.opaque.call(null, err, report);
    } else {
        console.error('Assertion failed: opaque not a function!' + err);
    }
});

var consumer = new kafka.KafkaConsumer(getConsumerConfig());

const emit = (key, event) => {
    if (!ready) {
        // kafka will handle reconnections but the produce method should never 
        // be called if the client was never 'ready'
        console.log('Producer never connected to Kafka yet');
        return Promise.reject(new Error('Producer never connected to Kafka yet'));
    }

    return new Promise((resolve, reject) => {
        try {
            producer.produce(config.getOrderTopicName(), 
                null, /* partition */
                new Buffer(JSON.stringify(event)),
                key,
                Date.now(),
                (err, report) => {
                    if (err) return reject(err);
                    return resolve(report);
                }
            );
        } catch (e) {
            console.error('Failed sending event ' + JSON.stringify(event) + " error:" + e);
            return reject(e);
        }
    });
}

const listen = (subscription) => {
    consumer.connect()
    consumer.on('ready', async () => {
        consumer.on('data', function(message) {
            subscription.callback(message);
        });
        consumer.subscribe([subscription.topic]);
        consumer.consume();
        console.log('Consumer connected to Kafka and subscribed');
    });
}

module.exports = {
    emit,
    listen
};
