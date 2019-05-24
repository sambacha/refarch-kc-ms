# Voyages and Fleet Simulation Solution Microservices

This repository includes a set of sub projects to implement the different microservices and functions to support the simulation of container shipment as introduced by [this main repository](https://ibm-cloud-architecture.github.io/refarch-kc).

This repository addresses the implementation of the orange boxes in the figure below:  

![](images/fleet-voyage-comp.png)

## Sub repositories

* The `fleet-ms` folder contains the Java App developed using microprofile and deployed on Open Liberty. It uses Kafka API to produce events. See [this chapter for details](fleetms.md) about deployment and code explanations.
* The `voyages-ms` folder contains the Node.js app for the voyages microservice also created with microprofile. It uses the Kafka API to produce events when an order has been assigned to a voyage. See [this chapter for details.](voyagems.md)

## Pre-requisites


## Build and run locally

## Testing

### Testing the fleet service

### Testing the voyage service


## Further readings

* [Event driven architecture in IBM Garage method](https://www.ibm.com/cloud/garage/architectures/eventDrivenArchitecture)
* [Event driven compagnion github with other best practices](https://ibm-cloud-architecture.github.io/refarch-eda/)
* [Event-driven training journey](https://ibm-cloud-architecture.github.io/refarch-eda/eda-skill-journey/)
