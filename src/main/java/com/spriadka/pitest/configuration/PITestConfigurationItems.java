package com.spriadka.pitest.configuration;

import java.util.Collection;
import java.util.Collections;

public enum PITestConfigurationItems {

    INCLUDE_LAUNCH_CLASSPATH(new ConfigurationItem<>("includeLaunchClasspath","pitest.include.launch.classpath",false)),
    TARGET_CLASSES(new ConfigurationItem<Collection<String>>("targetClasses","pitest.target.classes", Collections.emptyList())),
    TARGET_TESTS(new ConfigurationItem<Collection<String>>("targetTests","pitest.target.tests", Collections.emptyList())),
    DEPENDENCY_DISTANCE(new ConfigurationItem<>("dependencyDistance","pitest.dependency.distance",0)),
    THREADS(new ConfigurationItem<>("threads", "pitest.threads", 1)),
    MUTATE_STATIC_INITS(new ConfigurationItem<>("mutateStaticInits","pitest.mutate.static.inits",false)),
    MUTATORS(new ConfigurationItem<Collection<String>>("mutators", "pitest.mutators", Collections.emptyList())),
    EXCLUDED_METHODS(new ConfigurationItem<Collection<String>>("excludedMethods","pitest.excluded.methods", Collections.emptyList())),
    EXCLUDED_CLASSES(new ConfigurationItem<Collection<String>>("excludedClasses","pitest.excluded.classes", Collections.emptyList())),
    EXCLUDED_TESTS(new ConfigurationItem<Collection<String>>("excludedTests","pitest.excluded.tests", Collections.emptyList())),
    AVOID_CALLS_TO(new ConfigurationItem<Collection<String>>("avoidCallsTo","pitest.avoid.calls.to", Collections.emptyList())),
    VERBOSE(new ConfigurationItem<>("verbose","pitest.verbose",false)),
    TIMEOUT_FACTOR(new ConfigurationItem<>("timeoutFactor", "pitest.timeout.factor",1.25)),
    TIMEOUT_CONST(new ConfigurationItem<>("timeoutConst","pitest.timeout.const",3000)),
    MAX_MUTATIONS_PER_CLASS(new ConfigurationItem<>("maxMutationsPerClass","pitest.max.mutations.per.class", 0)),
    JVM_ARGS(new ConfigurationItem<Collection<String>>("jvmArgs", "pitest.jvm.args",Collections.emptyList())),
    JVM_PATH(new ConfigurationItem<>("jvmPath","pitest.jvm.path", System.getProperty("JAVA_HOME"))),
    OUTPUT_FORMATS(new ConfigurationItem<Collection<String>>("outputFormats","pitest.output.formats",Collections.singleton("HTML"))),
    FAIL_WHEN_NO_MUTATIONS(new ConfigurationItem<>("failWhenNoMutations", "pitest.fail.when.no.mutations", true)),
    CLASS_PATH(new ConfigurationItem<>("classPath","pitest.class.path",Collections.singleton(""))),
    MUTABLE_CODE_PATHS(new ConfigurationItem<>("mutableCodePaths", "pitest.mutable.code.paths", Collections.emptyList())),
    TEST_PLUGIN(new ConfigurationItem<>("testPlugin", "pitest.test.plugin","junit")),
    INCLUDED_GROUPS(new ConfigurationItem<Collection<String>>("includedGroups","pitest.included.groups", Collections.emptyList())),
    EXCLUDED_GROUPS(new ConfigurationItem<Collection<String>>("excludedGroups","pitest.excluded.groups", Collections.emptyList())),
    DETECT_INLINED_CODE(new ConfigurationItem<>("detectInlinedCode","pitest.detect.inlined.code", false)),
    TIMESTAMPED_REPORTS(new ConfigurationItem<>("timestampedReports","pitest.timestamped.reports", true)),
    MUTATION_THRESHOLD(new ConfigurationItem<>("mutationThreshold", "pitest.mutation.threshold", 0)),
    COVERAGE_THRESHOLD(new ConfigurationItem<>("coverageThreshold", "pitest.coverage.threshold", 0));

    private ConfigurationItem configurationItem;

    ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    PITestConfigurationItems(ConfigurationItem item) {
        this.configurationItem = item;
    }
}
