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
    console.log('assigning an order to voyage ' + voyageID);
    var voyageID = req.params.voyageID;
    console.log('body ' + JSON.stringify(req.body));
    var orderID = req.body.orderID;
    var containers = req.body.containers;
    console.log('OrderID ' + orderID);
    console.log('Containers ' + containers);
    
    var event = {
      'timestamp':  Date.now(),
      'type': 'OrderAssigned',
      'version': '1',
      'payload': {
        'voyageID': voyageID,
        'orderID': orderID
      }
    }
    console.log('built' + JSON.stringify(event));
    kafka.emit(event).then (function(fulfilled) {
      console.log('fulfilled' + event);  
      res.json(event);
    }).catch(function(err){
      console.log('rejected' + err);  
      res.status(500).send('Error occured');
    });

  });

  app.use('/voyage', router);
}

const cb = (message) => {
  console.log('received a message');
  var event = JSON.parse(message.value.toString());
  console.log(event);
  if (event.type === 'OrderCreated') {
    // For UI demo purpose, wait 10 secs before assigning this order to a voyage
    setTimeout(function() {
      var voyageID = 123;
      var assignEvent = {
        'timestamp':  Date.now(),
        'type': 'OrderAssigned',
        'version': '1',
        'payload': {
          'voyageID': voyageID,
          'orderID': event.payload.orderID
        }
      }
      console.log('built' + JSON.stringify(assignEvent));
      kafka.emit(assignEvent).then (function(fulfilled) {
        console.log('fulfilled' + assignEvent);  
      }).catch(function(err){
        console.log('rejected' + err);  
      });
    }, 10000)
   
  }
}

kafka.listen({
  'topic':'orders',
  'callback': cb
});



