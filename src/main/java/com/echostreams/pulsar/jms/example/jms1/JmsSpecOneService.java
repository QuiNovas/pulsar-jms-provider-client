package com.echostreams.pulsar.jms.example.jms1;

import javax.jms.JMSException;

public interface JmsSpecOneService {

    void produceAndConsumeTextTest() throws JMSException;

    void produceAndConsumeBytesTest() throws JMSException;

    void produceAndConsumeObjectTest() throws JMSException;

    void produceAndConsumeMapTest() throws JMSException;

    void produceAndConsumeStreamTest() throws JMSException;

    void qSendAndReceiveTextTest() throws JMSException;

    void topicPublishAndSubscribeTextTest() throws JMSException;
}
