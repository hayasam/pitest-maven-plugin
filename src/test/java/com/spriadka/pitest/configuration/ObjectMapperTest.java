package com.spriadka.pitest.configuration;

import java.util.ArrayList;
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
    public void should_map_pitest_configuration_correctly() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("targetClasses", "hello, world, how, are, you");
        PITestConfiguration piTestConfiguration = ObjectMapper.mapTo(PITestConfiguration.class, mapping);
        Assert.assertTrue("Target classes should be properly set",
            Arrays.deepEquals(piTestConfiguration.getTargetClasses(),
                new String[] {"hello", "world", "how", "are", "you"}));
    }

    @Test
    public void should_handle_enum_properly() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("color", "yellow");
        SingleEnumConfigurationSection singleEnumConfigurationSection =
            ObjectMapper.mapTo(SingleEnumConfigurationSection.class, mapping);
        Assert.assertEquals(singleEnumConfigurationSection.color, Colors.YELLOW);
    }

    private static class SingleEnumConfigurationSection implements ConfigurationSection {

        private Colors color;

        public SingleEnumConfigurationSection() {

        }

        public void setColor(Colors color) {
            this.color = color;
        }

        @Override
        public List<ConfigurationItem> registerConfigurationItems() {
            return Collections.singletonList(new ConfigurationItem("color", "", Colors.BLACK));
        }
    }

    private enum Colors {
        BLACK,
        YELLOW,
        ORANGE
    }

    @Test
    public void should_handle_enum_array_properly() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("colors", "black, yellow, orange");
        EnumConfigurationSection enumConfigurationSection = ObjectMapper.mapTo(EnumConfigurationSection.class, mapping);
        Assert.assertTrue(Arrays.deepEquals(enumConfigurationSection.getColors(), new Colors[] {
            Colors.BLACK, Colors.YELLOW,
            Colors.ORANGE}));
    }

    @Test
    public void should_handle_enum_collection_properly() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("colors", "black, yellow, orange");
        EnumCollectionConfigurationSection enumCollectionConfigurationSection =
            ObjectMapper.mapTo(EnumCollectionConfigurationSection.class, mapping);
        Assert.assertTrue(Arrays.deepEquals(enumCollectionConfigurationSection.colors.stream().unordered().toArray(), new Colors[] {
            Colors.BLACK, Colors.YELLOW, Colors.ORANGE
        }));
    }

    private static class EnumCollectionConfigurationSection implements ConfigurationSection {

        public EnumCollectionConfigurationSection() {

        }

        private List<Colors> colors;

        public void setColors(List<Colors> colors) {
            this.colors = new ArrayList<>(colors);
        }

        @Override
        public List<ConfigurationItem> registerConfigurationItems() {
            return Collections.singletonList(new ConfigurationItem("colors", "", Collections.emptyList()));
        }
    }

    @Test
    public void should_map_nested_properties_properly() {
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> scmMapping = new HashMap<>();
        scmMapping.put("range", "HEAD~2");
        scmMapping.put("include", "Hello,World");
        mapping.put("scm", scmMapping);
        PITestConfiguration piTestConfiguration = ObjectMapper.mapTo(PITestConfiguration.class, mapping);
        ScmConfiguration scmConfiguration = new ScmConfiguration();
        scmConfiguration.setRange("HEAD~2");
        scmConfiguration.setInclude(new String[] {"Hello"});
        Assert.assertEquals(piTestConfiguration.getScm(), scmConfiguration);
    }

    @Test
    public void should_map_mapping_correctly() {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("hello", "sentence");
        mapping.put("sauron", "frodo");
        configurationSection = ObjectMapper.mapTo(CorrectlyMappedClass.class, mapping);
        Assert.assertEquals("sentence", configurationSection.hello);
    }

    private static class PrivateAccessClazz implements ConfigurationSection {
        private PrivateAccessClazz() {

        }

        @Override
        public List<ConfigurationItem> registerConfigurationItems() {
            return Collections.emptyList();
        }
    }

    private static class EnumConfigurationSection implements ConfigurationSection {

        public EnumConfigurationSection() {

        }

        private Colors[] colors;

        public void setColors(Colors[] colors) {
            this.colors = colors;
        }

        Colors[] getColors() {
            return colors;
        }

        @Override
        public List<ConfigurationItem> registerConfigurationItems() {
            return Collections.singletonList(new ConfigurationItem("colors", "", new Colors[0]));
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
            return Arrays.asList(new ConfigurationItem("hello", "", "world"),
                new ConfigurationItem("sauron", "", "abrakadabra"));
        }
    }
}
