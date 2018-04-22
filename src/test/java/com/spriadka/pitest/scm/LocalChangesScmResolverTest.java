package com.spriadka.pitest.scm;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class LocalChangesScmResolverTest {

    @Mock
    private ScmManager manager;

    @Mock
    private Log log;

    @Rule
    public TemporaryFolder temporaryScmRoot = new TemporaryFolder();

    private LocalChangesScmResolver resolver;

    @Test
    public void should_return_files_with_matching_status() throws ScmException, IOException {

        resolver = createResolverInstance(Collections.singletonList(ScmFileStatus.ADDED));

        ScmResult scmResult = new ScmResult(null, null, null, true);
        List<ScmFile> scmFiles =
            Arrays.asList(new ScmFile("Hello", ScmFileStatus.ADDED), new ScmFile("World", ScmFileStatus.ADDED));
        Mockito.when(manager.status(any(ScmRepository.class), any(ScmFileSet.class)))
            .thenReturn(new StatusScmResult(scmFiles, scmResult));

        Assert.assertEquals(Arrays.asList("Hello", "World"), resolver.getAffectedFiles());
    }

    @Test
    public void should_return_empty_when_none_status_is_matched() throws IOException, ScmException {
        resolver = createResolverInstance(Collections.singletonList(ScmFileStatus.MODIFIED));

        ScmResult scmResult = new ScmResult(null, null, null, true);
        List<ScmFile> scmFiles =
            Arrays.asList(new ScmFile("Hello", ScmFileStatus.ADDED), new ScmFile("World", ScmFileStatus.ADDED));
        Mockito.when(manager.status(any(ScmRepository.class), any(ScmFileSet.class)))
            .thenReturn(new StatusScmResult(scmFiles, scmResult));

        Assert.assertEquals(Collections.emptyList(), resolver.getAffectedFiles());
    }

    @Test
    public void should_return_empty_when_error_occurs() throws IOException, ScmException {
        resolver = createResolverInstance(Collections.emptyList());
        Mockito.when(manager.status(any(ScmRepository.class), any(ScmFileSet.class))).thenThrow(ScmException.class);
        Assert.assertEquals(Collections.emptyList(), resolver.getAffectedFiles());
    }

    private LocalChangesScmResolver createResolverInstance(Collection<ScmFileStatus> statuses) throws IOException {
        return new LocalChangesScmResolver(File.createTempFile("prefix-", "-suffix", temporaryScmRoot.getRoot()), manager,
            null, log, statuses, Function.identity());
    }
}
