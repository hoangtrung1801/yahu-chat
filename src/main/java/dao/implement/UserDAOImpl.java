package dao.implement;

import dao.UserDAO;
import model.User;
import utilities.HibernateUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private final EntityManager entityManager;

    public UserDAOImpl() {
        entityManager = HibernateUtils.getEntityManager();
    }

    @Override
    public List<User> read() {
        return entityManager.createQuery("SELECT a FROM User a", User.class).getResultList();
    }

    @Override
    public User readById(Integer id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User create(User user) {
        HibernateUtils.beginTransaction();
        entityManager.persist(user);
        HibernateUtils.commitTransaction();
        return user;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public User delete(User entity) {
        return null;
    }

    @Override
    public User findWithUsername(String username) {
        return (User) entityManager.createQuery("SELECT e FROM User e WHERE e.username = :username")
                .setParameter("username", username)
                .getSingleResult();
    }
}
