package dao;

import model.Conversation;
import model.User;

public interface ConversationDAO extends DAO<Conversation, Integer> {
    public Conversation findConversationWithUsers(User... users);
}
