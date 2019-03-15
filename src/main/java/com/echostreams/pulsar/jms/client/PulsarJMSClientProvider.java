package com.echostreams.pulsar.jms.client;

import com.echostreams.pulsar.jms.config.PulsarConfig;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.ConsumerBuilderImpl;
import org.apache.pulsar.client.impl.ProducerBuilderImpl;
import org.apache.pulsar.client.impl.PulsarClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;

public class PulsarJMSClientProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarJMSClientProvider.class);

    private String serviceUrl = "pulsar://10.0.53.193:6650";
    private ConnectionFactory factory = new PulsarConnectionFactory();
    private Connection con;
    private Session session;
    private Destination topic;

    // Queue Test
    private QueueConnectionFactory qfactory = new PulsarConnectionFactory();
    private QueueConnection qcon;
    private QueueSession qsession;
    private Queue queue;

    // Topic Test
    private TopicConnectionFactory tfactory = new PulsarConnectionFactory();
    private TopicConnection tcon;
    private TopicSession tsession;
    private Topic tp;

    // Jms 2.0
    private ConnectionFactory cfactory = new PulsarConnectionFactory();
    private JMSContext jmsContext;
    private Destination ctopic;

    public static void main(String[] args) throws JMSException, IOException {
        // Reading config file property from resources/application.properties and assigning to variable
        PulsarConfig.initializeConfig("/application.properties");

        PulsarJMSClientProvider pulsarJMSClientProvider = new PulsarJMSClientProvider();
        pulsarJMSClientProvider.produceAndConsumeTextTest();
        pulsarJMSClientProvider.produceAndConsumeBytesTest();
        pulsarJMSClientProvider.produceAndConsumeObjectTest();
        pulsarJMSClientProvider.produceAndConsumeMapTest();
        pulsarJMSClientProvider.produceAndConsumeStreamTest();

        // Queue
        pulsarJMSClientProvider.qSendAndReceiveTextTest();

        //Topic
        pulsarJMSClientProvider.topicPublishAndSubscribeTextTest();

        //JMS2-JMSContext
        pulsarJMSClientProvider.jms2ProduceAndConsumeTextTest();
    }

    /*
    * TextMessage
    */
    private void produceAndConsumeTextTest() throws JMSException {
        // Change default Pulsar client config value to custom value
        ClientBuilder clientBuilder = PulsarClient.builder();
        clientBuilder.serviceUrl(PulsarConfig.SERVICE_URL);
        PulsarConfig.changeDefaultClientConfig(clientBuilder);

        con = factory.createConnection();
        session = con.createSession();
        topic = session.createTopic("test");

        // Changing the default value of Producer config
        PulsarConnection pulsarConnection = (PulsarConnection) con;
        ProducerBuilderImpl producerBuilderImpl = new ProducerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        producerBuilderImpl.producerName("test-producer").compressionType(CompressionType.LZ4);
        PulsarConfig.changeDefaultProducerConfig(producerBuilderImpl);

        MessageProducer producer = session.createProducer(topic);

        TextMessage text = session.createTextMessage();
        text.setText("this is a textmsg test.");

        producer.send(text);

        // Changing the default value of Consumer config
        ConsumerBuilderImpl consumerBuilder = new ConsumerBuilderImpl((PulsarClientImpl) pulsarConnection.getClient(), Schema.BYTES);
        consumerBuilder.consumerName("test-producer")
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionName("test-subcription");
        PulsarConfig.changeDefaultConsumerConfig(consumerBuilder);

        MessageConsumer consumer = session.createConsumer(topic);
        TextMessage textMessage = (TextMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

        con.close();
        PulsarConfig.clientConfig = null;
        PulsarConfig.producerConfig = null;
        PulsarConfig.consumerConfig = null;
    }

    /*
    * BytesMessage
    */
    private void produceAndConsumeBytesTest() throws JMSException {
        con = factory.createConnection();
        session = con.createSession();
        topic = session.createTopic("test");

        MessageProducer producer = session.createProducer(topic);

        BytesMessage text = session.createBytesMessage();
        text.writeChar('a');
        text.writeBoolean(true);
        //text.writeBytes("this is a Byte test.".getBytes());

        producer.send(text);

        MessageConsumer consumer = session.createConsumer(topic);
        BytesMessage bytesMessage = (BytesMessage) consumer.receive();
        int TEXT_LENGTH = new Long(bytesMessage.getBodyLength()).intValue();
        byte[] textBytes = new byte[TEXT_LENGTH];
        bytesMessage.readBytes(textBytes, TEXT_LENGTH);
        String textString = new String(textBytes);

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textString, bytesMessage.getJMSMessageID());

        con.close();
    }

    /*
    * ObjectMessage
    */
    private void produceAndConsumeObjectTest() throws JMSException {
        con = factory.createConnection();
        session = con.createSession();
        topic = session.createTopic("test");

        MessageProducer producer = session.createProducer(topic);

        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(new String("This is a Object Test"));

        producer.send(objectMessage);

        MessageConsumer consumer = session.createConsumer(topic);
        ObjectMessage omi = (ObjectMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", (String) omi.getObject(), omi.getJMSMessageID());

        con.close();
    }

    /*
    * MapMessage
     */
    private void produceAndConsumeMapTest() throws JMSException {
        con = factory.createConnection();
        session = con.createSession();
        topic = session.createTopic("test");

        MessageProducer producer = session.createProducer(topic);

        MapMessage mmo = session.createMapMessage();
        mmo.setString("First", "256");
        mmo.setInt("Second", 512);
        producer.send(mmo);

        MessageConsumer consumer = session.createConsumer(topic);
        MapMessage mmi = (MapMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message=Map: Second as String '{}' First as double '{}' with msg-id={}", mmi.getString("Second"), mmi.getDouble("First"), mmi.getJMSMessageID());

        con.close();
    }

    /*
    * StreamMessage
     */
    private void produceAndConsumeStreamTest() throws JMSException {
        con = factory.createConnection();
        session = con.createSession();
        topic = session.createTopic("test");

        MessageProducer producer = session.createProducer(topic);

        StreamMessage smo = session.createStreamMessage();
        smo.writeString("256");
        smo.writeInt(512);
        producer.send(smo);

        MessageConsumer consumer = session.createConsumer(topic);
        StreamMessage smi = (StreamMessage) consumer.receive();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message=Stream: Second as String '{}' First as float '{}' with msg-id={}", smi.readFloat(), smi.readString(), smi.getJMSMessageID());

        con.close();
    }

    /*
* TextMessage
*/
    private void qSendAndReceiveTextTest() throws JMSException {
        qcon = qfactory.createQueueConnection();
        qsession = qcon.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
        queue = qsession.createQueue("test");

        QueueSender qsend = qsession.createSender(queue);

        TextMessage text = session.createTextMessage();
        text.setText("Hello Text Queue using QueueConnection Sender Receiver");

        qsend.send(text);

        QueueReceiver qr = qsession.createReceiver(queue);
        TextMessage textMessage = (TextMessage) qr.receiveNoWait();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

        qcon.close();
    }

    /*
    * TextMessage
    */
    private void topicPublishAndSubscribeTextTest() throws JMSException {
        tcon = tfactory.createTopicConnection();
        tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        tp = tsession.createTopic("test");

        TopicPublisher pub = tsession.createPublisher(tp);
        TextMessage text = tsession.createTextMessage("Hello Text Topic using TopicConnection Publisher Subscriber");

        pub.send(text);

        TopicSubscriber tsub = tsession.createSubscriber(tp);
        TextMessage textMessage = (TextMessage) tsub.receiveNoWait();

        // Extract the message as a printable string and then log
        LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

        tcon.close();
    }

    private void jms2ProduceAndConsumeTextTest() throws JMSException {
        try {
            jmsContext = cfactory.createContext();
            ctopic = jmsContext.createTopic("test");

            jmsContext.createProducer().send(ctopic, "Hello  Text JMS 2 using JMSContext JMSProducer JMSConsumer");

            JMSConsumer consumer = jmsContext.createConsumer(ctopic);
            TextMessage textMessage = (TextMessage) consumer.receive();
            // Extract the message as a printable string and then log
            LOGGER.info("Received message='{}' with msg-id={}", textMessage.getText(), textMessage.getJMSMessageID());

            jmsContext.close();
        } catch (JMSRuntimeException e) {
            LOGGER.error("JMSRuntimeException", e);
        }
    }
}
