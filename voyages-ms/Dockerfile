FROM node:8-stretch

# Change working directory
WORKDIR "/app"

# Update packages and install dependency packages for services
RUN apt-get update \
 && apt-get dist-upgrade -y \
 && dpkg -P libssl-dev \
 && apt-get install liblz4-dev libssl1.0-dev libsasl2-dev \
 && apt-get clean \
 && echo 'Finished installing dependencies'

# Install npm production packages
COPY package.json /app/
RUN cd /app && npm install --production

COPY . /app

ENV NODE_ENV production
ENV PORT 3000
ENV KAFKA_BROKERS kafka03-prod02.messagehub.services.us-south.bluemix.net:9093,kafka01-prod02.messagehub.services.us-south.bluemix.net:9093,kafka02-prod02.messagehub.services.us-south.bluemix.net:9093,kafka04-prod02.messagehub.services.us-south.bluemix.net:9093,kafka05-prod02.messagehub.services.us-south.bluemix.net:9093
ENV KAFKA_ENV IBMCLOUD
ENV KAFKA_APIKEY 9WKzmHOGX6K6DwsNxKXBnrhijskOlMmDVdOgZdB8aTEzEJzJ

EXPOSE 3000

CMD ["npm", "start"]
