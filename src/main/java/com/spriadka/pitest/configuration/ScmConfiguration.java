package com.spriadka.pitest.configuration;

import java.util.Arrays;
import java.util.List;

public class ScmConfiguration implements ConfigurationSection {

    private String range;

    private String[] include;

    private String connectionType;

    private String root;

    @Override
    public List<ConfigurationItem> registerConfigurationItems() {
        return Arrays.asList(new ConfigurationItem("range", "pitest.scm.range", "HEAD"),
            new ConfigurationItem("include", "pitest.scm.include", new String[0]),
            new ConfigurationItem("connectionType","pitest.scm.connectionType","connection"),
            new ConfigurationItem("root", "pitest.scm.root", null));
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String[] getInclude() {
        return include;
    }

    public void setInclude(String[] include) {
        this.include = include;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (! (obj instanceof ScmConfiguration)) {
            return false;
        }

        ScmConfiguration other = (ScmConfiguration) obj;
        return other.range.equals(this.range) && Arrays.equals(include, this.include) && this.connectionType.equals(other.connectionType);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + range.hashCode();
        hash = 31 * hash + root.hashCode();
        hash = 31 * hash + Arrays.deepHashCode(this.include);
        hash = 31 * hash + connectionType.hashCode();
        return hash;
    }
}
