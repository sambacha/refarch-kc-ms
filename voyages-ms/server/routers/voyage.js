var express = require('express');

var json = require("../../data/voyages.json")

module.exports = function(app) {
  var router = express.Router();

  router.get('/', function (req, res, next) {
    res.send(json);
  });

  app.use("/voyage", router);
}



