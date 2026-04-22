package config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EnvConfig {

    private static final String ENV_FILE = ".env";
    private static final Map<String, String> ENV_VALUES = loadEnvFile();

    private EnvConfig() {
    }

    public static String get(String key) {
        String systemProperty = System.getProperty(toSystemPropertyName(key));
        if (isNotBlank(systemProperty)) {
            return systemProperty;
        }

        String processEnv = System.getenv(key);
        if (isNotBlank(processEnv)) {
            return processEnv;
        }

        String fileEnv = ENV_VALUES.get(key);
        if (isNotBlank(fileEnv)) {
            return fileEnv;
        }

        return null;
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return isNotBlank(value) ? value : defaultValue;
    }

    private static Map<String, String> loadEnvFile() {
        Path envPath = Path.of(ENV_FILE);
        if (!Files.exists(envPath)) {
            return Map.of();
        }

        Map<String, String> values = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(envPath);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains("=")) {
                    continue;
                }

                String[] parts = trimmed.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim();
                values.put(key, unquote(value));
            }
        } catch (IOException ignored) {
            return Map.of();
        }
        return Map.copyOf(values);
    }

    private static String toSystemPropertyName(String envKey) {
        String normalized = envKey;
        if (normalized.startsWith("RP_")) {
            normalized = normalized.substring(3);
        }
        return "rp." + normalized.toLowerCase().replace("_", ".");
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
