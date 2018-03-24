package com.spriadka.pitest.configuration;

import java.util.Collections;

public enum PITestConfigurationItems {

    INCLUDE_LAUNCH_CLASSPATH(new ConfigurationItem("includeLaunchClasspath","pitest.include.launch.classpath",false)),
    TARGET_CLASSES(new ConfigurationItem("targetClasses","pitest.target.classes", new String[0])),
    TARGET_TESTS(new ConfigurationItem("targetTests","pitest.target.tests", new String[0])),
    DEPENDENCY_DISTANCE(new ConfigurationItem("dependencyDistance","pitest.dependency.distance",0)),
    THREADS(new ConfigurationItem("threads", "pitest.threads", 1)),
    MUTATE_STATIC_INITS(new ConfigurationItem("mutateStaticInits","pitest.mutate.static.inits",false)),
    MUTATORS(new ConfigurationItem("mutators", "pitest.mutators", new String[0])),
    EXCLUDED_METHODS(new ConfigurationItem("excludedMethods","pitest.excluded.methods", new String[0])),
    EXCLUDED_CLASSES(new ConfigurationItem("excludedClasses","pitest.excluded.classes", new String[0])),
    EXCLUDED_TESTS(new ConfigurationItem("excludedTests","pitest.excluded.tests", new String[0])),
    AVOID_CALLS_TO(new ConfigurationItem("avoidCallsTo","pitest.avoid.calls.to", new String[0])),
    VERBOSE(new ConfigurationItem("verbose","pitest.verbose",false)),
    TIMEOUT_FACTOR(new ConfigurationItem("timeoutFactor", "pitest.timeout.factor",1.25)),
    TIMEOUT_CONST(new ConfigurationItem("timeoutConst","pitest.timeout.const",3000)),
    MAX_MUTATIONS_PER_CLASS(new ConfigurationItem("maxMutationsPerClass","pitest.max.mutations.per.class", 0)),
    JVM_ARGS(new ConfigurationItem("jvmArgs", "pitest.jvm.args",new String[0])),
    JVM_PATH(new ConfigurationItem("jvmPath","pitest.jvm.path", System.getProperty("JAVA_HOME"))),
    OUTPUT_FORMATS(new ConfigurationItem("outputFormats","pitest.output.formats",new String[]{"HTML"})),
    FAIL_WHEN_NO_MUTATIONS(new ConfigurationItem("failWhenNoMutations", "pitest.fail.when.no.mutations", true)),
    CLASS_PATH(new ConfigurationItem("classPath","pitest.class.path",new String[0])),
    MUTABLE_CODE_PATHS(new ConfigurationItem("mutableCodePaths", "pitest.mutable.code.paths", new String[0])),
    TEST_PLUGIN(new ConfigurationItem("testPlugin", "pitest.test.plugin","junit")),
    INCLUDED_GROUPS(new ConfigurationItem("includedGroups","pitest.included.groups", new String[0])),
    EXCLUDED_GROUPS(new ConfigurationItem("excludedGroups","pitest.excluded.groups", new String[0])),
    DETECT_INLINED_CODE(new ConfigurationItem("detectInlinedCode","pitest.detect.inlined.code", false)),
    TIMESTAMPED_REPORTS(new ConfigurationItem("timestampedReports","pitest.timestamped.reports", true)),
    MUTATION_THRESHOLD(new ConfigurationItem("mutationThreshold", "pitest.mutation.threshold", 0)),
    COVERAGE_THRESHOLD(new ConfigurationItem("coverageThreshold", "pitest.coverage.threshold", 0));

    private ConfigurationItem configurationItem;

    ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    PITestConfigurationItems(ConfigurationItem item) {
        this.configurationItem = item;
    }
}
