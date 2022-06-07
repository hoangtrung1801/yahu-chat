package main.model;

import main.database.DatabaseConnection;
import main.utilities.SecurePassword;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Serializable {

    private int userId;
    private String username;
    private String password;

    public User(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    public static boolean validateUser(String username, String password) {
        try {
            DatabaseConnection dbCon = DatabaseConnection.connect();

            String query = "EXEC get_user @username=?";
            PreparedStatement stGetUser = dbCon.conn.prepareStatement(query);
            stGetUser.setString(1, username);

            ResultSet rs = stGetUser.executeQuery();

            // no find user like this
            if(!rs.next()) return false;

            // check password
            String hashedPassword = rs.getString(3);
            if(!SecurePassword.comparePasswordWithHashedPassword(password, hashedPassword)) return false;

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean newUser(String username, String password) {
        try {
            String query = "EXEC new_user @username=?, @password=?";
            PreparedStatement stmt = DatabaseConnection.conn.prepareStatement(query);

            String hashedPassword = SecurePassword.hashPassword(password);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User getUser(String username) {
        try {
            String query = "EXEC get_user @username=?";
            PreparedStatement st = DatabaseConnection.conn.prepareStatement(query);

            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            // have no user like this
            if(!rs.next()) return null;

            return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
