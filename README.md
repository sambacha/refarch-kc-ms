# KC Solution Microservices

This repository includes a set of sub projects to implement the different microservices and functions to support the simulation of container shipment as introduced by [this main repository](https://github.com/ibm-cloud-architecture/refarch-kc).

This repository addresses the implementation of the yellow boxes in the figure below:  

![](https://github.com/ibm-cloud-architecture/refarch-kc/blob/master/docs/kc-hl-comp-view.png)


## Sub repositories

* The `fleet-ms` folder contains the Java App developed using microprofile and deployed on Open Liberty. It uses Kafka API to produce events. See [the readme for details](./fleet-ms/README.md) about deployment and code explanations.
* The `container-ms` folder contains the Java app for the container microservice, also created with microprofile and deployable on Open Liberty. 
* The `voyages-ms` folder contains the Node.js app for the voyages microservice also created with microprofile. It uses the Kafka API to produce events when an order has been assigned to a voyage.

## Project Status
[12/2018] Just started

## Contributors
If you want to contribute please read [this note.](CONTRIBUTING.md)
* Lead development [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)
* Developer [Hemankita Perabathini](https://www.linkedin.com/in/hemankita-perabathini/)
* Developer [Edoardo Comar](https://www.linkedin.com/in/edoardo-comar/)
* Developer [Mickael Maison](https://www.linkedin.com/in/mickaelmaison/)

Please [contact me](mailto:boyerje@us.ibm.com) for any questions.
