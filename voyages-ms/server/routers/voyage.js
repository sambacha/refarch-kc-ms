var express = require('express');
// mockup of backend data source. Can be changed later !
var voyagesList = require('../../data/voyages.json');
// voyages is a consumer of orders topic for OrderCreated event 
// and a producer when a voyage is assigned to an order 
var kafka = require('../utils/kafka.js');



module.exports = function(app) {
  var router = express.Router();

  // List all existing voyages
  router.get('/', function (req, res, next) {
    res.send(voyagesList);
  });

  // Assign an order to a voyage according to the number of container
  // Post data: {'orderID': 'a-orderid-as-key-in-orders-topic', 'containers': 2'}
  router.post('/:voyageID/assign/', function(req, res, next) {
    var voyageID = req.params.voyageID;
    var orderID = req.body.orderID;
    var containers = req.body.containers;
    console.log('assigning order ' + orderID + ' to voyage ' + voyageID);
    
    var event = {
      'timestamp':  Date.now(),
      'type': 'OrderAssigned',
      'version': '1',
      'payload': {
        'voyageID': voyageID,
        'orderID': orderID
      }
    }

    kafka.emit(orderID, event).then ( function(fulfilled) {
      console.log('Emitted ' + JSON.stringify(event));  
      res.json(event);
    }).catch( function(err){
      console.log('Rejected' + err);  
      res.status(500).send('Error occured');
    });

  });

  app.use('/voyage', router);
}

const cb = (message, reloading) => {
  var event = JSON.parse(message.value.toString());
  console.log('Event received ' + JSON.stringify(event));
  if (event.type === 'OrderCreated') {

    // For UI demo purpose, wait 30 secs before assigning this order to a voyage    
    var timeoutMs = reloading ? 0 : 30000;
    
    setTimeout(function() {
      
      var matchedVoyage = findSuitableVoyage(event.payload);
      var assignOrCancelEvent;
      if (matchedVoyage.voyageID) {
          assignOrCancelEvent = {
          'timestamp':  Date.now(),
          'type': 'OrderAssigned',
          'version': '1',
          'payload': {
            'voyageID': matchedVoyage.voyageID,
            'orderID': event.payload.orderID
          }
        }
      } else {
        assignOrCancelEvent = {
          'timestamp':  Date.now(),
          'type': 'OrderCancelled',
          'version': '1',
          'payload': {
            'reason': matchedVoyage.reason,
            'orderID': event.payload.orderID
          }
        }
      }

      if(!reloading) {
        console.log('Emitting ' + assignOrCancelEvent.type);  
        kafka.emit(event.payload.orderID, assignOrCancelEvent).then (function(fulfilled) {
          console.log('Emitted ' + JSON.stringify(assignOrCancelEvent));  
        }).catch(function(err){
          console.log('Rejected' + err);  
        });
      }
    }, timeoutMs)
   
  }
}

kafka.reload({
  'topic':'orders',
  'callback': cb
});


const findSuitableVoyage = (order) => {
  for (v of voyagesList) {
    if (v.destPort === order.destinationAddress.city) {
      if (v.freeCapacity >= order.quantity) {
        v.freeCapacity -= order.quantity;
        return { 'voyageID': v.voyageID};
      }
      return { 'reason': 'Insufficient free capacity'};
    }
  }
  return { 'reason': 'No matching destination'};
}
