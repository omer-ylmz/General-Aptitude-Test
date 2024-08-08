package com.gyt.corepackage.events.question;

public class DeletedQuestionEvent {
    Long id;

    public DeletedQuestionEvent(Long id) {
        this.id = id;
    }

    public DeletedQuestionEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
