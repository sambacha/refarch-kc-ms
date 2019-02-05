find target -iname "*SNAPSHOT*" -print | xargs rm -rf
rm -rf target/liberty/wlp/usr/servers/defaultServer/apps/expanded
mvn install -DskipITs
# image for public docker hub
docker build -t ibmcase/kc-fleetms .
# image for private registry in IBM Cloud
docker tag ibmcase/kc-fleetms registry.ng.bluemix.net/ibmcaseeda/kc-fleetms 
