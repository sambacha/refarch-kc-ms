var express = require('express');

var voyagesList = require('../../data/voyages.json');
var kafka = require('../utils/kafka.js');



module.exports = function(app) {
  var router = express.Router();

  // List all existing voyages
  router.get('/', function (req, res, next) {
    res.send(voyagesList);
  });

  // Assign an order to a voyage
  // Post data: {'orderID': 'qwerty', 'containers': 2'}
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

    kafka.emit(event).then ( function(fulfilled) {
      console.log('Emitted ' + JSON.stringify(event));  
      res.json(event);
    }).catch( function(err){
      console.log('Rejected' + err);  
      res.status(500).send('Error occured');
    });

  });

  app.use('/voyage', router);
}

const cb = (message) => {
  var event = JSON.parse(message.value.toString());
  console.log('Event received ' + JSON.stringify(event));
  if (event.type === 'OrderCreated') {
    // For UI demo purpose, wait 30 secs before assigning this order to a voyage
    setTimeout(function() {
      
      var voyageID = findSuitableVoyage(event.payload);
      var assignOrCancelEvent;
      if (voyageID) {
          assignOrCancelEvent = {
          'timestamp':  Date.now(),
          'type': 'OrderAssigned',
          'version': '1',
          'payload': {
            'voyageID': voyageID,
            'orderID': event.payload.orderID
          }
        }
      } else {
        assignOrCancelEvent = {
          'timestamp':  Date.now(),
          'type': 'OrderCancelled',
          'version': '1',
          'payload': {
            'reason': 'No suitable Voyages found.',
            'orderID': event.payload.orderID
          }
        }
      }

      console.log('Emitting ' + assignOrCancelEvent.type);  
      kafka.emit(assignOrCancelEvent).then (function(fulfilled) {
        console.log('Emitted ' + JSON.stringify(assignOrCancelEvent));  
      }).catch(function(err){
        console.log('Rejected' + err);  
      });
    }, 30000)
   
  }
}

kafka.listen({
  'topic':'orders',
  'callback': cb
});


const findSuitableVoyage = (order) => {
  for (v of voyagesList) {
    if (v.destPort === order.destinationAddress.city) {
      return v.voyageID;
    }
  }
  return null;
}
