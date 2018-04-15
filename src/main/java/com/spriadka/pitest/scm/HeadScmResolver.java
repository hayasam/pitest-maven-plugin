package com.spriadka.pitest.scm;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.ChangeFile;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.changelog.ChangeLogScmRequest;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;

public class HeadScmResolver extends AbstractScmResolver {

    public HeadScmResolver(File scmRoot, ScmManager scmManager, ScmRepository scmRepository,
        Log log, Collection<ScmFileStatus> fileStatuses, int numberOfCommits) {
        super(scmRoot, scmManager, scmRepository, log, fileStatuses);
        this.numberOfCommits = numberOfCommits;
    }

    public int getNumberOfCommits() {
        return numberOfCommits;
    }

    private int numberOfCommits;

    @Override
    protected List<String> getAffectedFiles() {
        try {
            ChangeLogScmRequest request = new ChangeLogScmRequest(scmRepository, new ScmFileSet(scmRoot));
            request.setLimit(numberOfCommits);
            ChangeLogScmResult result = scmManager.changeLog(request);
            if (!result.isSuccess()) {
                log.info("Execution of changelog for HEAD~" + numberOfCommits + " was not successful");
                return Collections.emptyList();
            }
            return result.getChangeLog()
                .getChangeSets()
                .stream()
                .flatMap(set -> set.getFiles().stream())
                .filter(file -> fileStatuses.contains(file.getAction()))
                .map(ChangeFile::getName)
                .collect(Collectors.toList());

        } catch (ScmException scm) {
            log.info("Error while getting files from HEAD~" + numberOfCommits, scm);
            return Collections.emptyList();
        }
    }
}
