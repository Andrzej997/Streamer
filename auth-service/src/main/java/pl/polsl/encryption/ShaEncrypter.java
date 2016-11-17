package pl.polsl.encryption;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

/**
 * Created by Mateusz on 25.10.2016.
 */
@Component
public class ShaEncrypter implements PasswordEncoder {

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

    @Override
    public String encode(CharSequence rawPassword) {
        return ShaEncrypter.sha256(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String sha256 = ShaEncrypter.sha256(rawPassword.toString());
        return encodedPassword.equals(sha256);
    }
}
