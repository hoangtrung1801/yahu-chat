package dao.implement;

import dao.UserDAO;
import model.User;
import utilities.HibernateUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private EntityManager entityManager;

    public UserDAOImpl() {
        entityManager = HibernateUtils.getEntityManager();
    }

    @Override
    public List<User> read() {
        HibernateUtils.beginTransaction();
        List<User> users = entityManager.createQuery("SELECT a FROM User a", User.class).getResultList();
        HibernateUtils.commitTransaction();

        return users;
    }

    @Override
    public User readById(Integer id) {
        HibernateUtils.beginTransaction();
        User user = entityManager.find(User.class, id);
        HibernateUtils.commitTransaction();

        return user;
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
        try {
            HibernateUtils.beginTransaction();
            User user = (User) entityManager.createQuery("SELECT e FROM User e WHERE e.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
            HibernateUtils.commitTransaction();
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}
