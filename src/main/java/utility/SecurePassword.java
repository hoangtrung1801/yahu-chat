package utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecurePassword {

    private static final String SECRET_TOKEN = "hello";

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
            String salt = getSalt();

            MessageDigest md = MessageDigest.getInstance("SHA-1");
//            md.update(salt.getBytes());
            md.update(SECRET_TOKEN.getBytes());

            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<bytes.length;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String hashedPassword = sb.toString();

            return hashedPassword;
    }

    public static boolean comparePasswordWithHashedPassword(String password, String hashedPassword) throws NoSuchAlgorithmException {
        return hashedPassword.equals(hashPassword(password));
    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

}
