package dto;

import java.io.Serializable;
import java.util.Objects;

public class ConversationDto implements Serializable {
    private Integer id;
    private String conversationName;

    public ConversationDto() {
    }

    public ConversationDto(Integer id, String conversationName) {
        this.id = id;
        this.conversationName = conversationName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationDto entity = (ConversationDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.conversationName, entity.conversationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conversationName);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "conversationName = " + conversationName + ")";
    }
}
