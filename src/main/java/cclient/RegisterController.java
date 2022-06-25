package cclient;

import dao.UserDAO;
import dao.implement.UserDAOImpl;
import model.User;
import utilities.SecurePassword;

public class RegisterController {

    private Register gui;

    UserDAO userDAO;

    public RegisterController(Register gui) {
        this.gui = gui;
        userDAO = new UserDAOImpl();
    }

    void register() {
        String username = gui.tUsername.getText();
        String password = gui.tPassword.getText();
        String confirmPassword = gui.tConfirmPassword.getText();

        // validate username, password
        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            System.out.println("Please type full information");
            return;
        }

        // validate if have same username in database
        if(userDAO.findWithUsername(username) != null) {
            System.out.println("Username is used");
            return;
        }

        // check password = confirmPassword
        if(!password.equals(confirmPassword)) {
            System.out.println("Password and confirm password is not the same");
        }

        // create new user
        try {
            User user = new User(username, SecurePassword.hashPassword(password));
            userDAO.create(user);
            System.out.println("Create user successfull : " + user.getId());
            gui.registerSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
