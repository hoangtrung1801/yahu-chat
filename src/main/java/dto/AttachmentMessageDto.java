package dto;

import model.MessageType;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class AttachmentMessageDto implements Serializable {
    private Integer id;
    private MessageType messageType;
    private String messageText;
    private String attachmentUrl;
    private Instant sentDatetime;
    private ConversationDto conversation;
    private UserDto user;

    public AttachmentMessageDto() {
    }

    public AttachmentMessageDto(MessageType messageType, String messageText, String attachmentUrl, Instant sentDatetime, ConversationDto conversation, UserDto user) {
        this.messageType = messageType;
        this.messageText = messageText;
        this.attachmentUrl = attachmentUrl;
        this.sentDatetime = sentDatetime;
        this.conversation = conversation;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Instant getSentDatetime() {
        return sentDatetime;
    }

    public void setSentDatetime(Instant sentDatetime) {
        this.sentDatetime = sentDatetime;
    }

    public ConversationDto getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDto conversation) {
        this.conversation = conversation;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentMessageDto entity = (AttachmentMessageDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.messageType, entity.messageType) &&
                Objects.equals(this.messageText, entity.messageText) &&
                Objects.equals(this.attachmentUrl, entity.attachmentUrl) &&
                Objects.equals(this.sentDatetime, entity.sentDatetime) &&
                Objects.equals(this.conversation, entity.conversation) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageType, messageText, attachmentUrl, sentDatetime, conversation, user);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "messageType = " + messageType + ", " +
                "messageText = " + messageText + ", " +
                "attachmentUrl = " + attachmentUrl + ", " +
                "sentDatetime = " + sentDatetime + ", " +
                "conversation = " + conversation + ", " +
                "user = " + user + ")";
    }
}
