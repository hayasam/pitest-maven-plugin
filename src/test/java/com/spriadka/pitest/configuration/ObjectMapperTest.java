package com.spriadka.pitest.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;


public class ObjectMapperTest {

    private CorrectlyMappedClass configurationSection;

    @Test(expected = RuntimeException.class)
    public void should_throw_when_class_to_be_mapped_cannot_be_instantiated() {
        ObjectMapper.mapTo(PrivateAccessClazz.class, new HashMap<>());
    }

    @Test
    public void should_map_mapping_correctly() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("hello", "sentence");
        mapping.put("sauron", "frodo");
        configurationSection = ObjectMapper.mapTo(CorrectlyMappedClass.class, mapping);
        Assert.assertEquals(configurationSection.hello, "sentence");
    }

    private class PrivateAccessClazz implements ConfigurationSection {
        private PrivateAccessClazz() {

        }

        @Override
        public List<ConfigurationItem> registerConfigurationItems() {
            return Collections.emptyList();
        }
    }

    private static class CorrectlyMappedClass implements ConfigurationSection {

        private String hello;
        private String sauron;

        public void setHello(String hello) {
            this.hello = hello;
        }

        public void setSauron(String sauron) {
            this.sauron = sauron;
        }

        public CorrectlyMappedClass() {

        }

        @Override
        public List<ConfigurationItem> registerConfigurationItems() {
            return Arrays.asList(new ConfigurationItem<>("hello","","world"), new ConfigurationItem<>("sauron","","abrakadabra"));
        }
    }

}
