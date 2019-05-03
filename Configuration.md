# ======== Steps to Configure Without Any Auth, Token, JNDI, TLS, Athenz

## ================= Config resources/application.properties ================= ##

## No Authentication
    Go to resources/application.properties and set the following properites :-

        #Leave empty-no auth, TLS-tls auth, ATHENZ-athenz auth, TOKEN-token based auth
        #pulsar.enabledAuth =TLS OR pulsar.enabledAuth =ATHENZ OR pulsar.enabledAuth =TOKEN OR pulsar.enabledAuth =
        pulsar.enabledAuth =

        #use "pulsar+ssl://" in serviceUrl to enable TLS
        pulsar.serviceUrl=pulsar://IP_ADDRESS:6650

## Configuring Token

1. # Setup jwt security token for pulsar broker
    Follow below link to setup token where pulsar is installed. This help you to create token and apply the same
    into conf/broker.conf. Then restart the pulsar

        https://pulsar.apache.org/docs/en/security-token-admin/

2. # Setup jwt security token for pulsar client(here is example for our java app : pulsar-jms-provider-examples)
    Go to resources/application.properties and set the following properites :-

        #Leave empty-no auth, TLS-tls auth, ATHENZ-athenz auth, TOKEN-token based auth
        #pulsar.enabledAuth =TLS OR pulsar.enabledAuth =ATHENZ OR pulsar.enabledAuth =TOKEN OR pulsar.enabledAuth =
        pulsar.enabledAuth =TOKEN

        #use "pulsar+ssl://" in serviceUrl to enable TLS
        pulsar.serviceUrl=pulsar://IP_ADDRESS:6650

        #direct token - token:encoded token value
        #Eg: token:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGllbnQifQ.cVl6bkcDvkOEO-ogm9u0o6q95l-XVNyDaVO9Uvjdstc
        #from file - /path/to/token/file
        #Eg: G:\\token_auth\\token_file
        pulsar.authParams=G:\\token_auth\\token_file

3. # To setup token for pulsar client and run from CLI
   Go to pulsar installation directory and follow below link to setup

        https://pulsar.apache.org/docs/en/security-token-client/

## Configuring TLS

    TODO

## Configuring Athenz

    TODO


## Configuring a JNDI InitialContext

Applications use a JNDI InitialContext, which obtained from an InitialContextFactory, to look up JMS objects
such as ConnectionFactory. The pulsar-jms-provider  provides an implementation of the InitialContextFactory
in class *com.echostreams.pulsar.jms.jndi.PulsarInitialContextFactory*.

This may be configured and used in three main ways:

1.  # Via jndi.properties file on the Java Classpath.

    By including a file named jndi.properties on the Classpath and set Pulsar InitialContextFactory value as below.

        java.naming.factory.initial = com.echostreams.pulsar.jms.jndi.PulsarInitialContextFactory

    The other properties or environment Hashtable is as follows:

    + Format to define a ConnectionFactory, Queue, and Topic

            connectionfactory.lookupName = URI
            queue.lookupName = queueName
            topic.lookupName = topicName

    + Example to define a ConnectionFactory, Queue, and Topic

            connectionfactory.pulsarConFactoryLookup = pulsar://35.174.9.240:6650
            queue.pulsarQueueLookup = queueTest
            topic.pulsarTopicLookup = topicTest

    When instantiating InitialContext object , InitialContextFactory implementation will be
    discovered and all the values of jndi.properties will be added to context for lookup

        javax.naming.Context ctx = new javax.naming.InitialContext();

2.  Via system properties.

        System.setProperty("java.naming.factory.initial", "com.echostreams.pulsar.jms.jndi.PulsarInitialContextFactory");
                                                    OR
        System.setProperty("java.naming.factory.initial", PulsarConstants.JNDI_CONTEXT_FACTORY);
        System.setProperty("connectionfactory.pulsarConFactoryLookup", "pulsar://35.174.9.240:6650");
        System.setProperty("queue.pulsarQueueLookup", "queueTest");
        System.setProperty("topic.pulsarTopicLookup", "topicTest");

    When instantiating InitialContext object , InitialContextFactory implementation will be
    discovered and the properties set above will be added to context for lookup

         javax.naming.Context ctx = new javax.naming.InitialContext();

3.  Programmatically using an environment Hashtable.

    The InitialContext may also be configured directly by passing an environment during creation:

        Hashtable<Object, Object> env = new Hashtable<Object, Object>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, PulsarConstants.JNDI_CONTEXT_FACTORY);
        javax.naming.Context context = new javax.naming.InitialContext(env);

    When instantiating InitialContext object , InitialContextFactory implementation will be
        discovered and an environment set above will be added to context for lookup

             javax.naming.Context ctx = new javax.naming.InitialContext();

    Below are the sample example to create the connection :-

            Context context = new InitialContext();

            ConnectionFactory factory = (ConnectionFactory) context.lookup("pulsarConFactoryLookup");
            Destination pulsarTopic = (Destination) context.lookup("pulsarTopicLookup");

            Connection con = factory.createConnection();




