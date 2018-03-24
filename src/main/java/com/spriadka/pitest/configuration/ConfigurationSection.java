package com.spriadka.pitest.configuration;

import com.spriadka.pitest.configuration.ConfigurationItem;
import java.util.List;

public interface ConfigurationSection {
    List<ConfigurationItem> registerConfigurationItems();
}
