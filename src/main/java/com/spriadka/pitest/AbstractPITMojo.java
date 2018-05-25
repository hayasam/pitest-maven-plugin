package com.spriadka.pitest;

import com.spriadka.pitest.configuration.ConfigurationLoader;
import com.spriadka.pitest.configuration.PITestConfiguration;
import java.util.Collections;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.pitest.coverage.CoverageSummary;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.statistics.MutationStatistics;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;

public abstract class AbstractPITMojo extends AbstractMojo {

    private static final String SKIP_EXECUTION_PROPERTY = "pitest.skip";

    private static final String SKIP_TESTS_PROPERTY = "pitest.skipTests";

    @Parameter(required = true, readonly = true, property = "project")
    protected MavenProject project;

    protected final PluginServices pluginServices;

    protected Predicate<Artifact> getDependencyFilter() {
        return dependencyFilter;
    }

    protected Map<String, Artifact> getArtifactMap() {
        return artifactMap;
    }

    protected void setArtifactMap(Map<String, Artifact> artifactMap) {
        this.artifactMap = artifactMap;
    }

    protected final Predicate<Artifact> dependencyFilter;

    @Parameter(property = "plugin.artifactMap", readonly = true, required = true)
    private Map<String, Artifact> artifactMap;

    public AbstractPITMojo() {
        this(new PluginServices(AbstractPITMojo.class.getClassLoader()),
            new DependencyFilter(new PluginServices(AbstractPITMojo.class.getClassLoader())));
    }

    public AbstractPITMojo(PluginServices pluginServices, Predicate<Artifact> dependencyFilter) {
        this.pluginServices = pluginServices;
        this.dependencyFilter = dependencyFilter;
    }

    @Override
    public final void execute() throws MojoFailureException, MojoExecutionException {
        RunDecision runDecision = shouldRun();
        if (!runDecision.shouldRun()) {
            getLog().info("PITest execution was stopped because:");
            runDecision.getReasons().forEach(reason -> getLog().info("  - " + reason));
            return;
        }
        logPluginServices();
        PITestConfiguration configuration = ConfigurationLoader.load(project.getBasedir());
        Optional<CombinedStatistics> result = analyse(configuration);
        if (result.isPresent()) {
            throwErrorIfScoreBelowThreshold(result.get().getMutationStatistics(), configuration);
            throwErrorIfMoreThanMaximumSurvivors(result.get().getMutationStatistics(), configuration);
            throwErrorIfCoverageBelowThreshold(result.get().getCoverageSummary(), configuration);
        }
    }

    protected MavenProject getProject() {
        return project;
    }

    private void logPluginServices() {
        this.pluginServices.findClientClasspathPlugins()
            .forEach(clientClassPathPlugin -> getLog().info(
                "Found shared classpath plugin: " + clientClassPathPlugin.description()));
        this.pluginServices.findToolClasspathPlugins()
            .forEach(toolClasspathPlugin -> getLog().info("Found plugin: " + toolClasspathPlugin.description()));

    }

    protected Optional<CombinedStatistics> analyse(PITestConfiguration configuration)
        throws MojoExecutionException {
        EntryPoint entryPoint = new EntryPoint();
        ReportOptions reportOptions =
            new ConfigurationToReportOptionsConverter(this, configuration).createReportOptions();
        AnalysisResult result = entryPoint.execute(project.getBasedir(), reportOptions, this.pluginServices,
            configuration.getEnvironmentVariables());
        if (result.getError().hasSome()) {
            throw new MojoExecutionException("Mutation execution has failed", result.getError().value());
        }
        return Optional.of(result.getStatistics().value());
    }

    private RunDecision shouldRun() {
        RunDecision runDecision = new RunDecision();
        NonEmptyProjectChecker nonEmptyProjectCheck = new NonEmptyProjectChecker();
        if (System.getProperty(SKIP_EXECUTION_PROPERTY) != null) {
            runDecision.addReason("Execution of PIT should be skipped");
        }
        if (System.getProperty(SKIP_TESTS_PROPERTY) != null || System.getProperty("skipTests") != null) {
            runDecision.addReason("Test execution should be skipped");
        }
        if (this.project.getPackaging().equals("pom")) {
            runDecision.addReason("Project's packaging is POM");
        }
        if (!nonEmptyProjectCheck.test(project)) {
            runDecision.addReason("Project has no tests");
        }
        return runDecision;
    }

    static class RunDecision {

        private List<String> reasons = new ArrayList<>(4);

        boolean shouldRun() {
            return reasons.isEmpty();
        }

        public void addReason(String reason) {
            reasons.add(reason);
        }

        public List<String> getReasons() {
            return Collections.unmodifiableList(reasons);
        }
    }

    static class NonEmptyProjectChecker implements Predicate<MavenProject> {

        @Override
        @SuppressWarnings("unchecked")
        public boolean test(MavenProject mavenProject) {
            return Stream.concat((Stream<String>) mavenProject.getCompileSourceRoots().stream(),
                (Stream<String>) mavenProject.getTestCompileSourceRoots().stream())
                .allMatch(filePathExists);
        }

        private Predicate<String> filePathExists = filePath -> new File(filePath).exists();
    }

    private void throwErrorIfCoverageBelowThreshold(
        final CoverageSummary coverageSummary, PITestConfiguration configuration) throws MojoFailureException {
        int coverageThreshold = configuration.getCoverageThreshold();
        if ((coverageThreshold != 0)
            && (coverageSummary.getCoverage() < coverageThreshold)) {
            throw new MojoFailureException("Line coverage of "
                + coverageSummary.getCoverage() + "("
                + coverageSummary.getNumberOfCoveredLines() + "/"
                + coverageSummary.getNumberOfLines() + ") is below threshold of "
                + coverageThreshold);
        }
    }

    private void throwErrorIfScoreBelowThreshold(final MutationStatistics result, PITestConfiguration configuration)
        throws MojoFailureException {
        int mutationThreshold = configuration.getMutationThreshold();
        if ((mutationThreshold != 0)
            && (result.getPercentageDetected() < mutationThreshold)) {
            throw new MojoFailureException("Mutation score of "
                + result.getPercentageDetected() + " is below threshold of "
                + mutationThreshold);
        }
    }

    private void throwErrorIfMoreThanMaximumSurvivors(final MutationStatistics result, PITestConfiguration configuration)
        throws MojoFailureException {
        int maxSurviving = configuration.getMaxSurviving();
        if ((maxSurviving >= 0)
            && (result.getTotalSurvivingMutations() > maxSurviving)) {
            throw new MojoFailureException("Had "
                + result.getTotalSurvivingMutations() + " surviving mutants, but only "
                + maxSurviving + " survivors allowed");
        }
    }
}
