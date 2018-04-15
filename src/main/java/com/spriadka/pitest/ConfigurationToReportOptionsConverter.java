package com.spriadka.pitest;

import com.spriadka.pitest.configuration.PITestConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.testapi.TestGroupConfig;
import org.pitest.util.Glob;

public class ConfigurationToReportOptionsConverter {

    private PITestConfiguration piTestConfiguration;
    private MavenProject mavenProject;
    private Log log;
    private Predicate<Artifact> dependencyFilter;

    public ReportOptions convert() {

        List<String> classPath = new ArrayList<>();
        try {
            classPath.addAll(mavenProject.getTestClasspathElements());
        } catch (DependencyResolutionRequiredException e) {
            log.info(e);
        }

        ReportOptions reportOptions = new ReportOptions();
        reportOptions.setTestPlugin(piTestConfiguration.getTestPlugin());
        reportOptions.setDependencyAnalysisMaxDistance(piTestConfiguration.getDependencyDistance());
        reportOptions.setIncludeLaunchClasspath(piTestConfiguration.isIncludeLaunchClasspath());
        reportOptions.setTargetTests(Glob.toGlobPredicates(Arrays.asList(piTestConfiguration.getTargetTests())));
        reportOptions.setTargetClasses(Arrays.asList(piTestConfiguration.getTargetClasses()));
        reportOptions.setNumberOfThreads(piTestConfiguration.getThreads());
        reportOptions.setDependencyAnalysisMaxDistance(piTestConfiguration.getDependencyDistance());
        reportOptions.setMutators(Arrays.asList(piTestConfiguration.getMutators()));
        reportOptions.setExcludedClasses(Arrays.asList(piTestConfiguration.getExcludedClasses()));
        reportOptions.setExcludedTestClasses(Glob.toGlobPredicates(Arrays.asList(piTestConfiguration.getExcludedTestClasses())));
        reportOptions.setExcludedMethods(Arrays.asList(piTestConfiguration.getExcludedMethods()));
        reportOptions.setVerbose(piTestConfiguration.isVerbose());
        reportOptions.setTimeoutConstant(piTestConfiguration.getTimeoutConst());
        reportOptions.setTimeoutFactor(piTestConfiguration.getTimeoutFactor());
        reportOptions.setJavaExecutable(piTestConfiguration.getJvmPath());
        reportOptions.setFailWhenNoMutations(piTestConfiguration.isFailWhenNoMutations());
        reportOptions.setTestPlugin(piTestConfiguration.getTestPlugin());
        reportOptions.setGroupConfig(new TestGroupConfig(Arrays.asList(piTestConfiguration.getExcludedGroups()),
            Arrays.asList(piTestConfiguration.getIncludedGroups())));

        reportOptions.setShouldCreateTimestampedReports(piTestConfiguration.isTimestampedReports());
        reportOptions.setMutationUnitSize(piTestConfiguration.getMaxMutationsPerClass());
        reportOptions.setExportLineCoverage(piTestConfiguration.isExportLineCoverage());
        reportOptions.addChildJVMArgs(Arrays.asList(piTestConfiguration.getJvmArgs()));
        reportOptions.addOutputFormats(Arrays.asList(piTestConfiguration.getOutputFormats()));
        reportOptions.setMutationEngine(piTestConfiguration.getMutationEngine());
        reportOptions.setExcludedRunners(Arrays.asList(piTestConfiguration.getExcludedRunners()));
        reportOptions.setDetectInlinedCode(piTestConfiguration.isDetectInlinedCode());
        setSourceRoots(reportOptions);

        return reportOptions;
    }

    private void setSourceRoots(ReportOptions reportOptions) {
        List<String> sourceRoots = new ArrayList<>();
        sourceRoots.addAll(mavenProject.getCompileSourceRoots());
        sourceRoots.addAll(mavenProject.getTestCompileSourceRoots());
        List<File> rootsAsFiles = sourceRoots.stream()
            .map(File::new)
            .collect(Collectors.toList());
        reportOptions.setSourceDirs(rootsAsFiles);
    }
}
