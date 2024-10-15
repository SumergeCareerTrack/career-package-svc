package com.sumerge.careertrack.career_package_svc.services;


import com.sumerge.careertrack.career_package_svc.entities.requests.NotificationRequestDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class ProducerService {
    @Value("${kafka.topic.name}")
    private String topicName;
    private final KafkaTemplate<String,String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(NotificationRequestDTO dto)  {
        System.out.println("Before SENT: " + dto);
        JSONObject obj= new JSONObject(dto);
        System.out.println("SENT: " + obj);
        kafkaTemplate.send(topicName, obj.toString());

    }



}