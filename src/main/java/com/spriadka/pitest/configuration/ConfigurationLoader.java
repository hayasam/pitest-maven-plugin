package com.spriadka.pitest.configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.apache.maven.plugin.MojoExecutionException;
import org.yaml.snakeyaml.Yaml;

public class ConfigurationLoader {

    public static final String PITEST_YML = "pitest.yml";
    public static final String PITEST_YAML = "pitest.yaml";
    public static final String PITEST_CONFIG = "pitest.config";

    public static PITestConfiguration load(File projectDir) {
        final String customConfigFilePath = System.getProperty(PITEST_CONFIG);
        File configFile;
        if (isCustomConfigFileValid(customConfigFilePath)) {
            configFile = new File(customConfigFilePath);
        } else {
            configFile = resolveConfigFileUsingDefaultStrategy(projectDir);
        }
        if (configFile == null) {
            return ObjectMapper.mapTo(PITestConfiguration.class, Collections.emptyMap());
        }
        return loadConfigurationFromFile(configFile);
    }

    private static File resolveConfigFileUsingDefaultStrategy(File projectDir) {
        return Arrays.stream(projectDir.listFiles())
            .filter(file -> file.getName().equals(PITEST_YML) || file.getName().equals(PITEST_YAML))
            .findFirst().orElse(null);
    }

    private static PITestConfiguration loadConfigurationFromFile(File configFile) {
        try (FileReader fileReader = new FileReader(configFile)) {
            final Yaml yaml = new Yaml();
            Map<String, Object> yamlConfigMap = yaml.load(fileReader);
            if (yamlConfigMap == null) {
                yamlConfigMap = Collections.emptyMap();
            }
            return ObjectMapper.mapTo(PITestConfiguration.class, yamlConfigMap);
        } catch (IOException e) {
            throw new RuntimeException("Error when reading config file", e);
        }
    }

    private static boolean isCustomConfigFileValid(String configFilePath) {
        if (configFilePath == null) {
            return false;
        }
        final File configFile = new File(configFilePath);
        return configFile.exists() && !configFile.isDirectory();
    }
}
