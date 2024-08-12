package com.gyt.managementservice.business.concretes;

import com.gyt.corepackage.events.mail.InvitationEvent;
import com.gyt.managementservice.business.abstracts.InvitationProducerService;
import com.gyt.managementservice.kafka.producer.InvitationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationProducerManager implements InvitationProducerService {
    private final InvitationProducer invitationProducer;

    @Override
    public void sendInvitation(InvitationEvent invitationEvent) {
        invitationProducer.sendInvitationEvent(invitationEvent);
    }
}
