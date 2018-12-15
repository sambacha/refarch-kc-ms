export KAFKA_BROKERS="localhost:9092"
export KAFKA_ENV="LOCAL"
docker rm fleetms
docker run --name fleetms -e KAFKA_BROKERS -e KAFKA_ENV -p 9081:9080 -p 9444:9443 ibmcase/fleetms 
