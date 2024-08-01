package tokens;

import org.jasypt.util.text.BasicTextEncryptor;
import java.util.prefs.Preferences;

public class TokenStorage {

    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private static final String UPDATE_TOKEN_KEY = "update_token";
    private static final String ENCRYPTION_PASSWORD = "admin";

    private Preferences preferences;
    private BasicTextEncryptor textEncryptor;

    public TokenStorage() {
        preferences = Preferences.userNodeForPackage(TokenStorage.class);
        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(ENCRYPTION_PASSWORD);
    }

    public void saveTokens(String refreshToken, String updateToken) {
        String encryptedRefreshToken = textEncryptor.encrypt(refreshToken);
        String encryptedUpdateToken = textEncryptor.encrypt(updateToken);

        preferences.put(REFRESH_TOKEN_KEY, encryptedRefreshToken);
        preferences.put(UPDATE_TOKEN_KEY, encryptedUpdateToken);
    }

    public void saveRefreshTokens(String refreshToken) {
        String encryptedRefreshToken = textEncryptor.encrypt(refreshToken);

        preferences.put(REFRESH_TOKEN_KEY, encryptedRefreshToken);
    }

    public void saveUpdateTokens(String updateToken) {
        String encryptedUpdateToken = textEncryptor.encrypt(updateToken);

        preferences.put(UPDATE_TOKEN_KEY, encryptedUpdateToken);
    }

    public String getRefreshToken() {
        String encryptedRefreshToken = preferences.get(REFRESH_TOKEN_KEY, null);
        return encryptedRefreshToken != null ? textEncryptor.decrypt(encryptedRefreshToken) : null;
    }

    public String getUpdateToken() {
        String encryptedUpdateToken = preferences.get(UPDATE_TOKEN_KEY, null);
        return encryptedUpdateToken != null ? textEncryptor.decrypt(encryptedUpdateToken) : null;
    }
}