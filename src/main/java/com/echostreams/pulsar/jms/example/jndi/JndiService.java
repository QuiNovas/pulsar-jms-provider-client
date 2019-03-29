package com.echostreams.pulsar.jms.example.jndi;

import javax.jms.JMSException;
import javax.naming.NamingException;

public interface JndiService {

    void produceAndConsumeTextUsingJndiConfigFile() throws JMSException, NamingException;

    void produceAndConsumeTextUsingEnvironment() throws JMSException, NamingException;

    void produceAndConsumeTextUsingSystemProp() throws JMSException, NamingException;
}
