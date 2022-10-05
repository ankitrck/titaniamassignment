package com.koshti.titaniam.queue;

import com.koshti.titaniam.models.Entities;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Map;

public class Listener implements MessageListener {

    private JmsTemplate jmsTemplate;
    private Queue queue;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String msg = ((TextMessage) message).getText();
                System.out.println("Received message: " + msg);
                if (msg == null) {
                    throw new IllegalArgumentException("Null value received...");
                }
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Entities receiveMessage() throws JMSException {
        Map map = (Map) this.jmsTemplate.receiveAndConvert();
        return new Entities((Integer) map.get("id"), (String) map.get("name"));
    }
}
