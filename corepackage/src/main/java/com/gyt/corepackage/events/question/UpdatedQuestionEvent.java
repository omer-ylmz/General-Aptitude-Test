package com.gyt.corepackage.events.question;

import com.gyt.corepackage.events.Event;

public class UpdatedQuestionEvent implements Event {
    private Long id;
    private String text;

    public UpdatedQuestionEvent(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public UpdatedQuestionEvent() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
