package com.spriadka.pitest.scm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;

public abstract class AbstractScmResolver implements ScmResolver {

    protected final File scmRoot;
    protected final ScmManager scmManager;
    protected final ScmRepository scmRepository;
    protected final List<ScmFileStatus> fileStatuses;
    protected final Log log;

    public AbstractScmResolver(File scmRoot, ScmManager scmManager, ScmRepository scmRepository, Log log,
        Collection<ScmFileStatus> statuses) {
        this.scmRoot = scmRoot;
        this.scmManager = scmManager;
        this.scmRepository = scmRepository;
        this.log = log;
        this.fileStatuses = new ArrayList<>(statuses);
    }
}
