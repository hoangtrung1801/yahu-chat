package main.model;

import main.database.DatabaseConnection;
import main.utilities.SecurePassword;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User implements Serializable {

//    public static String queryGetUserWithID = "EXEC get_user_with_userID @userID=?";
//    public static String queryGetUserWithUsername = "EXEC get_user_with_username @username=?";
//    public static String queryNewUser = "EXEC new_user @username=?, @password=?";

    public static String queryGetAllUsers = "CALL get_users()";
    public static String queryGetUserWithID = "CALL  get_user_with_id(?)";
    public static String queryGetUserWithUsername = "CALL get_user_with_username(?)";
    public static String queryNewUser = "CALL create_user(?, ?)";

    private int userId;
    private String username;

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static boolean validateUser(String username, String password) {
        try {
            DatabaseConnection dbCon = DatabaseConnection.connect();

            PreparedStatement stGetUser = dbCon.conn.prepareStatement(queryGetUserWithUsername);
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
            PreparedStatement stmt = DatabaseConnection.conn.prepareStatement(queryNewUser);

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

    public static User getUserWithUsername(String username) {
        try {
            PreparedStatement st = DatabaseConnection.conn.prepareStatement(queryGetUserWithUsername);

            st.setString(1, username);
            ResultSet rs = st.executeQuery();

            // have no user like this
            if(!rs.next()) return null;

            return new User(rs.getInt(1), rs.getString(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUserWithID(int userID) {
         try {
            PreparedStatement st = DatabaseConnection.conn.prepareStatement(queryGetUserWithID);

            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            // have no user like this
            if(!rs.next()) return null;

            return new User(rs.getInt(1), rs.getString(2));
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
