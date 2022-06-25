package dao.implement;

import dao.MessageDAO;
import model.Message;
import utilities.HibernateUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    private final EntityManager entityManager;
    
    public MessageDAOImpl() {
        entityManager = HibernateUtils.getEntityManager();
    }
    
    @Override
    public List<Message> read() {
        List<Message> messages = entityManager.createQuery("SELECT e FROM Message e", Message.class).getResultList();
        return messages;
    }

    @Override
    public Message readById(Integer id) {
        return entityManager.find(Message.class, id);
    }

    @Override
    public Message create(Message entity) {
        HibernateUtils.beginTransaction();
        entityManager.persist(entity);
        HibernateUtils.commitTransaction();
        return entity;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }

    @Override
    public Message delete(Message entity) {
        return null;
    }
}
