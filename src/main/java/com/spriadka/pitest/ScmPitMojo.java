package com.spriadka.pitest;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.manager.ScmManager;

public class ScmPitMojo extends AbstractMojo {

    @Component
    private ScmManager scmManager;

    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    }
}
