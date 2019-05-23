# Voyage Management Service

This service keeps track of each scheduled, current or completed voyage of a container ship, being loaded with containers at a source port, sailing to a destination port and having onboard containers unloaded there. 

A generated IBM Cloud application

[![](https://img.shields.io/badge/IBM%20Cloud-powered-blue.svg)](https://bluemix.net)


## Build

The script `.script/buildDocker.sh` build the docker image for this service.

## Run locally as Node.js application

This is more for development purpose in your sandbox.

```bash
npm install
npm test
npm start
```

## Deploy on IBM Kubernetes Service

```sh
# to install the helm release under browncompute namespace
$ helm install voyagesms/ --name kc-voyagesms --namespace browncompute 
# To see the pod deployment
$ kubectl describe pod voyagesms  --namespace browncompute
# To get the exposed port
$ kubectl get service kc-voyagesms
$ kubectl get service voyagesms-application-service -n browncompute
# Status and traced of the pods:
$ kubectl logs fleetms-deployment-58b7d58fb8-qcqz7  -n browncompute
# then point your URL to the ipaddress and port number:
http://
```

## NOT VERIFIED Build, run, and deploy using IDT

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
