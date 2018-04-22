package com.spriadka.pitest.configuration;

import java.util.Collections;

public enum PITestConfigurationItems {

    INCLUDE_LAUNCH_CLASSPATH(new ConfigurationItem("includeLaunchClasspath","pitest.include.launch.classpath",false)),
    TARGET_CLASSES(new ConfigurationItem("targetClasses","pitest.target.classes", Constants.EMPTY_ARRAY)),
    TARGET_TESTS(new ConfigurationItem("targetTests","pitest.target.tests", Constants.EMPTY_ARRAY)),
    DEPENDENCY_DISTANCE(new ConfigurationItem("dependencyDistance","pitest.dependency.distance",0)),
    THREADS(new ConfigurationItem("threads", "pitest.threads", 1)),
    MUTATE_STATIC_INITS(new ConfigurationItem("mutateStaticInits","pitest.mutate.static.inits",false)),
    MUTATORS(new ConfigurationItem("mutators", "pitest.mutators", Constants.EMPTY_ARRAY)),
    EXCLUDED_METHODS(new ConfigurationItem("excludedMethods","pitest.excluded.methods", Constants.EMPTY_ARRAY)),
    EXCLUDED_CLASSES(new ConfigurationItem("excludedClasses","pitest.excluded.classes", Constants.EMPTY_ARRAY)),
    EXCLUDED_TEST_CLASSES(new ConfigurationItem("excludedTestClasses","pitest.excludedTestClasses", Constants.EMPTY_ARRAY)),
    EXCLUDED_TESTS(new ConfigurationItem("excludedTests","pitest.excluded.tests", Constants.EMPTY_ARRAY)),
    AVOID_CALLS_TO(new ConfigurationItem("avoidCallsTo","pitest.avoid.calls.to", Constants.EMPTY_ARRAY)),
    VERBOSE(new ConfigurationItem("verbose","pitest.verbose",false)),
    TIMEOUT_FACTOR(new ConfigurationItem("timeoutFactor", "pitest.timeout.factor",1.25)),
    TIMEOUT_CONST(new ConfigurationItem("timeoutConst","pitest.timeout.const",3000)),
    MAX_MUTATIONS_PER_CLASS(new ConfigurationItem("maxMutationsPerClass","pitest.max.mutations.per.class", 0)),
    JVM_ARGS(new ConfigurationItem("jvmArgs", "pitest.jvm.args",Constants.EMPTY_ARRAY)),
    JVM_PATH(new ConfigurationItem("jvmPath","pitest.jvm.path", System.getProperty("JAVA_HOME"))),
    OUTPUT_FORMATS(new ConfigurationItem("outputFormats","pitest.output.formats",Constants.OUTPUT_FORMATS_DEFAULT)),
    FAIL_WHEN_NO_MUTATIONS(new ConfigurationItem("failWhenNoMutations", "pitest.fail.when.no.mutations", true)),
    CLASS_PATH(new ConfigurationItem("classPath","pitest.class.path",Constants.EMPTY_ARRAY)),
    MUTABLE_CODE_PATHS(new ConfigurationItem("mutableCodePaths", "pitest.mutable.code.paths", Constants.EMPTY_ARRAY)),
    TEST_PLUGIN(new ConfigurationItem("testPlugin", "pitest.test.plugin","junit")),
    INCLUDED_GROUPS(new ConfigurationItem("includedGroups","pitest.included.groups", Constants.EMPTY_ARRAY)),
    EXCLUDED_GROUPS(new ConfigurationItem("excludedGroups","pitest.excluded.groups", Constants.EMPTY_ARRAY)),
    DETECT_INLINED_CODE(new ConfigurationItem("detectInlinedCode","pitest.detect.inlined.code", false)),
    TIMESTAMPED_REPORTS(new ConfigurationItem("timestampedReports","pitest.timestamped.reports", true)),
    MUTATION_THRESHOLD(new ConfigurationItem("mutationThreshold", "pitest.mutation.threshold", 0)),
    COVERAGE_THRESHOLD(new ConfigurationItem("coverageThreshold", "pitest.coverage.threshold", 0)),
    EXPORT_LINE_COVERAGE(new ConfigurationItem("exportLineCoverage","pitest.exportLineCoverage",false)),
    EXCLUDED_RUNNERS(new ConfigurationItem("excludedRunners","pitest.excludedRunners",Constants.EMPTY_ARRAY)),
    MUTATION_ENGINE(new ConfigurationItem("mutationEngine", "pitest.mutationEngine","gregor")),
    MAX_SURVIVING(new ConfigurationItem("maxSurviving","pitest.max.surviving",-1)),
    ENVIRONMENT_VARIABLES(new ConfigurationItem("environmentVariables", "pitest.environment.variables", Collections.emptyMap())),
    REPORTS_DIRECTORY(new ConfigurationItem("reportsDirectory","pitest.reports.directory","")),
    ADDITIONAL_CLASSPATH_ELEMENTS(new ConfigurationItem("additionalClasspathElements","pitest.additional.classpath.elements",Constants.EMPTY_ARRAY)),
    CLASSPATH_DEPENDENCY_EXCLUDES(new ConfigurationItem("classpathDependencyExcludes", "pitest.classpath.dependency.excludes",Constants.EMPTY_ARRAY));
    private static class Constants {
        static final String[] EMPTY_ARRAY = new String[0];
        static final String[] OUTPUT_FORMATS_DEFAULT = new String[] {"HTML"};
    }
    private ConfigurationItem configurationItem;

    ConfigurationItem getConfigurationItem() {
        return configurationItem;
    }

    PITestConfigurationItems(ConfigurationItem item) {
        this.configurationItem = item;
    }
}
