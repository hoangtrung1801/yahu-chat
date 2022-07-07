package dto;

import model.MessageType;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class MessageDto implements Serializable {
    private Integer id;
    private MessageType messageType;
    private String messageText;
    private Instant sentDatetime;
    private ConversationDto conversation;
    private UserDto user;

    public MessageDto() {
    }

    public MessageDto(String messageText, Instant sentDatetime, ConversationDto conversation, UserDto user) {
        this.messageType = MessageType.TEXT;
        this.messageText = messageText;
        this.sentDatetime = sentDatetime;
        this.conversation = conversation;
        this.user = user;
    }

    public MessageDto(MessageType messageType, String messageText, Instant sentDatetime, ConversationDto conversation, UserDto user) {
        this.messageType = messageType;
        this.messageText = messageText;
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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDto entity = (MessageDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.messageText, entity.messageText) &&
                Objects.equals(this.sentDatetime, entity.sentDatetime) &&
                Objects.equals(this.conversation, entity.conversation) &&
                Objects.equals(this.user, entity.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageText, sentDatetime, conversation, user);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "messageText = " + messageText + ", " +
                "sentDatetime = " + sentDatetime + ", " +
                "conversation = " + conversation + ", " +
                "user = " + user + ")";
    }
}
