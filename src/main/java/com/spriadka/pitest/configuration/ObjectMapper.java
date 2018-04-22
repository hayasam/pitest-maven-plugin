package com.spriadka.pitest.configuration;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectMapper {

    static <T extends ConfigurationSection> T mapTo(Class<T> clazz, Map<String, Object> mapping) {
        T instance;
        try {
            instance = clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to instantiate new " + clazz.getSimpleName(), e);
        }
        Arrays.stream(clazz.getMethods()).filter(method -> method.getName().startsWith("set"))
            .forEach(
                method -> invokeMethodWithMappedValue(method, instance, instance.registerConfigurationItems(), mapping));
        return instance;
    }

    private static <T extends ConfigurationSection> void invokeMethodWithMappedValue(Method method, T instance,
        Collection<ConfigurationItem> configurationItems, Map<String, Object> mapping) {
        method.setAccessible(true);
        if (method.getParameterTypes().length != 1) {
            return;
        }
        String propertyName = extractPropertyName(method);
        Object configValue = mapping.get(propertyName);
        Object convertedObject = convertObject(configValue, configurationItems, propertyName, method);
        try {
            method.invoke(instance, convertedObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static String extractPropertyName(Method method) {
        String propertyName = method.getName().substring(3);
        return String.format("%c%s", Character.toLowerCase(propertyName.charAt(0)), propertyName.substring(1));
    }

    @SuppressWarnings("unchecked")
    private static Object convertObject(Object configValue, Collection<ConfigurationItem> configurationItems,
        String property, Method method) {
        Optional<ConfigurationItem> correspondingConfigItem = configurationItems.stream()
            .filter(configurationItem -> configurationItem.getName().equals(property))
            .findFirst();
        Class<?> setterArgumentType = method.getParameterTypes()[0];
        if (!correspondingConfigItem.isPresent()) {
            if (!ConfigurationSection.class.isAssignableFrom(setterArgumentType)) {
                return null;
            } else {
                if (configValue == null) {
                    return mapTo((Class<ConfigurationSection>) setterArgumentType, Collections.emptyMap());
                } else {
                    return mapTo((Class<ConfigurationSection>) setterArgumentType, (Map<String, Object>) configValue);
                }
            }
        } else {
            ConfigurationItem configurationItem = correspondingConfigItem.get();
            Object mappedValue = getUserSetProperty(configValue, configurationItem);
            if (mappedValue == null && configurationItem.getDefaultValue() != null) {
                mappedValue = configurationItem.getDefaultValue();
            }
            if (mappedValue != null) {
                return convert(mappedValue, method);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Object convert(Object mappedValue, Method method) {
        Class<?> setterArgumentType = method.getParameterTypes()[0];
        if (setterArgumentType.isArray()) {
            return handleArray(mappedValue, setterArgumentType.getComponentType());
        } else if (List.class.isAssignableFrom(setterArgumentType)) {
            return handleList(mappedValue, method);
        } else if (setterArgumentType.isEnum()) {
            return handleEnum(mappedValue, setterArgumentType);
        } else if (setterArgumentType.isAssignableFrom(mappedValue.getClass())) {
            return mappedValue;
        } else if (ConfigurationSection.class.isAssignableFrom(setterArgumentType)) {
            return mapTo((Class<ConfigurationSection>) setterArgumentType, (Map<String, Object>) mappedValue);
        } else {
            return convertToType(setterArgumentType, mappedValue.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private static Object handleList(Object mappedValue, Method method) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length == 1) {
            Type type = genericParameterTypes[0];

            if (type instanceof ParameterizedType) {
                Type[] parameters = ((ParameterizedType) type).getActualTypeArguments();
                if (parameters.length == 1) {
                    return getConvertedList((Class<Object>) parameters[0], mappedValue);
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Enum handleEnum(Object mappedValue, Class<?> setterArgumentType) {
        if (mappedValue.getClass().isEnum()) {
            return (Enum) mappedValue;
        }
        String value = (String) mappedValue;
        return Enum.valueOf((Class<Enum>) setterArgumentType, value.toUpperCase());
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] handleArray(Object mappedValue, Class<T> setterArgumentType) {
        if (mappedValue != null && mappedValue.getClass().isArray()) {
            return (T[]) mappedValue;
        }
        List<T> convertedList = getConvertedList(setterArgumentType, mappedValue);
        T[] array = (T[]) Array.newInstance(setterArgumentType, convertedList.size());
        return convertedList.toArray(array);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> getConvertedList(Class<T> parameterType, Object mappedValue) {
        final Class<?> aClass = mappedValue.getClass();
        if (List.class.isAssignableFrom(aClass)) {
            return (List<T>) mappedValue;
        } else if (String.class.isAssignableFrom(aClass)) {
            final String value = (String) mappedValue;
            final List<String> values = Arrays.stream(value.split("\\s*,\\s*")).collect(Collectors.toList());
            List<T> convertedList = new ArrayList<>(values.size());
            for (String v : values) {
                convertedList.add((T) convertToType(parameterType, v));
            }
            return convertedList;
        }

        return null;
    }

    private static <T> Object convertToType(Class<T> clazz, String mappedValue) {
        if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            return Integer.valueOf(mappedValue);
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            return Float.valueOf(mappedValue);
        } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
            return Double.valueOf(mappedValue);
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            return Long.valueOf(mappedValue);
        } else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
            return Boolean.valueOf(mappedValue);
        } else if (String.class.equals(clazz)) {
            return mappedValue;
        } else if (clazz.isEnum()) {
            return handleEnum(mappedValue, clazz);
        }
        return null;
    }

    private static Object getUserSetProperty(Object configValue, ConfigurationItem configurationItem) {
        String systemPropertyKey = configurationItem.getSystemProperty();
        if (systemPropertyKey != null && !systemPropertyKey.isEmpty()) {
            String systemPropertyValue = System.getProperty(systemPropertyKey);
            return systemPropertyValue != null ? systemPropertyValue : configValue;
        }
        return configValue;
    }
}
