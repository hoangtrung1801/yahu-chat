package dao.implement;

import dao.UserDAO;
import model.User;
import org.hibernate.query.Query;
import utility.HibernateUtils;

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
        List users = entityManager.createQuery("SELECT user from User user where user.username = :username")
                .setParameter("username", username).getResultList();
        if(users.isEmpty()) return null;
        return (User) users.get(0);
    }

    @Override
    public List<User> findContact(String target) {
        Query<User> query = (Query<User>) entityManager.createNamedQuery("find_contact", User.class);
        String filter = "%" + target + "%";
        query.setParameter("filter", filter);
        List<User> result = query.getResultList();
        return result;
    }

    public static void main(String[] args) {
//        UserDAO userDAO = new UserDAOImpl();
//        List<User> users = userDAO.findContact("u");
//        for(var user: users) System.out.println(user);
    }
}
