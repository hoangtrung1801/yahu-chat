package dao.implement;

import dao.ConversationDAO;
import dao.UserDAO;
import model.Conversation;
import model.GroupMember;
import model.User;
import utilities.HibernateUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConversationDAOImpl implements ConversationDAO {
    private EntityManager entityManager;
//    private EntityManagerFactory entityManagerFactory;

    public ConversationDAOImpl() {
        entityManager = HibernateUtils.getEntityManager();
//        entityManagerFactory = HibernateUtils.getEntityManagerFactory();
    }

    @Override
    public List<Conversation> read() {
        HibernateUtils.beginTransaction();
        List<Conversation> conversations = entityManager.createQuery("SELECT e FROM Conversation e", Conversation.class).getResultList();
        HibernateUtils.commitTransaction();

        return conversations;
    }

    @Override
    public Conversation readById(Integer id) {
        return entityManager.find(Conversation.class, id);
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
        return null;
    }

    @Override
    public Conversation delete(Conversation entity) {
        return null;
    }

    @Override
    public Conversation findConversationWithUsers(User... users) {
        HibernateUtils.beginTransaction();
        List<Conversation> conversations = entityManager.createQuery("SELECT e FROM Conversation e", Conversation.class).getResultList();
        Conversation result = conversations.stream().reduce(null, (con1, con2) -> {
            // check if in group members of conversion had all users (input)
            Set<GroupMember> gm = con2.getGroupMembers();
            List<User> userFromGM = gm.stream().map(groupMember -> groupMember.getUser()).collect(Collectors.toList());

            return userFromGM.containsAll(List.of(users)) ? con2 : con1;
        });

//        if(result != null)
//            System.out.println(result.getConversationName() + " " + result.getId());
        HibernateUtils.commitTransaction();

        return result;
    }

    public static void main(String[] args) {
//        UserDAO userDAO = new UserDAOImpl();
//        User user1 = userDAO.readById(5);
//        User user2 = userDAO.readById(9);
//
//        ConversationDAO conversationDAO = new ConversationDAOImpl();
//        conversationDAO.findConversationWithUsers(user1, user2);
    }
}
