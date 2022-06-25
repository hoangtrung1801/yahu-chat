package cclient;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.User;
import utilities.SecurePassword;

public class LoginController {

    private Login gui;
    private ChatClient chatClient;

    UserDAO userDAO;

    public LoginController(Login gui, ChatClient chatClient) {
        this.gui = gui;
        this.chatClient = chatClient;
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

            gui.frame.dispose();
            ChatClient.user = user;
            chatClient.loginSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showRegisterGUI() {
        Register register = new Register();
        register.show();
    }
}
