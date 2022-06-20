package dao;

import model.User;

public interface UserDAO extends DAO<User, Integer> {
    public User findWithUsername(String username);
}
