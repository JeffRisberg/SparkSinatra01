package com.incra.sparkui.config;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import org.apache.commons.configuration.Configuration;

import java.util.Iterator;

/**
 * App config backed by Archaius.
 */
public class ArchaiusAppConfig implements AppConfig {
    ArchaiusAppConfig() {
    }

    @Override
    public String getString(final String key, final String defaultValue) {
        return DynamicPropertyFactory.getInstance().getStringProperty(key, defaultValue).get();
    }

    @Override
    public int getInt(final String key, final int defaultValue) {
        return DynamicPropertyFactory.getInstance().getIntProperty(key, defaultValue).get();
    }

    @Override
    public long getLong(final String key, final long defaultValue) {
        return DynamicPropertyFactory.getInstance().getLongProperty(key, defaultValue).get();
    }

    @Override
    public double getDouble(final String key, final double defaultValue) {
        return DynamicPropertyFactory.getInstance().getDoubleProperty(key, defaultValue).get();
    }

    @Override
    public boolean getBoolean(final String key, final boolean defaultValue) {
        return DynamicPropertyFactory.getInstance().getBooleanProperty(key, defaultValue).get();
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        return ConfigurationManager.getConfigInstance().getKeys(prefix);
    }

    @Override
    public Configuration getUnderlyingConfig() {
        return ConfigurationManager.getConfigInstance();
    }

    /**
     * Sets an instance-level override. This will trump everything including
     * dynamic properties and system properties. Useful for tests.
     *
     * @param key   the specified key.
     * @param value the specified value.
     */
    @Override
    public void setOverrideProperty(final String key, final Object value) {
        ((ConcurrentCompositeConfiguration) ConfigurationManager.getConfigInstance()).setOverrideProperty(key, value);
    }
}
