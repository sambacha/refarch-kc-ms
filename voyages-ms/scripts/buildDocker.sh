#!/bin/bash
echo "##########################################"
echo " Build Voyage microservice "
echo "##########################################"
export LDFLAGS=-L/usr/local/opt/openssl/lib
export CPPFLAGS=-I/usr/local/opt/openssl/include

if [[ $PWD = */scripts ]]; then
 cd ..
fi
if [[ $# -eq 0 ]];then
  kcenv="local"
else
  kcenv=$1
fi

. ./scripts/setenv.sh

docker build -t ibmcase/$kname .
if [[ "$kcenv" != "local" ]]; then
    # image for private registry in IBM Cloud
    docker tag ibmcase/$kname us.icr.io/ibmcaseeda/$kname 
fi