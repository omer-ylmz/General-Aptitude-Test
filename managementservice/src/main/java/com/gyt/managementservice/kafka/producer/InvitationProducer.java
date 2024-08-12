package com.gyt.managementservice.kafka.producer;

import com.gyt.corepackage.events.mail.InvitationEvent;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvitationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvitationProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInvitationEvent(InvitationEvent event) {
        LOGGER.info(String.format("Invitation event is being sent => %s", event));

        Message<InvitationEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "invitation-event")
                .build();

        kafkaTemplate.send(message);
    }

}
