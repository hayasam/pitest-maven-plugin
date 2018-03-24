package com.spriadka.pitest.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScmConfiguration implements ConfigurationSection {

    private Range range;

    private Collection<ScmFileStatus> scmFileStatuses;

    @Override
    public List<ConfigurationItem> registerConfigurationItems() {
        return Collections.emptyList();
    }
}
