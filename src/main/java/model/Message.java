package model;

import utility.MessageTypeConverter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "message")
public class Message {
    public Message() {}

    public Message(MessageType messageType, String messageText, String attachmentUrl, Instant sentDatetime) {
        this.messageType = messageType;
        this.messageText = messageText;
        this.attachmentUrl = attachmentUrl;
        this.sentDatetime = sentDatetime;
    }

    public Message(String messageText, Instant sentDatetime) {
        this.messageType = MessageType.TEXT;
        this.messageText = messageText;
        this.sentDatetime = sentDatetime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "message_type", nullable = false)
    @Convert(converter = MessageTypeConverter.class)
    private MessageType messageType;

    @Column(name = "message_text", length = 256)
    private String messageText;

    @Column(name = "attachment_url", length = 256)
    private String attachmentUrl;

    @Column(name = "sent_datetime", nullable = false)
    private Instant sentDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Instant getSentDatetime() {
        return sentDatetime;
    }

    public void setSentDatetime(Instant sentDatetime) {
        this.sentDatetime = sentDatetime;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", messageType=" + messageType +
                ", messageText='" + messageText + '\'' +
                ", attachmentUrl='" + attachmentUrl + '\'' +
                ", sentDatetime=" + sentDatetime +
                ", conversation=" + conversation +
                ", user=" + user +
                '}';
    }
}