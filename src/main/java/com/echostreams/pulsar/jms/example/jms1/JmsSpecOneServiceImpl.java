package com.echostreams.pulsar.jms.example.jms1;

import com.echostreams.pulsar.jms.client.PulsarConnection;
import com.echostreams.pulsar.jms.client.PulsarConnectionFactory;
import com.echostreams.pulsar.jms.config.PulsarConfig;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.ConsumerBuilderImpl;
import org.apache.pulsar.client.impl.ProducerBuilderImpl;
import org.apache.pulsar.client.impl.PulsarClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JmsSpecOneServiceImpl implements JmsSpecOneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsSpecOneServiceImpl.class);

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

    /*
   * TextMessage
   */
    public void produceAndConsumeTextTest() throws JMSException {
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
        consumerBuilder.consumerName("test-consumer")
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
    public void produceAndConsumeBytesTest() throws JMSException {
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
    public void produceAndConsumeObjectTest() throws JMSException {
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
    public void produceAndConsumeMapTest() throws JMSException {
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
    public void produceAndConsumeStreamTest() throws JMSException {
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
    public void qSendAndReceiveTextTest() throws JMSException {
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
    public void topicPublishAndSubscribeTextTest() throws JMSException {
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
}
