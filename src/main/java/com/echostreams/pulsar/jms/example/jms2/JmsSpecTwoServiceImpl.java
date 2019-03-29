package com.echostreams.pulsar.jms.example.jms2;

import com.echostreams.pulsar.jms.client.PulsarConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class JmsSpecTwoServiceImpl implements JmsSpecTwoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JmsSpecTwoServiceImpl.class);

    private ConnectionFactory cfactory = new PulsarConnectionFactory();
    private JMSContext jmsContext;
    private Destination ctopic;

    @Override
    public void jms2ProduceAndConsumeTextTest() throws JMSException {
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
