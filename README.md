# KC Solution Microservices

This repository includes a set of sub projects to implement the different microservices and funtions to support the simulation of container shipment as introduce by [this main repository](https://github.com/ibm-cloud-architecture/refarch-kc).

This repository addresses the implementation of the yellow boxes in the figure below:  

![](https://github.com/ibm-cloud-architecture/refarch-kc/blob/master/docs/kc-hl-comp-view.png)


## Sub repositories

* The `fleet-ms` folder contains the Java App developed using microprofile and deployed on Open Liberty. It uses Kafka API to produce events. See [the readme for details](./fleet-ms/README.md) about deployment and code explanations.



## Project Status
[12/2018] Just started

## Contributors
* Lead development [Jerome Boyer](https://www.linkedin.com/in/jeromeboyer/)
* [Hemankita Perabathini](https://www.linkedin.com/in/hemankita-perabathini/)

Please [contact me](mailto:boyerje@us.ibm.com) for any questions.