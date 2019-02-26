#!/bin/bash
echo "##########################################"
echo " Build Voyage microservice "
echo "##########################################"

if [[ $PWD = */scripts ]]; then
 cd ..
fi
. ./scripts/setenv.sh

docker build -t ibmcase/$kname .
if [[ $kcenv -ne "local"]]; then
    # image for private registry in IBM Cloud
    docker tag ibmcase/$kname registry.ng.bluemix.net/ibmcaseeda/$kname 
fi