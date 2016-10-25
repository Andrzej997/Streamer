package pl.polsl.encryption;

import java.security.MessageDigest;

/**
 * Created by Mateusz on 25.10.2016.
 */
public class ShaEncrypter {

    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte h : hash) {
                String hex = Integer.toHexString(0xff & h);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
