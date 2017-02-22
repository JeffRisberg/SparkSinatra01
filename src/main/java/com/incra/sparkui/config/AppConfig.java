package com.incra.sparkui.config;

import org.apache.commons.configuration.Configuration;

import java.util.Iterator;

/**
 * Application Config.
 */
public interface AppConfig {
    String getString(String key, String defaultValue);

    int getInt(String key, int defaultValue);

    long getLong(String key, long defaultValue);

    double getDouble(String key, double defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    Iterator<String> getKeys(String prefix);

    Configuration getUnderlyingConfig();

    /**
     * Sets an instance-level override. This will trump everything including
     * dynamic properties and system properties. Useful for tests.
     *
     * @param key   the specified key.
     * @param value the specified value.
     */
    void setOverrideProperty(String key, Object value);
}
