package com.echostreams.pulsar.jms.example.jndi;

import com.echostreams.pulsar.jms.client.PulsarConnection;
import com.echostreams.pulsar.jms.config.PulsarConfig;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.ConsumerBuilderImpl;
import org.apache.pulsar.client.impl.ProducerBuilderImpl;
import org.apache.pulsar.client.impl.PulsarClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class JndiServiceImpl implements JndiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JndiServiceImpl.class);

    private String serviceUrl;

    public JndiServiceImpl() {
    }

    public JndiServiceImpl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /*
    * Sending TextMessage using JNDI lookup with config jndi.properties
    */
    @Override
    public void produceAndConsumeTextUsingJndiConfigFile() throws JMSException, NamingException {
        LOGGER.info("Sending TextMessage using JNDI lookup with config jndi.properties");

        Session session;
        // Change default Pulsar client config value to custom value
        ClientBuilder clientBuilder = PulsarClient.builder();
        clientBuilder.serviceUrl(PulsarConfig.SERVICE_URL);
        PulsarConfig.changeDefaultClientConfig(clientBuilder);

        // The configuration for the Pulsar InitialContextFactory has been supplied in
        // a jndi.properties file(under resources folder) in the classpath, which results in it being picked
        // up automatically by the InitialContext constructor like below.
        Context context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("pulsarConFactoryLookup");
        Destination pulsarTopic = (Destination) context.lookup("pulsarTopicLookup");

        Connection con = factory.createConnection();

        session = con.createSession();
        // Changing the default value of Producer config
        PulsarConnection pulsarConnection = (PulsarConnection) con;
        ProducerBuilderImpl producerBuilderImpl = new ProducerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        producerBuilderImpl.producerName("jndi-test-producer").compressionType(CompressionType.LZ4);
        PulsarConfig.changeDefaultProducerConfig(producerBuilderImpl);

        MessageProducer producer = session.createProducer(pulsarTopic);

        TextMessage text = session.createTextMessage();
        text.setText("this is a textmsg test using jndi.");

        producer.send(text);

        // Changing the default value of Consumer config
        ConsumerBuilderImpl consumerBuilder = new ConsumerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        consumerBuilder.consumerName("jndi-test-consumer")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionName("jndi-test-subcription");
        PulsarConfig.changeDefaultConsumerConfig(consumerBuilder);

        MessageConsumer consumer = session.createConsumer(pulsarTopic);
        TextMessage textMessage = (TextMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

        con.close();
        PulsarConfig.clientConfig = null;
        PulsarConfig.producerConfig = null;
        PulsarConfig.consumerConfig = null;
    }

    /*
    * Sending TextMessage using JNDI lookup by passing environment hashtable explicitly
    */
    @Override
    public void produceAndConsumeTextUsingEnvironment() throws JMSException, NamingException {
        LOGGER.info("Sending TextMessage using JNDI lookup by passing environment hashtable explicitly");

        Session session;
        // Change default Pulsar client config value to custom value
        ClientBuilder clientBuilder = PulsarClient.builder();
        clientBuilder.serviceUrl(PulsarConfig.SERVICE_URL);
        PulsarConfig.changeDefaultClientConfig(clientBuilder);


        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "com.echostreams.pulsar.jms.jndi.PulsarInitialContextFactory");
        p.put(Context.PROVIDER_URL, serviceUrl);
        Context context = new InitialContext(p);

        // lookup the queue connection factory
        QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("queue/connectionFactory");

        // lookup the queue object
        Queue queue = (Queue) context.lookup("queue/queue0");

        Connection con = factory.createConnection();
        session = con.createSession();

        // Changing the default value of Producer config
        PulsarConnection pulsarConnection = (PulsarConnection) con;
        ProducerBuilderImpl producerBuilderImpl = new ProducerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        producerBuilderImpl.producerName("jndi-test-producer").compressionType(CompressionType.LZ4);
        PulsarConfig.changeDefaultProducerConfig(producerBuilderImpl);

        MessageProducer producer = session.createProducer(queue);

        TextMessage text = session.createTextMessage();
        text.setText("this is a textmsg test using jndi.");

        producer.send(text);

        // Changing the default value of Consumer config
        ConsumerBuilderImpl consumerBuilder = new ConsumerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        consumerBuilder.consumerName("jndi-test-consumer")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionName("jndi-test-subcription");
        PulsarConfig.changeDefaultConsumerConfig(consumerBuilder);

        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage textMessage = (TextMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

        con.close();
        PulsarConfig.clientConfig = null;
        PulsarConfig.producerConfig = null;
        PulsarConfig.consumerConfig = null;
    }

    /*
    * Sending TextMessage using JNDI by setting system property
    */
    @Override
    public void produceAndConsumeTextUsingSystemProp() throws JMSException, NamingException {
        LOGGER.info("Sending TextMessage using JNDI by setting system property");

        Session session;
        // Change default Pulsar client config value to custom value
        ClientBuilder clientBuilder = PulsarClient.builder();
        clientBuilder.serviceUrl(PulsarConfig.SERVICE_URL);
        PulsarConfig.changeDefaultClientConfig(clientBuilder);


        //System.setProperty("java.naming.factory.initial", PulsarConstants.JNDI_CONTEXT_FACTORY);
        System.setProperty("connectionfactory.pulsarConFactoryLookup", serviceUrl);
        System.setProperty("queue.pulsarQueueLookup", "queueTest");

        Context context = new InitialContext();

        // lookup the queue connection factory
        QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup("pulsarConFactoryLookup");

        // lookup the queue object
        Queue queue = (Queue) context.lookup("queueTest");

        Connection con = factory.createConnection();
        session = con.createSession();

        // Changing the default value of Producer config
        PulsarConnection pulsarConnection = (PulsarConnection) con;
        ProducerBuilderImpl producerBuilderImpl = new ProducerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        producerBuilderImpl.producerName("jndi-test-producer").compressionType(CompressionType.LZ4);
        PulsarConfig.changeDefaultProducerConfig(producerBuilderImpl);

        MessageProducer producer = session.createProducer(queue);

        TextMessage text = session.createTextMessage();
        text.setText("this is a textmsg test using jndi.");

        producer.send(text);

        // Changing the default value of Consumer config
        ConsumerBuilderImpl consumerBuilder = new ConsumerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        consumerBuilder.consumerName("jndi-test-consumer")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionName("jndi-test-subcription");
        PulsarConfig.changeDefaultConsumerConfig(consumerBuilder);

        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage textMessage = (TextMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

        con.close();
        PulsarConfig.clientConfig = null;
        PulsarConfig.producerConfig = null;
        PulsarConfig.consumerConfig = null;
    }
}
