package com.gyt.managementservice.business.concretes;

import com.gyt.corepackage.events.mail.InvitationEvent;
import com.gyt.managementservice.business.abstracts.InvitationProducerService;
import com.gyt.managementservice.kafka.producer.InvitationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationProducerManager implements InvitationProducerService {
    private final InvitationProducer invitationProducer;

    @Override
    public void sendInvitation(InvitationEvent invitationEvent) {
        log.debug("Attempting to send invitation event: {}", invitationEvent);

        try {
            invitationProducer.sendInvitationEvent(invitationEvent);
            log.info("Successfully sent invitation event: {}", invitationEvent);
        } catch (Exception e) {
            log.error("Error sending invitation event: {}", invitationEvent, e);
            throw e;
        }
    }
}
