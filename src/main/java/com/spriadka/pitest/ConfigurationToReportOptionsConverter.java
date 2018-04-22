package com.spriadka.pitest;

import com.spriadka.pitest.configuration.PITestConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.logging.Log;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.testapi.TestGroupConfig;
import org.pitest.util.Glob;

public class ConfigurationToReportOptionsConverter {

    private final PITestConfiguration piTestConfiguration;
    private final AbstractPITMojo mojo;

    public ConfigurationToReportOptionsConverter(AbstractPITMojo mojo, PITestConfiguration configuration) {
        this.mojo = mojo;
        this.piTestConfiguration = configuration;
    }

    public ReportOptions createReportOptions() {

        List<String> classPath = new ArrayList<>();
        try {
            classPath.addAll(this.mojo.getProject().getTestClasspathElements());
        } catch (DependencyResolutionRequiredException e) {
            this.mojo.getLog().info(e);
        }
        addOwnDependenciesToClassPath(classPath);
        classPath.addAll(Arrays.asList(piTestConfiguration.getAdditionalClasspathElements()));
        for (Object artifact : this.mojo.getProject().getArtifacts()) {
            final Artifact dependency = (Artifact) artifact;
            if (Arrays.asList(piTestConfiguration.getClasspathDependencyExcludes()).contains(
                dependency.getGroupId() + ":" + dependency.getArtifactId())) {
                classPath.remove(dependency.getFile().getPath());
            }
        }
        ReportOptions reportOptions = new ReportOptions();
        reportOptions.setCodePaths(Collections.singleton(this.mojo.getProject().getBuild().getOutputDirectory()));
        reportOptions.setTestPlugin(piTestConfiguration.getTestPlugin());
        reportOptions.setClassPathElements(classPath);
        reportOptions.setDependencyAnalysisMaxDistance(piTestConfiguration.getDependencyDistance());
        reportOptions.setFailWhenNoMutations(piTestConfiguration.isFailWhenNoMutations());

        reportOptions.setTargetClasses(Arrays.asList(piTestConfiguration.getTargetClasses()));
        reportOptions.setTargetTests(Glob.toGlobPredicates(Arrays.asList(piTestConfiguration.getTargetTests())));

        reportOptions.setExcludedClasses(Arrays.asList(piTestConfiguration.getExcludedClasses()));
        reportOptions.setExcludedTestClasses(
            Glob.toGlobPredicates(Arrays.asList(piTestConfiguration.getExcludedTestClasses())));
        reportOptions.setExcludedMethods(Arrays.asList(piTestConfiguration.getExcludedMethods()));
        reportOptions.setNumberOfThreads(piTestConfiguration.getThreads());
        reportOptions.setExcludedRunners(Arrays.asList(piTestConfiguration.getExcludedRunners()));

        setReportDir(reportOptions,piTestConfiguration);
        reportOptions.setVerbose(piTestConfiguration.isVerbose());

        reportOptions.addChildJVMArgs(Arrays.asList(piTestConfiguration.getJvmArgs()));
        reportOptions.setMutators(Arrays.asList(piTestConfiguration.getMutators()));
        reportOptions.setTimeoutConstant(piTestConfiguration.getTimeoutConst());
        reportOptions.setTimeoutFactor(piTestConfiguration.getTimeoutFactor());

        setSourceRoots(reportOptions);
        reportOptions.addOutputFormats(Arrays.asList(piTestConfiguration.getOutputFormats()));

        reportOptions.setShouldCreateTimestampedReports(piTestConfiguration.isTimestampedReports());
        reportOptions.setMutationUnitSize(piTestConfiguration.getMaxMutationsPerClass());
        reportOptions.setDetectInlinedCode(piTestConfiguration.isDetectInlinedCode());

        reportOptions.setExportLineCoverage(piTestConfiguration.isExportLineCoverage());
        reportOptions.setMutationEngine(piTestConfiguration.getMutationEngine());
        reportOptions.setJavaExecutable(piTestConfiguration.getJvmPath());


        reportOptions.setGroupConfig(new TestGroupConfig(Arrays.asList(piTestConfiguration.getExcludedGroups()),
            Arrays.asList(piTestConfiguration.getIncludedGroups())));

        return reportOptions;
    }

    private void setReportDir(ReportOptions reportOptions, PITestConfiguration piTestConfiguration) {
        if (!piTestConfiguration.getReportsDirectory().isEmpty()) {
            File path = new File(piTestConfiguration.getReportsDirectory());
            reportOptions.setReportDir(path.getAbsolutePath());
        } else {
            reportOptions.setReportDir(this.mojo.getProject().getBasedir().getAbsolutePath() + "pit-reports");
        }
    }

    private void addOwnDependenciesToClassPath(final List<String> classPath) {
        this.mojo.getArtifactMap().forEach((key,value) -> this.mojo.getLog().info("Found Artifact: " + value));
        for (final Artifact dependency : filteredDependencies()) {
            this.mojo.getLog().info("Adding " + dependency.getGroupId() + ":"
                + dependency.getArtifactId() + " to SUT classpath");
            classPath.add(dependency.getFile().getAbsolutePath());
        }
    }

    private Collection<Artifact> filteredDependencies() {
        return this.mojo.getArtifactMap().values().stream().filter(this.mojo.getDependencyFilter()).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void setSourceRoots(ReportOptions reportOptions) {
        List<String> sourceRoots = new ArrayList<>();
        sourceRoots.addAll(this.mojo.getProject().getCompileSourceRoots());
        sourceRoots.addAll(this.mojo.getProject().getTestCompileSourceRoots());
        List<File> rootsAsFiles = sourceRoots.stream()
            .map(File::new)
            .collect(Collectors.toList());
        reportOptions.setSourceDirs(rootsAsFiles);
    }
}