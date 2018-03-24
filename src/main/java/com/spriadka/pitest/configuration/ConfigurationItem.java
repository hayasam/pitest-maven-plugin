package com.spriadka.pitest.configuration;

import java.util.Optional;

public class ConfigurationItem<T> {

    private final String name;
    private final String systemProperty;
    private Optional<T> value;
    private final T defaultValue;

    public ConfigurationItem(String name, String systemProperty, T defaultValue) {
        this.name = name;
        this.systemProperty = systemProperty;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void set(T value) {
        this.value = Optional.of(value);
    }

    public T get() {
        return value.orElse(defaultValue);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, value.orElse(defaultValue));
    }
}
