package model;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "group_member")
public class GroupMember {
    @EmbeddedId
    private GroupMemberId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("conversationId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @Column(name = "joined_datetime")
    private Instant joinedDatetime;

    @Column(name = "left_datetime", nullable = false)
    private Instant leftDatetime;

    public GroupMemberId getId() {
        return id;
    }

    public void setId(GroupMemberId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
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

}