var kafka = require('node-rdkafka');
var config = require('../utils/config.js')

const committedTimeoutMs = 10000;

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

const getConsumerConfig = (gid) => {
    console.log('in getConsumerConfig');
    var consumerConfig = {
        'debug': 'all',
        'metadata.broker.list': config.getKafkaBrokers(),
        'broker.version.fallback': '0.10.2.1',
        'log.connection.close' : false,
        'client.id': 'voyage-consumer',
        'group.id': gid,
        'enable.auto.commit' : false
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

const getConsumerTopicConfig = () => {
    return {'auto.offset.reset':'earliest'};
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

var consumer = new kafka.KafkaConsumer(getConsumerConfig('voyage-consumer-group'), getConsumerTopicConfig());
var reloadConsumer = new kafka.KafkaConsumer(getConsumerConfig('voyage-consumer-group-reload'), getConsumerTopicConfig());
// consumer.on('event.log', function(m){
//     console.log(m);
// })

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

const reload = (subscription) => {
    consumer.connect();

    consumer.on('ready', async () => {
        console.log('Consumer on ready')
        // TODO handle multiple partitions
        consumer.committed([{ topic: subscription.topic, partition: 0, offset: -1 }], committedTimeoutMs, function(err,tps) {
            console.log('Consumer on committed cb ', err, tps);
            // TODO handle err
            var reloadLimit = tps[0].offset;
            if (reloadLimit>0) {
                reloadConsumer.connect({ timeout: 2000 }, function(err, info) {
                    console.log('ReloadConsumer connect cb', err, info);
                    reloadConsumer.subscribe([subscription.topic]); //should consume from 0
                    var finishedReloading = false;
                    while(!finishedReloading) {
                        reloadConsumer.consume(10, function(err,messages) {
                            console.log('ReloadConsumer on consume(n,messages) cb ', err);
                            for(var m of messages) {
                                if (m.offset <= reloadLimit) {
                                    subscription.callback(message, true);
                                } else {
                                    finishedReloading = true;
                                    break; // for loop
                                }
                            }
                        })
                    }
                    reloadConsumer.disconnect();
                    listen(subscription);
                });
            } else {
                listen(subscription);
            }
        });
    });

}


const listen = (subscription) => {
    console.log('Consumer in listen ' + subscription);
    consumer.on('data', function(message) {
        try {
            subscription.callback(message, false);
            consumer.commitMessageSync(message);
        } catch(err) {
            // TODO send to error queue
            logger.error(err)
        }
    });
    consumer.subscribe([subscription.topic]); //should consume from committed
    consumer.consume();
    console.log('Consumer starting consume loop');
}

module.exports = {
    emit,
    listen,
    reload
};
