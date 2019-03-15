#!/bin/bash

MAIN_CLASS=com.echostreams.pulsar.jms.client.client.PulsarJMSClientProvider
MAIN_JAR=pulsar-jms-provider-client-1.0-SNAPSHOT.jar

APP_HOME=/home/pulsar-jms-provider-client-1.0-SNAPSHOT

# Setup the JVM
if [ "x$JAVA_HOME" != "x" ]; then
    JAVA=$JAVA_HOME/bin/java
else
    JAVA="java"
fi

# Setup the classpath
CP="$APP_HOME"
CP="$CP:$APP_HOME/$MAIN_JAR"

JAVA_OPTS="-Xms256m -Xmx512m"

exec $JAVA $JAVA_OPTS -cp $CP $MAIN_CLASS