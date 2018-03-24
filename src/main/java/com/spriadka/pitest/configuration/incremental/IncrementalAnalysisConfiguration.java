package com.spriadka.pitest.configuration.incremental;

import com.spriadka.pitest.configuration.ConfigurationItem;
import com.spriadka.pitest.configuration.ConfigurationSection;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class IncrementalAnalysisConfiguration implements ConfigurationSection {

    private File historyInputLocation;
    private File historyOutputLocation;

    @Override
    public List<ConfigurationItem> registerConfigurationItems() {
        return Arrays.asList(new ConfigurationItem<>("historyInputLocation", "pitest.incremental.history.input.location",""),
            new ConfigurationItem<>("historyOutputLocation", "pitest.history.output.location",""));
    }

    public void setHistoryInputLocation(String filePath) {
        this.historyInputLocation = new File(filePath);
    }

    public void setHistoryOutputLocation(String filePath) {
        this.historyOutputLocation = new File(filePath);
    }
}
