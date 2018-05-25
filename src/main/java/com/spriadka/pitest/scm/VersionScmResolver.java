package com.spriadka.pitest.scm;

import java.util.Collections;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;

public class VersionScmResolver extends AbstractScmResolver {

    private final String revision;

    public VersionScmResolver(File scmRoot, ScmManager scmManager, ScmRepository scmRepository,
        Log log, Collection<ScmFileStatus> fileStatuses, String revision) {
        super(scmRoot, scmManager, scmRepository, log, fileStatuses);
        this.revision = revision;
    }

    @Override
    public List<String> resolveTargetClasses() {
        ChangeLogScmResult changeLogScmResult = null;
        try {
            changeLogScmResult = executeCommitChangelog(revision);
        } catch (ScmException scme) {
            try {
                changeLogScmResult = executeTagChangelog(changeLogScmResult, revision);
            } catch (ScmException scme1) {
                try {
                    changeLogScmResult = executeBranchChangelog(changeLogScmResult, revision);
                } catch (ScmException scme2) {
                    log.info("Failed to execute changelog for " + revision);
                    return Collections.emptyList();
                }
            }
        }
        if (!changeLogScmResult.isSuccess()) {
            log.info("Failed while getting changelog from " + revision);
            return Collections.emptyList();
        }
        return changeLogScmResult
            .getChangeLog()
            .getChangeSets()
            .stream()
            .flatMap(changeSet -> changeSet.getFiles().stream())
            .filter(changeFile -> fileStatuses.contains(changeFile.getAction()))
            .map(ChangeFile::getName)
            .collect(Collectors.toList());
    }

    private ChangeLogScmResult executeCommitChangelog(String revisionValue) throws ScmException {
        ChangeLogScmRequest changeLogScmRequest = new ChangeLogScmRequest(scmRepository, new ScmFileSet(scmRoot));
        changeLogScmRequest.setStartRevision(new ScmRevision(revisionValue));
        return scmManager.changeLog(changeLogScmRequest);
    }

    private ChangeLogScmResult executeTagChangelog(ChangeLogScmResult changeLogScmResult, String revisionValue) throws ScmException {
        if (changeLogScmResult != null && changeLogScmResult.isSuccess()) {
            return changeLogScmResult;
        }
        ChangeLogScmRequest changeLogScmRequest = new ChangeLogScmRequest(scmRepository, new ScmFileSet(scmRoot));
        changeLogScmRequest.setStartRevision(new ScmTag(revisionValue));
        return scmManager.changeLog(changeLogScmRequest);
    }

    private ChangeLogScmResult executeBranchChangelog(ChangeLogScmResult changeLogScmResult, String revisionValue) throws ScmException {
        if (changeLogScmResult != null && changeLogScmResult.isSuccess()) {
            return changeLogScmResult;
        }
        ChangeLogScmRequest changeLogScmRequest = new ChangeLogScmRequest(scmRepository, new ScmFileSet(scmRoot));
        changeLogScmRequest.setStartRevision(new ScmBranch(revisionValue));
        return scmManager.changeLog(changeLogScmRequest);
    }
}
