package com.gyt.corepackage.events.mail;

import com.gyt.corepackage.events.Event;

import java.util.List;

public class InvitationEvent implements Event {
    private List<String> emails; // Email listesi
    private String url; // Sınavın URL'i
    private String subject; // Email başlığı
    private String content; // Email içeriği
    private String senderName; // Gönderenin ismi
    private String eventName; // Davet etkinliğinin adı

    // Getter ve Setter metodları
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    // toString metodu
    @Override
    public String toString() {
        return "Event{" +
                "emails=" + emails +
                ", url='" + url + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", senderName='" + senderName + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
    }
}
