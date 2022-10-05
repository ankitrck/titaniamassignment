package com.koshti.titaniam.queue;

import com.koshti.titaniam.models.Entities;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

public class Converter implements MessageConverter {

    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        Entities entity = (Entities) object;
        MapMessage message = session.createMapMessage();
        message.setString("name", entity.getName());
        message.setInt("id", entity.getId());
        return message;
    }

    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        MapMessage mapMessage = (MapMessage) message;
        return new Entities(mapMessage.getInt("id"),mapMessage.getString("name"));
    }

}