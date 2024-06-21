var express = require("express");
var path = require("path");
var cors = require("cors");
var serveStatic = require("serve-static");
var fs = require("fs");
require("dotenv").config();

app = express();
app.use(cors());
app.use(serveStatic(__dirname + "/dist"));

app.get("/*", (req, res) => {
  res.sendFile(__dirname + "/dist/app/index.html");
});

var port = process.env.FE_PORT || 3003;
app.listen(port);
console.log("frontend server started " + port);
