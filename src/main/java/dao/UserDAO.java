package dao;

import model.User;

import java.util.List;

public interface UserDAO extends DAO<User, Integer> {
    public User findWithUsername(String username);

    public List<User> findContact(String target);
}
