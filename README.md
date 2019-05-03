# pulsar-jms-provider-examples : This app is used to send and receiver message from pulsar using pulsar-jms-provider api

Steps to setup java app to send and receiver messages to pulsar through pulsar-jms-provider api

# 1. ======= Create a executable build =====

Clone the repo

Make sure you have maven installed

Go to project directory and Run the maven build:

    mvn install

It will produce a JAR file and pulsar-jms-provider-examples-XXX-bin.zip in the target directory, and run all tests with the most recently-released build of Clojure.

OR
Can also build using IDE


# 2. ======= Steps to setup pulsar on ubuntu server ========

# Install Java8 and Pulsar, here steps to do on ubuntu server

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

# Installing pulsar on Ubuntu as standalone if not installed

*Go to the directory where to store pulsar

wget https://archive.apache.org/dist/pulsar/pulsar-2.2.1/apache-pulsar-2.2.1-bin.tar.gz

tar xvfz apache-pulsar-2.2.1-bin.tar.gz

cd apache-pulsar-2.2.1

*Create a screen using below command

screen -R pulsar

Press Enter

*Run below command to start pulsar

bin/pulsar standalone

# 3. ======= Steps to setup application =======

# Deploy the executable build

Copy pulsar-jms-provider-examples-XXX-bin.zip from project target folder and paste to ubuntu server where pulsar is installed.

Unzip *bin.zip

Go to unzipped folder and copy pulsar-jms-provider-examples-XXX.jar from lib to pulsar-jms-provider-examples-XXX

    cp ./lib/pulsar-jms-provider-examples-XXX.jar .

  press Enter

# Config application.properties by referring Configuration.md file in project pulsar-jms-provider-examples

# Run the app on ubuntu

Go to pulsar-jms-provider-examples-XXX if not

Modify below file for the app home and java opts

vi startApp.sh

    APP_HOME=/home/pulsar-jms-provider-examples-XXX

Save and exit

Change file permission

    chmod 777 startApp.sh
Run

    ./startApp.sh