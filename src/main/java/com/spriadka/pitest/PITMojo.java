package com.spriadka.pitest;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "PIT", defaultPhase = LifecyclePhase.VERIFY)
public class PITMojo extends AbstractMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (System.getProperty("pitest.config").isEmpty()) {
            getLog().info("Property not specified");
        }
        getLog().info(System.getProperty("pitest.config"));
    }
}
