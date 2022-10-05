package com.koshti.titaniam.controller;

import com.koshti.titaniam.IService;
import com.koshti.titaniam.TitaniamApplication;
import com.koshti.titaniam.models.Entities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import java.util.List;
@RestController
public class RController {

    private Queue queue;

    private JmsTemplate jmsTemplate;
    @Autowired
    IService service;
    @GetMapping("/all")
    public List<Entities> getall(){
        return service.getAll();
    }

    @GetMapping("/syncEngines")
    public String syncall(){
        String response;
        if (service.syncall() == 1)
            response = "Intial SYNC success";
        else
            response = "An Error occurred";
        return response;
    }

    @GetMapping("/commit")
    public void commitData()
    {
        jmsTemplate = TitaniamApplication.context.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend("dblayerbox", new Entities(1, "Hello Kenobi"));
    }

    @GetMapping(path = {"/entity", "/entity/{id}"})
    public ResponseEntity getOne(@PathVariable(required=false,name="id") int id)
    {
        jmsTemplate = TitaniamApplication.context.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend("dblayerboxget", id);

        return ResponseEntity.status(HttpStatus.OK).body("Operation successful");
    }

    @GetMapping(path = {"/entityemp", "/entityemp/{name}"})
    public ResponseEntity addOne(@PathVariable(required=false,name="name") String name)
    {
        jmsTemplate = TitaniamApplication.context.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend("dblayerboxpost", name);

        return ResponseEntity.status(HttpStatus.OK).body("Operation successful");
    }
}
