export KAFKA_BROKERS="kafka03-prod02.messagehub.services.us-south.bluemix.net:9093,kafka01-prod02.messagehub.services.us-south.bluemix.net:9093,kafka02-prod02.messagehub.services.us-south.bluemix.net:9093,kafka04-prod02.messagehub.services.us-south.bluemix.net:9093,kafka05-prod02.messagehub.services.us-south.bluemix.net:9093"
export KAFKA_ENV="IBMCLOUD"
export KAFKA_APIKEY="9WKzmHOGX6K6DwsNxKXBnrhijskOlMmDVdOgZdB8aTEzEJzJ"
mvn liberty:run-server
