package com.spriadka.pitest;

import com.spriadka.pitest.configuration.PITestConfiguration;
import com.spriadka.pitest.configuration.ScmConfiguration;
import com.spriadka.pitest.scm.PathToJavaClassConverter;
import com.spriadka.pitest.scm.ScmResolver;
import com.spriadka.pitest.scm.ScmResolverFactory;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.manager.NoSuchScmProviderException;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;
import org.pitest.util.StringUtil;

@Mojo(name = "pitest-scm", defaultPhase = LifecyclePhase.VERIFY, requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class ScmPitMojo extends AbstractPITMojo {

    @Component
    private ScmManager scmManager;

    @Override
    protected Optional<CombinedStatistics> analyse(PITestConfiguration configuration)
        throws MojoExecutionException {
        List<String> modifiedTargetClasses = fetchModifiedTargetClasses(configuration);
        if (modifiedTargetClasses.isEmpty()) {
            getLog().info("PITest did not found any target classes, skipping execution");
            return Optional.empty();
        }
        String[] targetClasses = new String[modifiedTargetClasses.size()];
        modifiedTargetClasses.toArray(targetClasses);
        configuration.setTargetClasses(targetClasses);
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

    private List<String> fetchModifiedTargetClasses(PITestConfiguration configuration)
        throws MojoExecutionException {
        ScmConfiguration scmConfiguration = configuration.getScm();
        ScmRepository repository;
        try {
             repository = scmManager.makeScmRepository(getConnection(scmConfiguration));
        } catch (NoSuchScmProviderException | ScmRepositoryException e) {
            throw new MojoExecutionException("", e);
        }
        Collection<ScmFileStatus> includingFileStatus = Arrays.stream(ScmStatus.values())
            .filter(scmStatus -> matchesStatus(scmStatus, scmConfiguration.getInclude()))
            .map(ScmStatus::getStatus)
            .collect(Collectors.toList());
        ScmResolver resolver = new ScmResolverFactory(project.getBasedir(), repository, scmManager, getLog(), includingFileStatus)
            .fromRange(scmConfiguration.getRange());
        File sourceRoot = new File(project.getBuild().getSourceDirectory());
        return resolver.resolveTargetClasses().stream()
            .map(new PathToJavaClassConverter(sourceRoot.getAbsolutePath()))
            .filter(string -> !string.isEmpty())
            .distinct()
            .collect(Collectors.toList());
    }

    private String getConnection(ScmConfiguration scmConfiguration) throws MojoExecutionException {
        if (project.getScm() == null) {
            throw new MojoExecutionException("SCM connection for project not configured");
        }
        String connection = project.getScm().getConnection();
        if (scmConfiguration.getConnectionType().equalsIgnoreCase("connection") && !StringUtil.isNullOrEmpty(connection)) {
            return connection;
        }
        String developerConnection = project.getScm().getDeveloperConnection();
        if (scmConfiguration.getConnectionType().equalsIgnoreCase("developerConnection") && !StringUtil.isNullOrEmpty(developerConnection)) {
            return developerConnection;
        }
        throw new MojoExecutionException("SCM connection is not set");
    }

    private boolean matchesStatus(ScmStatus scmStatus, String... stringStatuses) {
        return Arrays.stream(stringStatuses).anyMatch(stringStatus -> stringStatus.equalsIgnoreCase(scmStatus.name()));
    }


}
