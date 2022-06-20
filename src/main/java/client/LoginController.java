package client;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.User;
import utilities.SecurePassword;

public class LoginController {

    Login gui;
    UserDAO userDAO;

    public LoginController(Login gui) {
        this.gui = gui;
        userDAO = new UserDAOImpl();
    }

    void login() {
        try {
            String username = gui.tUsername.getText();
            String password = gui.tPassword.getText();

            User user = userDAO.findWithUsername(username);
            if (user == null) {
                System.out.println("Username is invalid");
                return;
            }

            if (!SecurePassword.comparePasswordWithHashedPassword(password, user.getPassword())) {
                System.out.println("Password is not correct");
                return;
            }

            ApplicationContext.setUser(user);
            loginSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loginSuccess() {
        gui.frame.dispose();
        gui.client.loginSuccess();
    }

    void showRegisterGUI() {
        Register register = new Register();
        register.show();
    }
}
