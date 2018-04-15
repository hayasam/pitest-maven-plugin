package com.spriadka.pitest.configuration;

import com.spriadka.pitest.scm.HeadScmResolver;
import com.spriadka.pitest.scm.LocalChangesScmResolver;
import com.spriadka.pitest.scm.ScmResolver;
import com.spriadka.pitest.scm.ScmResolverFactory;
import com.spriadka.pitest.scm.VersionScmResolver;
import edu.emory.mathcs.backport.java.util.Collections;
import java.io.File;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class ScmResolverFactoryTest {

    @Mock
    private File scmRoot;

    @Mock
    private ScmManager manager;

    @Mock
    private ScmRepository repository;

    @Mock
    private Log log;

    private ScmResolverFactory factory = new ScmResolverFactory(scmRoot, repository, manager, log, Collections.emptyList());

    @Test
    public void should_instantiate_localChangesResolver_from_local() {
        ScmResolver resolver = factory.fromRange("local");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(LocalChangesScmResolver.class.getName(), resolver.getClass().getName());
    }

    @Test
    public void should_instantiate_localChangesResolver_from_localChanges() {
        ScmResolver resolver = factory.fromRange("localChanges");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(LocalChangesScmResolver.class.getName(), resolver.getClass().getName());
    }

    @Test
    public void should_instantiate_lastCommitResolver_from_last() {
        ScmResolver resolver = factory.fromRange("last");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(HeadScmResolver.class.getName(), resolver.getClass().getName());
        Assert.assertEquals(((HeadScmResolver) resolver).getNumberOfCommits(), 1);
    }

    @Test
    public void should_instantiate_lastCommitResolver_from_lastCommit() {
        ScmResolver resolver = factory.fromRange("lastCommit");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(HeadScmResolver.class.getName(), resolver.getClass().getName());
        Assert.assertEquals(((HeadScmResolver) resolver).getNumberOfCommits(), 1);
    }

    @Test
    public void should_instantiate_lastTwoCommitsResolver_carets() {
        ScmResolver resolver = factory.fromRange("HEAD^^");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(HeadScmResolver.class.getName(), resolver.getClass().getName());
        Assert.assertEquals(((HeadScmResolver) resolver).getNumberOfCommits(), 2);
    }

    @Test
    public void should_instantiate_lastTwoCommitsResolver_tildas() {
        ScmResolver resolver = factory.fromRange("HEAD~~");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(HeadScmResolver.class.getName(), resolver.getClass().getName());
        Assert.assertEquals(((HeadScmResolver) resolver).getNumberOfCommits(), 2);
    }

    @Test
    public void should_instantiate_headScmResolver() {
        ScmResolver resolver = factory.fromRange("HEAD~3");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(HeadScmResolver.class.getName(), resolver.getClass().getName());
        Assert.assertEquals(((HeadScmResolver) resolver).getNumberOfCommits(), 3);
    }

    @Test
    public void should_instantiate_versionScmResolver() {
        ScmResolver resolver = factory.fromRange("9cb567ef");
        Assert.assertNotNull("Newly instantiated resolver should not be null",resolver);
        Assert.assertEquals(VersionScmResolver.class.getName(), resolver.getClass().getName());
    }

}
