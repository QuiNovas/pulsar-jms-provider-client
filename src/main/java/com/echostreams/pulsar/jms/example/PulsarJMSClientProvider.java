package com.echostreams.pulsar.jms.example;

import com.echostreams.pulsar.jms.config.PulsarConfig;
import com.echostreams.pulsar.jms.example.jms1.JmsSpecOneService;
import com.echostreams.pulsar.jms.example.jms1.JmsSpecOneServiceImpl;
import com.echostreams.pulsar.jms.example.jms2.JmsSpecTwoService;
import com.echostreams.pulsar.jms.example.jms2.JmsSpecTwoServiceImpl;
import com.echostreams.pulsar.jms.example.jndi.JndiService;
import com.echostreams.pulsar.jms.example.jndi.JndiServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;

public class PulsarJMSClientProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(PulsarJMSClientProvider.class);

    private static String SERVICE_URL = "pulsar://35.174.9.240:6650";

    public static void main(String[] args) throws JMSException, IOException, NamingException {
        // Reading config file property from resources/application.properties and assigning to variable
        PulsarConfig.initializeConfig("/application.properties");

        PulsarJMSClientProvider pulsarJMSClientProvider = new PulsarJMSClientProvider();

        // TODO Once JNDI code is merged uncomment below code and test for JNDI
/*        // JNDI
        pulsarJMSClientProvider.executeJNDIExamples();*/

        //JMS1
        pulsarJMSClientProvider.executeJMS1Examples();

        //JMS2
        pulsarJMSClientProvider.executeJMS2Examples();

    }

    /*
    * JNDI examples
     */
    private void executeJNDIExamples() throws NamingException, JMSException {
        JndiService jndiService = new JndiServiceImpl(SERVICE_URL);

        jndiService.produceAndConsumeTextUsingJndiConfigFile();
        jndiService.produceAndConsumeTextUsingEnvironment();
        jndiService.produceAndConsumeTextUsingSystemProp();
    }

    /*
   * JMS1 examples
    */
    private void executeJMS1Examples() throws JMSException {
        JmsSpecOneService jmsSpecOneService = new JmsSpecOneServiceImpl();
        jmsSpecOneService.produceAndConsumeTextTest();
        jmsSpecOneService.produceAndConsumeBytesTest();
        jmsSpecOneService.produceAndConsumeObjectTest();
        jmsSpecOneService.produceAndConsumeMapTest();
        jmsSpecOneService.produceAndConsumeStreamTest();

        // Queue
        jmsSpecOneService.qSendAndReceiveTextTest();

        //Topic
        jmsSpecOneService.topicPublishAndSubscribeTextTest();
    }

    /*
   * JMS2 examples
    */
    private void executeJMS2Examples() throws JMSException {
        JmsSpecTwoService jmsSpecTwoService = new JmsSpecTwoServiceImpl();
        jmsSpecTwoService.jms2ProduceAndConsumeTextTest();
    }

}
