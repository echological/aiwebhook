import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int KEY_SIZE_BYTES = 32;

    public static void main(String[] args) {
        String apiKey = generateApiKey();
        System.out.println("Generated API Key:");
        System.out.println(apiKey);
    }

    private static String generateApiKey() {
        byte[] bytes = new byte[KEY_SIZE_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
