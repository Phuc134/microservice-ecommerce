package org.example.notificationservice.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.network.Send;
import org.apache.kafka.common.protocol.types.Field;
import org.example.notificationservice.dto.request.EmailRequest;
import org.example.notificationservice.dto.request.Recipient;
import org.example.notificationservice.dto.request.SendEmailRequest;
import org.example.notificationservice.dto.request.Sender;
import org.example.notificationservice.httpClient.EmailClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    EmailClient emailClient;
    String apiKey = "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";

    public String sendEmail(SendEmailRequest request) {
        Sender sender = Sender.builder()
                .name("phuc")
                .email("lephuc25101@gmail.com")
                .build();
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(sender)
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e){
            throw new RuntimeException("Failed to send email");
        }
    }
    @KafkaListener(topics = "SEND_EMAIL")
    public void handleListenSendEmail(Map<String, Object> event) {
        Map<String, Object> toData = (Map<String, Object>) event.get("to");
        String email = (String) toData.get("email");
        String name = (String) toData.get("name");
        String subject = (String) event.get("subject");
        String htmlContent = (String) event.get("htmlContent");

        // Create SendEmailRequest object
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .to(Recipient.builder().email(email).name(name).build())
                .subject(subject)
                .htmlContent(htmlContent)
                .build();
        sendEmail(sendEmailRequest);
    }

}
