set p = $(echo $PWD | awk -v h="scripts" '$0 ~h')
if [[ $PWD = */scripts ]]; then
 cd ..
fi
. ./scripts/setenv.sh

docker build -t ibmcase/$kname .
# image for private registry in IBM Cloud
docker tag ibmcase/$kname registry.ng.bluemix.net/ibmcaseeda/$kname 