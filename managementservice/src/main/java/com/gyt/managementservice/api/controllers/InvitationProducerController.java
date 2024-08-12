package com.gyt.managementservice.api.controllers;

import com.gyt.corepackage.events.mail.InvitationEvent;

import com.gyt.managementservice.business.abstracts.InvitationProducerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invitation")
@AllArgsConstructor
public class InvitationProducerController {

    private final InvitationProducerService invitationProducerService;

    @PostMapping("/send")
    public String sendInvitation(@RequestBody InvitationEvent invitationEvent) {
        invitationProducerService.sendInvitation(invitationEvent);
        return "Invitation event sent successfully!";
    }

}
