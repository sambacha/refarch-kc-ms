# Voyage Service

This service keeps track of each scheduled, current or completed voyage of a container ship, being loaded with containers at a source port, sailing to a destination port and having onboard containers unloaded there. 

A generated IBM Cloud application

[![](https://img.shields.io/badge/IBM%20Cloud-powered-blue.svg)](https://bluemix.net)

## Run locally as Node.js application

```bash
npm install
npm test
npm start
```

## Build, run, and deploy using IDT

```bash
# Install needed dependencies:
npm run idt:install
# Build the docker image for your app:
npm run idt:build
# Run the app locally through docker:
npm run idt:run
# Deploy your app to IBM Cloud:
npm run idt:deploy
```
