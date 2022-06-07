package main.client;

import main.model.User;

public class ApplicationContext {

    public static User user;

    public static void setUser(User user) {
        ApplicationContext.user = user;
    }

    public static User getUser() {
        return user;
    }
}