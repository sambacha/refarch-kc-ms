# K Container integration tests
This is a project to test some of the challenging requirements like event sourcing with fail over, Saga pattern with recovery and fail over.

## How to proof the event sourcing and CQRS


## How to proof the SAGA pattern
We want to validate the SAGA pattern to support the long running, cross microservices, order transactions. The diagram is illustrating the use case we want to proof and tests:

![](docs/saga-ctx.png)

What we need to proof for the happy path:
* Send a new order to the order microservice via API with all the data to ship fresh goods from two countries separated by ocean
* verify the status of the order is pending
* The unique cross services key is the order ID
* verify orderCreated event was published
* verify voyage was allocated to order
* verify container was allocated to order
* verify ship has new containers added to its plan shipping plan 
* verify the status of the order is assigned

Business exeception error: no container for this type of load is available in this time frame. So the business response will be to keep the order in pending but trigger a business process for a customer representative to be in contact with the manufacturer for a remediation plan. 
* Verify the response to the container service is an OrderUnfulled event.
