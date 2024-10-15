package com.sumerge.careertrack.career_package_svc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sumerge.careertrack.career_package_svc.entities.requests.NotificationRequestDTO;
import com.sumerge.careertrack.career_package_svc.services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//TODO REMOVE THIS IT WAS FOR TESTING PURPOSES

@RestController
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @PostMapping("/send")
    public void sendMessage(@RequestBody NotificationRequestDTO dto) throws JsonProcessingException {
        producerService.sendMessage(dto);
        System.out.println("SENT: " + dto);
    }

}
