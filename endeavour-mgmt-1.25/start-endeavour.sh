DIR=$(dirname $0)
cd $DIR/tomcat/bin
export CATALINA_OPTS="-Xms256m -Xmx768m -XX:MaxPermSize=256m -Duser.language=en -Duser.country=EN -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000"
sh startup.sh

