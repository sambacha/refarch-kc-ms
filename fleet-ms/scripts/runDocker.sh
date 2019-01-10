export KAFKA_BROKERS="localhost:9092"
export KAFKA_ENV="LOCAL"
docker rm kc-fleetms
docker run --name kc-fleetms -e KAFKA_BROKERS -e KAFKA_ENV -p 9080:9080 -p 9444:9443 ibmcase/kc-fleetms 
