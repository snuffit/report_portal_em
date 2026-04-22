package config;

public final class ApiConfig {

    private static final String BASE_URL = "https://demo.reportportal.io";
    private static final String API_PREFIX = "/api/v1";
    private static final String DEFAULT_PROJECT = "default_personal";

    private ApiConfig() {
    }

    public static String getBaseUrl() {
        return EnvConfig.getOrDefault("RP_BASE_URL", BASE_URL);
    }

    public static String getApiPrefix() {
        return EnvConfig.getOrDefault("RP_API_PREFIX", API_PREFIX);
    }

    public static String getProjectName() {
        return EnvConfig.getOrDefault("RP_PROJECT_NAME", DEFAULT_PROJECT);
    }

    public static String getLogin() {
        String login = EnvConfig.get("RP_LOGIN");
        if (login == null) {
            throw new IllegalStateException("RP_LOGIN is not configured. Set it in .env or environment variables.");
        }
        return login;
    }

    public static String getPassword() {
        String password = EnvConfig.get("RP_PASSWORD");
        if (password == null) {
            throw new IllegalStateException("RP_PASSWORD is not configured. Set it in .env or environment variables.");
        }
        return password;
    }

    public static String getToken() {
        String token = EnvConfig.get("RP_TOKEN");
        if (token == null) {
            throw new IllegalStateException("RP_TOKEN is not configured. Set it in .env or environment variables.");
        }
        return token;
    }
}
