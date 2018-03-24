package com.spriadka.pitest.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ObjectMapper {

    static <T extends ConfigurationSection> T mapTo(Class<T> clazz, Map<String, Object> mapping) {
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to instantiate new " + clazz.getSimpleName(), e);
        }
        Arrays.stream(clazz.getMethods()).filter(method -> method.getName().startsWith("set"))
            .forEach(method -> invokeMethodWithMappedValue(method, instance, instance.registerConfigurationItems()));
        return instance;
    }

    private static <T extends ConfigurationSection> void invokeMethodWithMappedValue(Method method, T instance, Collection<ConfigurationItem> configurationItems) {
        method.setAccessible(true);
        if (method.getParameterTypes().length != 1) {
            return;
        }
        String propertyName = extractPropertyName(method);
        Optional<ConfigurationItem> correspondingConfigItem = configurationItems.stream()
            .filter(configurationItem -> configurationItem.getName().equals(propertyName))
            .findFirst();
        if (!correspondingConfigItem.isPresent()) {

        }
        else {
            try {
                method.invoke(instance, correspondingConfigItem.get().get());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static String extractPropertyName(Method method) {
        String propertyName = method.getName().substring(3);
        return String.format("%c%s", Character.toLowerCase(propertyName.charAt(0)), propertyName.substring(1));
    }
}
