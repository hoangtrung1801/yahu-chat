package dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class GroupMemberDto implements Serializable {
    private GroupMemberIdDto id;
    private UserDto user;
    private ConversationDto conversation;
    private Instant joinedDatetime;
    private Instant leftDatetime;

    public GroupMemberDto() {
    }

    public GroupMemberDto(GroupMemberIdDto id, UserDto user, ConversationDto conversation, Instant joinedDatetime, Instant leftDatetime) {
        this.id = id;
        this.user = user;
        this.conversation = conversation;
        this.joinedDatetime = joinedDatetime;
        this.leftDatetime = leftDatetime;
    }

    public GroupMemberIdDto getId() {
        return id;
    }

    public void setId(GroupMemberIdDto id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ConversationDto getConversation() {
        return conversation;
    }

    public void setConversation(ConversationDto conversation) {
        this.conversation = conversation;
    }

    public Instant getJoinedDatetime() {
        return joinedDatetime;
    }

    public void setJoinedDatetime(Instant joinedDatetime) {
        this.joinedDatetime = joinedDatetime;
    }

    public Instant getLeftDatetime() {
        return leftDatetime;
    }

    public void setLeftDatetime(Instant leftDatetime) {
        this.leftDatetime = leftDatetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMemberDto entity = (GroupMemberDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.user, entity.user) &&
                Objects.equals(this.conversation, entity.conversation) &&
                Objects.equals(this.joinedDatetime, entity.joinedDatetime) &&
                Objects.equals(this.leftDatetime, entity.leftDatetime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, conversation, joinedDatetime, leftDatetime);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "user = " + user + ", " +
                "conversation = " + conversation + ", " +
                "joinedDatetime = " + joinedDatetime + ", " +
                "leftDatetime = " + leftDatetime + ")";
    }

    public static class GroupMemberIdDto implements Serializable {
        private Integer userId;
        private Integer conversationId;

        public GroupMemberIdDto() {
        }

        public GroupMemberIdDto(Integer userId, Integer conversationId) {
            this.userId = userId;
            this.conversationId = conversationId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getConversationId() {
            return conversationId;
        }

        public void setConversationId(Integer conversationId) {
            this.conversationId = conversationId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GroupMemberIdDto entity = (GroupMemberIdDto) o;
            return Objects.equals(this.userId, entity.userId) &&
                    Objects.equals(this.conversationId, entity.conversationId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, conversationId);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" +
                    "userId = " + userId + ", " +
                    "conversationId = " + conversationId + ")";
        }
    }
}
