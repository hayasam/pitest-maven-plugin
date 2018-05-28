package com.spriadka.pitest.configuration.incremental;

import com.spriadka.pitest.configuration.ConfigurationItem;
import com.spriadka.pitest.configuration.ConfigurationSection;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class IncrementalAnalysisConfiguration implements ConfigurationSection {

    private String historyInputLocation;
    private String historyOutputLocation;
    private boolean enableDefault;

    @Override
    public List<ConfigurationItem> registerConfigurationItems() {
        return Arrays.asList(new ConfigurationItem("historyInputLocation", "pitest.incremental.history.input.location",null),
            new ConfigurationItem("historyOutputLocation", "pitest.incremental.history.output.location",null),
            new ConfigurationItem("enableDefault", "pitest.incremental.enable.default", false));
    }

    public String getHistoryInputLocation() {
        return historyInputLocation;
    }

    public String getHistoryOutputLocation() {
        return historyOutputLocation;
    }

    public void setHistoryInputLocation(String filePath) {
        this.historyInputLocation = filePath;
    }

    public void setHistoryOutputLocation(String filePath) {
        this.historyOutputLocation = filePath;
    }

    public boolean isEnableDefault() {
        return enableDefault;
    }

    public void setEnableDefault(boolean enableDefault) {
        this.enableDefault = enableDefault;
    }
}
