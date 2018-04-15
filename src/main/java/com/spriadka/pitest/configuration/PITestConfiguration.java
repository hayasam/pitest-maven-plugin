package com.spriadka.pitest.configuration;

import com.spriadka.pitest.configuration.incremental.IncrementalAnalysisConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PITestConfiguration implements ConfigurationSection {

    public static final String PITEST_CONFIG_YAML = "pitest.yaml";
    public static final String PITEST_CONFIG_YML = "pitest.yml";

    private boolean includeLaunchClasspath;
    private String[] targetClasses;
    private String[] targetTests;
    private int dependencyDistance;
    private int threads;
    private boolean mutateStaticInits;
    private String[] mutators;
    private String[] excludedMethods;
    private String[] excludedClasses;
    private String[] excludedTests;
    private String[] avoidCallsTo;
    private boolean verbose;
    private float timeoutFactor;
    private int timeoutConst;
    private int maxMutationsPerClass;
    private String[] jvmArgs;
    private String jvmPath;
    private String[] outputFormats;
    private boolean failWhenNoMutations;
    private String[] classPath;
    private String[] mutableCodePaths;
    private String testPlugin;
    private String[] includedGroups;
    private String[] excludedGroups;
    private boolean detectInlinedCode;
    private boolean timestampedReports;
    private int mutationThreshold;
    private int coverageThreshold;

    private boolean exportLineCoverage;

    private String mutationEngine;
    private String[] excludedRunners;

    public String[] getExcludedTestClasses() {
        return excludedTestClasses;
    }

    public void setExcludedTestClasses(String[] excludedTestClasses) {
        this.excludedTestClasses = excludedTestClasses;
    }

    private String[] excludedTestClasses;

    private ScmConfiguration scm;
    private IncrementalAnalysisConfiguration incremental;

    @Override
    public List<ConfigurationItem> registerConfigurationItems() {
        return Arrays.stream(PITestConfigurationItems.values())
            .map(PITestConfigurationItems::getConfigurationItem)
            .collect(Collectors.toList());
    }

    public boolean isIncludeLaunchClasspath() {
        return includeLaunchClasspath;
    }

    public void setIncludeLaunchClasspath(boolean includeLaunchClasspath) {
        this.includeLaunchClasspath = includeLaunchClasspath;
    }

    public String[] getTargetClasses() {
        return targetClasses;
    }

    public void setTargetClasses(String[] targetClasses) {
        this.targetClasses = targetClasses;
    }

    public String[] getTargetTests() {
        return targetTests;
    }

    public void setTargetTests(String[] targetTests) {
        this.targetTests = targetTests;
    }

    public int getDependencyDistance() {
        return dependencyDistance;
    }

    public void setDependencyDistance(int dependencyDistance) {
        this.dependencyDistance = dependencyDistance;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public boolean isMutateStaticInits() {
        return mutateStaticInits;
    }

    public void setMutateStaticInits(boolean mutateStaticInits) {
        this.mutateStaticInits = mutateStaticInits;
    }

    public String[] getMutators() {
        return mutators;
    }

    public void setMutators(String[] mutators) {
        this.mutators = mutators;
    }

    public String[] getExcludedMethods() {
        return excludedMethods;
    }

    public void setExcludedMethods(String[] excludedMethods) {
        this.excludedMethods = excludedMethods;
    }

    public String[] getExcludedClasses() {
        return excludedClasses;
    }

    public void setExcludedClasses(String[] excludedClasses) {
        this.excludedClasses = excludedClasses;
    }

    public String[] getExcludedTests() {
        return excludedTests;
    }

    public void setExcludedTests(String[] excludedTests) {
        this.excludedTests = excludedTests;
    }

    public String[] getAvoidCallsTo() {
        return avoidCallsTo;
    }

    public void setAvoidCallsTo(String[] avoidCallsTo) {
        this.avoidCallsTo = avoidCallsTo;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public float getTimeoutFactor() {
        return timeoutFactor;
    }

    public void setTimeoutFactor(float timeoutFactor) {
        this.timeoutFactor = timeoutFactor;
    }

    public int getTimeoutConst() {
        return timeoutConst;
    }

    public void setTimeoutConst(int timeoutConst) {
        this.timeoutConst = timeoutConst;
    }

    public int getMaxMutationsPerClass() {
        return maxMutationsPerClass;
    }

    public void setMaxMutationsPerClass(int maxMutationsPerClass) {
        this.maxMutationsPerClass = maxMutationsPerClass;
    }

    public String[] getJvmArgs() {
        return jvmArgs;
    }

    public void setJvmArgs(String[] jvmArgs) {
        this.jvmArgs = jvmArgs;
    }

    public String getJvmPath() {
        return jvmPath;
    }

    public void setJvmPath(String jvmPath) {
        this.jvmPath = jvmPath;
    }

    public String[] getOutputFormats() {
        return outputFormats;
    }

    public void setOutputFormats(String[] outputFormats) {
        this.outputFormats = outputFormats;
    }

    public boolean isFailWhenNoMutations() {
        return failWhenNoMutations;
    }

    public void setFailWhenNoMutations(boolean failWhenNoMutations) {
        this.failWhenNoMutations = failWhenNoMutations;
    }

    public String[] getClassPath() {
        return classPath;
    }

    public void setClassPath(String[] classPath) {
        this.classPath = classPath;
    }

    public String[] getMutableCodePaths() {
        return mutableCodePaths;
    }

    public void setMutableCodePaths(String[] mutableCodePaths) {
        this.mutableCodePaths = mutableCodePaths;
    }

    public String getTestPlugin() {
        return testPlugin;
    }

    public void setTestPlugin(String testPlugin) {
        this.testPlugin = testPlugin;
    }

    public String[] getIncludedGroups() {
        return includedGroups;
    }

    public void setIncludedGroups(String[] includedGroups) {
        this.includedGroups = includedGroups;
    }

    public String[] getExcludedGroups() {
        return excludedGroups;
    }

    public void setExcludedGroups(String[] excludedGroups) {
        this.excludedGroups = excludedGroups;
    }

    public boolean isDetectInlinedCode() {
        return detectInlinedCode;
    }

    public void setDetectInlinedCode(boolean detectInlinedCode) {
        this.detectInlinedCode = detectInlinedCode;
    }

    public boolean isTimestampedReports() {
        return timestampedReports;
    }

    public void setTimestampedReports(boolean timestampedReports) {
        this.timestampedReports = timestampedReports;
    }

    public int getMutationThreshold() {
        return mutationThreshold;
    }

    public void setMutationThreshold(int mutationThreshold) {
        this.mutationThreshold = mutationThreshold;
    }

    public int getCoverageThreshold() {
        return coverageThreshold;
    }

    public void setCoverageThreshold(int coverageThreshold) {
        this.coverageThreshold = coverageThreshold;
    }

    public ScmConfiguration getScm() {
        return scm;
    }

    public void setScm(ScmConfiguration scm) {
        this.scm = scm;
    }

    public IncrementalAnalysisConfiguration getIncremental() {
        return incremental;
    }

    public void setIncremental(
        IncrementalAnalysisConfiguration incremental) {
        this.incremental = incremental;
    }

    public boolean isExportLineCoverage() {
        return exportLineCoverage;
    }

    public void setExportLineCoverage(boolean exportLineCoverage) {
        this.exportLineCoverage = exportLineCoverage;
    }

    public String getMutationEngine() {
        return mutationEngine;
    }

    public void setMutationEngine(String mutationEngine) {
        this.mutationEngine = mutationEngine;
    }

    public String[] getExcludedRunners() {
        return excludedRunners;
    }

    public void setExcludedRunners(String[] excludedRunners) {
        this.excludedRunners = excludedRunners;
    }
}
