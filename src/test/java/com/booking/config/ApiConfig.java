package com.booking.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Test settings, loaded once from config.properties.
 * A system property with the same name (-Dbase.url=...) overrides the file.
 *
 * @param baseUrl  root address of the API
 * @param username administrator user name
 * @param password administrator password
 */
public record ApiConfig(String baseUrl, String username, String password) {

    private static final String CONFIG_FILE = "config.properties";
    private static final ApiConfig INSTANCE = load();

    /** Compact constructor: stop immediately with a clear message if a value is missing. */
    public ApiConfig {
        requireValue(baseUrl, "base.url");
        requireValue(username, "auth.username");
        requireValue(password, "auth.password");
    }

    /** @return the settings for this test run */
    public static ApiConfig get() {
        return INSTANCE;
    }

    /** Records print all fields by default - never show the password in logs. */
    @Override
    public String toString() {
        return "ApiConfig[baseUrl=%s, username=%s, password=****]".formatted(baseUrl, username);
    }

    private static ApiConfig load() {
        Properties properties = readFile();
        return new ApiConfig(
                valueOf(properties, "base.url"),
                valueOf(properties, "auth.username"),
                valueOf(properties, "auth.password"));
    }

    private static Properties readFile() {
        Properties properties = new Properties();
        try (InputStream input = ApiConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(CONFIG_FILE + " was not found on the test classpath");
            }
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + CONFIG_FILE, e);
        }
    }

    private static String valueOf(Properties properties, String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    private static void requireValue(String value, String key) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing configuration value: " + key);
        }
    }
}