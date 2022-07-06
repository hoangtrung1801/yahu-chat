package dao.implement;

import dao.ConversationDAO;
import model.Conversation;
import model.GroupMember;
import model.User;
import utility.HibernateUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

public class ConversationDAOImpl implements ConversationDAO {
    private final EntityManager entityManager;

    public ConversationDAOImpl() {
        entityManager = HibernateUtils.getEntityManager();
    }

    @Override
    public List<Conversation> read() {
        return entityManager.createQuery("SELECT e FROM Conversation e", Conversation.class).getResultList();
    }

    @Override
    public Conversation readById(Integer id) {
        HibernateUtils.beginTransaction();
        Conversation conversation = entityManager.find(Conversation.class, id);
        HibernateUtils.commitTransaction();

        return conversation;
    }

    @Override
    public Conversation create(Conversation entity) {
        HibernateUtils.beginTransaction();
        entityManager.persist(entity);
        HibernateUtils.commitTransaction();
        return entity;
    }

    @Override
    public Conversation update(Conversation entity) {
        HibernateUtils.beginTransaction();
        entityManager.merge(entity);
        HibernateUtils.commitTransaction();
        return entity;
    }

    @Override
    public Conversation delete(Conversation entity) {
        return null;
    }

    @Override
    public Conversation findConversationWithUsers(User... users) {
        List<Conversation> conversations = entityManager.createQuery("SELECT e FROM Conversation e", Conversation.class).getResultList();

        return conversations.stream().reduce(null, (con1, con2) -> {
            // check if in group members of conversion had all users (input)
            Set<GroupMember> gm = con2.getGroupMembers();
            List<User> userFromGM = gm.stream().map(GroupMember::getUser).toList();

            return userFromGM.containsAll(List.of(users)) ? con2 : con1;
        });
    }
}
