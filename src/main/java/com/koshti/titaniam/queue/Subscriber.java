package com.koshti.titaniam.queue;

import com.koshti.titaniam.PrimaryCluster.PrimaryDbRepository;
import com.koshti.titaniam.SecondaryCluster.SecondaryDbRepository;
import com.koshti.titaniam.IService;
import com.koshti.titaniam.models.Entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Map;

@Component
public class Subscriber implements MessageListener {

    @Autowired
    private Environment env;
    private Entities ent = null;
    @Autowired
    private PrimaryDbRepository config1;
    @Autowired
    private SecondaryDbRepository config2;
    @Autowired
    IService service;
    public JmsTemplate getJmsTemplate() {
        return getJmsTemplate();
    }

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String msg = ((TextMessage) message).getText();
                System.out.println("Message has been consumed : " + msg);
            } catch (JMSException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException("Message Error");
        }
    }

    public Entities receiveMessage() throws JMSException {
        Map map = (Map) getJmsTemplate().receiveAndConvert();
        return new Entities((Integer) map.get("id"), (String) map.get("name"));
    }


    @JmsListener(destination = "dblayerboxget")
    public void GetJmsListenerMethod(TextMessage message) throws JMSException {
        boolean ifPrimary = true;
        System.out.println("JMS listener received text message: " + message.getText());

        try {
            // ALways read from primary
            System.out.println("Reading from PRIMARY Cluster");
            ent = config1.getOne(Integer.parseInt(message.getText()));
        }
        catch (org.hibernate.exception.GenericJDBCException ex)
        {
            System.out.println("Error");
            ifPrimary = false;
        }
        catch (Exception e)
        {
            ifPrimary = false;
            System.out.println("Exception Error");
        }

        try
        {
            //if primary not reachble, try from secondary
            if (!ifPrimary) {
                System.out.println("PRIMARY not available. Reading from SECONDARY Cluster");
                ent = config2.getOne(Integer.parseInt(message.getText()));
            }
        }
        catch (org.hibernate.exception.GenericJDBCException ex)
        {
            System.out.println("Error");
            ifPrimary = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(ent!=null)
            System.out.println("Output: " + ent.getId() + " - " + ent.getName());
        else
            System.out.println("Entity is NULL");
    }

    @JmsListener(destination = "dblayerboxpost")
    public void PostJmsListenerMethod(TextMessage message) throws JMSException {

        String ZoneRedundancy = env.getProperty("ZoneRedundancy");
        boolean allSaved = true;
        boolean primarySaved = true;

        System.out.println("JMS listener received text message: " + message.getText());

        try {
            // ALways read from primary
            System.out.println("Saving to  PRIMARY Cluster");
            config1.addOne(message.getText().toString());
        }
        catch (org.hibernate.exception.GenericJDBCException ex)
        {
            allSaved=false;
            primarySaved=false;
            System.out.println("Error");
        }
        catch (Exception e)
        {
            allSaved=false;
            primarySaved=false;
            e.printStackTrace();
        }

        if(ZoneRedundancy.equalsIgnoreCase("true") && primarySaved)
        {
            try {
                // ALways read from primary
                System.out.println("Saving to  SECONDARY Cluster");
                config2.addOne(message.getText().toString());
            }
            catch (org.hibernate.exception.GenericJDBCException ex)
            {
                allSaved=false;
                System.out.println("Error");
            }
            catch (Exception e)
            {
                allSaved= false;
                e.printStackTrace();
            }
        }

        if(allSaved && primarySaved)
            System.out.println("Output: Data saved to all clusters");
        else if(!primarySaved)
            System.out.println("Output: SAVING to Primary failed. Opertaion halted.");
        else
            System.out.println("Output: An error occured in saving to all Clusters.DB SYNC Engine will keep trying");

    }
}
