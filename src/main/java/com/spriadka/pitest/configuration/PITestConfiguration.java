package com.spriadka.pitest.configuration;

import com.spriadka.pitest.configuration.incremental.IncrementalAnalysisConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PITestConfiguration implements ConfigurationSection {

    public static final String PITEST_CONFIG_YAML = "pitest.yaml";
    public static final String PITEST_CONFIG_YML = "pitest.yml";

    private ScmConfiguration scmConfiguration;
    private IncrementalAnalysisConfiguration incrementalAnalysisConfiguration;

    @Override
    public List<ConfigurationItem> registerConfigurationItems() {
        return Arrays.stream(PITestConfigurationItems.values())
            .map(PITestConfigurationItems::getConfigurationItem)
            .collect(Collectors.toList());
    }
}
