package dto;

import java.time.Instant;
import java.util.Objects;

public class UserMessageDto {
    private int userId;
    private int messageId;
    private String username;
    private String messageText;
    private Instant sentDatetime;
    private ConversationDto conversation;

    public UserMessageDto(int userId, int messageId, String username, String messageText, Instant sentDatetime, ConversationDto conversation) {
        this.userId = userId;
        this.messageId = messageId;
        this.username = username;
        this.messageText = messageText;
        this.sentDatetime = sentDatetime;
        this.conversation = conversation;
    }

    public UserMessageDto() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "UserMessageDto{" +
                "userId=" + userId +
                ", messageId=" + messageId +
                ", username='" + username + '\'' +
                ", messageText='" + messageText + '\'' +
                ", sentDatetime=" + sentDatetime +
                ", conversation=" + conversation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMessageDto that = (UserMessageDto) o;
        return userId == that.userId && messageId == that.messageId && Objects.equals(username, that.username) && Objects.equals(messageText, that.messageText) && Objects.equals(sentDatetime, that.sentDatetime) && Objects.equals(conversation, that.conversation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, messageId, username, messageText, sentDatetime, conversation);
    }
}
