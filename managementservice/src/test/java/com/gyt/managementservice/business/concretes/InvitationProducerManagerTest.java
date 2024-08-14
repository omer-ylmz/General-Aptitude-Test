package com.gyt.managementservice.business.concretes;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gyt.corepackage.events.mail.InvitationEvent;
import com.gyt.managementservice.kafka.producer.InvitationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class InvitationProducerManagerTest {
    @Mock
    private InvitationProducer invitationProducer;

    @InjectMocks
    private InvitationProducerManager invitationProducerManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendInvitationSuccess() {
        InvitationEvent invitationEvent = new InvitationEvent();
        doNothing().when(invitationProducer).sendInvitationEvent(invitationEvent);

        invitationProducerManager.sendInvitation(invitationEvent);

        verify(invitationProducer).sendInvitationEvent(invitationEvent);
    }

    @Test
    void testSendInvitationFailure() {
        InvitationEvent invitationEvent = new InvitationEvent();
        Exception exception = new RuntimeException("Sending failed");
        doThrow(exception).when(invitationProducer).sendInvitationEvent(invitationEvent);

        Exception thrownException = assertThrows(RuntimeException.class, () -> {
            invitationProducerManager.sendInvitation(invitationEvent);
        });

        assertEquals("Sending failed", thrownException.getMessage());
        verify(invitationProducer).sendInvitationEvent(invitationEvent);
    }
}