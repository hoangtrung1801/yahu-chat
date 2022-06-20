package dao.implement;

import dao.ConversationDAO;
import model.Conversation;
import utilities.HibernateUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

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
        return null;
    }

    @Override
    public Conversation delete(Conversation entity) {
        return null;
    }
}
