package com.spriadka.pitest;

import com.spriadka.pitest.configuration.ConfigurationLoader;
import com.spriadka.pitest.configuration.PITestConfiguration;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "PIT", defaultPhase = LifecyclePhase.VERIFY)
public class PitMojo extends AbstractMojo {

    @Parameter(property = "project", required = true, readonly = true)
    private MavenProject mavenProject;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Called");
    }
}
