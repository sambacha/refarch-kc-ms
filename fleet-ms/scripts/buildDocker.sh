find target -iname "*SNAPSHOT*" -print | xargs rm -rf
rm -rf target/liberty/wlp/usr/servers/defaultServer/apps/expanded
mvn install -DskipITs
docker build -t ibmcase/kc-fleetms .
docker tag ibmcase/kc-fleetms registry.ng.bluemix.net/ibmcaseeda/kc-fleetms 
