. ./setenv.sh
docker build -t ibmcase/$kname .
# image for private registry in IBM Cloud
docker tag ibmcase/$kname registry.ng.bluemix.net/ibmcaseeda/$kname 