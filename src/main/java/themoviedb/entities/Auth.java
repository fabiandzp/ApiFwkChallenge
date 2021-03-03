package themoviedb.entities;

public class Auth {
    private static String username;
    private static String password;
    private static String apiKey;
    private static String requestToken;
    private static String SessionId;
    private static boolean tokenValidation;

    public static boolean isSessionValidation() {
        return sessionValidation;
    }

    public static void setSessionValidation(boolean sessionValidation) {
        Auth.sessionValidation = sessionValidation;
    }

    private static boolean sessionValidation;


    public Auth(String apiKey, String username, String password) {
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        Auth.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Auth.password = password;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        Auth.apiKey = apiKey;
    }

    public static String getRequestToken() {
        return requestToken;
    }

    public static void setRequestToken(String requestToken) {
        Auth.requestToken = requestToken;
    }

    public static String getSessionId() {
        return SessionId;
    }

    public static void setSessionId(String sessionId) {
        SessionId = sessionId;
    }

    public static boolean getTokenValidation() {
        return tokenValidation;
    }

    public static void setTokenValidation(boolean tokenValidation) {
        Auth.tokenValidation = tokenValidation;
    }
}
