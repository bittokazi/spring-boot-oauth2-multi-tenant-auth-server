#!/bin/bash

node ./frontend/server.js &
java -jar /app/gateway.jar &
java -jar /app/app.jar &

wait -n
exit $?