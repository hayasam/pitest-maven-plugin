package com.spriadka.pitest.configuration;


public class ConfigurationItem {

    private final String name;
    private final String systemProperty;
    private final Object defaultValue;

    public ConfigurationItem(String name, String systemProperty, Object defaultValue) {
        this.name = name;
        this.systemProperty = systemProperty;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getSystemProperty() {
        return systemProperty;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
