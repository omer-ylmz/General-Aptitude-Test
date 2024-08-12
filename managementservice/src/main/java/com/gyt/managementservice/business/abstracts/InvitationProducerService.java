package com.gyt.managementservice.business.abstracts;

import com.gyt.corepackage.events.mail.InvitationEvent;

public interface InvitationProducerService {

    void sendInvitation(InvitationEvent invitationEvent);
}
