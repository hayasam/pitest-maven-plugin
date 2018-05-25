package com.spriadka.pitest.scm;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;

public class LocalChangesScmResolver extends AbstractScmResolver {

    public LocalChangesScmResolver(File scmRoot, ScmManager scmManager, ScmRepository scmRepository,
        Log log, Collection<ScmFileStatus> fileStatuses) {
        super(scmRoot, scmManager, scmRepository, log, fileStatuses);
    }

    @Override
    public List<String> resolveTargetClasses() {
        try {
            StatusScmResult result = scmManager.status(scmRepository, new ScmFileSet(scmRoot));
            return result.getChangedFiles()
                .stream()
                .filter(changedFile -> fileStatuses.contains(changedFile.getStatus()))
                .map(ScmFile::getPath)
                .collect(Collectors.toList());

        } catch (ScmException scm) {
            log.info("Error while getting affected local changes", scm);
            return Collections.emptyList();
        }
    }
}
