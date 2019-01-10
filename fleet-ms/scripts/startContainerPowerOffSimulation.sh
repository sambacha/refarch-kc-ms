#/bin/bash
set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi

curl -v  -H "accept: */*" -H "Content-Type: application/json" -d @./scripts/shipCtlPowerOff.json http://localhost:9080/fleetms/ships/simulate