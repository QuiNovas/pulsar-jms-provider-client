# pulsar-jms-provider-client

Example to send and receiver messages to pulsar through pulsar-jms-provider api

#Initial DEMO ===== Deployed executable build on aws ubuntu server 52.91.109.0 with user ubuntu =====

*Build is already deployed and all config is set to access pulsar without auth

Login to above server with ppk file using putty. OR ssh/with git bash ssh -i "path_of_.pem_file/shan_jms_pulsar.pem" ubuntu@ec2-52-91-109-0.compute-1.amazonaws.com

cd /home/ubuntu/pulsar-jms-provider-client-1.0-SNAPSHOT

./startApp.sh

Press enter

======= Steps to setup from scratch on ubuntu server ========

#Install Java8 and Pulsar, here steps to do on ubuntu server

*Install Java8 on ubuntu if not installed

sudo apt-get update

sudo add-apt-repository ppa:webupd8team/java

sudo apt-get update

sudo apt-get install oracle-java8-installer

*See the location where java installed or how many java's installed

sudo update-alternatives --config java

Press Enter

*set JAVA_HOME environment variable to determine the Java

sudo nano /etc/environment

*Copy below path

JAVA_HOME="/usr/lib/jvm/java-8-oracle"

*Save and exit the file CLTL+X then Y and press Enter

*Reload it using below command

source /etc/environment

echo $JAVA_HOME

#Installing pulsar on Ubuntu as standalone if not installed

*Go to the directory where to store pulsar

wget https://archive.apache.org/dist/pulsar/pulsar-2.2.1/apache-pulsar-2.2.1-bin.tar.gz

tar xvfz apache-pulsar-2.2.1-bin.tar.gz

cd apache-pulsar-2.2.1

*Create a screen using below command

screen -R pulsar

Press Enter

*Run below command to start pulsar

bin/pulsar standalone

#Running pulsar-jms-provider-client using source code/IDE

Setup config file resources/application.properties

Without any auhtenctication(Change the IP where pulsar is installed) pulsar.serviceUrl=pulsar://IP_ADDRESS:6650

Save above file.

As of now file PulsarJMSClientProvider.java has main method to execute and connect to pulsar. Default topic "test" will be created with default message "this is a test."

Right click on PulsarJMSClientProvider and click on run.

We can see the console for the send and receive message

#Creating pulsar-jms-provider-client execution build using IDE and Running on ubuntu

Create a build using IDE, clean,compile and install

Copy *bin.zip from project target folder and paste to ubuntu server where pulsar installed.

Unzip *bin.zip

Go to unzipped folder and copy pulsar-jms-provider-client-1.0-SNAPSHOT.jar from lib

Modify below file for the app home vi startApp.sh

APP_HOME=/home/pulsar-jms-provider-client-1.0-SNAPSHOT

Save and exit 8.Change file permission chmod 777 startApp.sh

Setup config file resources/application.properties

Without any auhtenctication(Change the IP where pulsar is installed) pulsar.serviceUrl=pulsar://IP_ADDRESS:6650

Save above file. 10.Run ./startApp.sh

Default topic "test" will be created with default message "this is a test."

We can see the console/log file for the send and receive message
