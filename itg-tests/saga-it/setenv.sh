#!/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
export KAFKA_BROKERS=
export MSORDER=localhost:9080
