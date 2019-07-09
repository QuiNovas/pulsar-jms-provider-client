## pulsar-jms-provider-examples

Embedding pulsar-jms-provider as maven dependency in this project.

Examples in this app : Publish messages to pulsar broker and consume messages from pulsar broker

## Build from sources

 After a git clone of this repository, cd into the cloned sources and make sure you have maven installed to build this:

 `mvn install`.

 In project_source_folder/target directory will be produced the selfcontained file with all dependencies and a running script.

   `OR`

 Can also build using IDE

## Setup pulsar broker and App on ubuntu server

 * Install Java8

    Check if  Java8 installed

     `java -version`

     Install Java8

      ```
      sudo apt-get update

      sudo add-apt-repository ppa:webupd8team/java

      sudo apt-get update

      sudo apt-get install oracle-java8-installer

      ```

    Check/Change the location where java installed or how many java's installed

      `sudo update-alternatives --config java`

    Set JAVA_HOME environment variable to determine the Java

      `sudo nano /etc/environment`

    Copy below path

      `JAVA_HOME="/usr/lib/jvm/java-8-oracle"`

    Save and exit the file CLTL+X then Y and press Enter

    Reload it using below command

      `source /etc/environment`

    Verify the path

      `echo $JAVA_HOME`

* Install Pulsar

    Go to the directory where to store pulsar

     ```
      wget https://archive.apache.org/dist/pulsar/pulsar-2.2.1/apache-pulsar-2.2.1-bin.tar.gz

      tar xvfz apache-pulsar-2.2.1-bin.tar.gz

      cd apache-pulsar-2.2.1


     ```
    Create a screen using below command

     `screen -R pulsar`

    Press Enter

    Run below command to start pulsar

     `bin/pulsar standalone`

    Come out from screen

     `CTRL+A then D`

 * Deploy the executable build

    Copy pulsar-jms-provider-examples-XXX-bin.zip from project target folder and paste to ubuntu server

      ```
        unzip pulsar-jms-provider-examples-XXX
        cd pulsar-jms-provider-examples-XXX
      ```

 * Config application.properties

      `Refer Configuration.md file in project pulsar-jms-provider-examples`

 * Run the app on ubuntu

   Go to pulsar-jms-provider-examples-XXX if not

      ```
        chmod 777 startApp.sh
        ./startApp.sh

      ```

 ---