package com.gyt.mailservice.kafka.consumer;


import com.gyt.corepackage.events.mail.InvitationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsumerInvitation {
    @KafkaListener(topics = "invitation-event", groupId = "mail-service-group")
    public void consumeInvitationEvent(InvitationEvent invitationEvent) {
        log.info(String.format("Received invitation event: %s", invitationEvent));
        //Buraya mail gönderme servisinden method gelecek. Şimdilik bilgi şeklinde bırakıldı
        log.info("Sending email to: " + invitationEvent.getEmails());
        log.info("Email subject: " + invitationEvent.getSubject());
        log.info("Email content: " + invitationEvent.getContent());
        log.info("URL: " + invitationEvent.getUrl());
    }
}
