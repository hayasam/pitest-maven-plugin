package com.spriadka.pitest.scm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;

public class ScmResolverFactory {

    private final File scmRoot;
    private final ScmRepository repository;
    private final ScmManager manager;
    private final Log log;
    private final Collection<ScmFileStatus> statuses;

    public ScmResolverFactory (File scmRoot, ScmRepository repository, ScmManager manager, Log log,
        Collection<ScmFileStatus> statuses) {
        this.scmRoot = scmRoot;
        this.repository = repository;
        this.manager = manager;
        this.log = log;
        this.statuses = new ArrayList<>(statuses);
    }

    public ScmResolver fromRange(String range) {
        List<AbstractPatternScmResolverEntry> resolverEntries = new ArrayList<>(5);
        resolverEntries.add(new LocalChangesPatternScmResolver());
        resolverEntries.add(new LastCommitPatternScmResolver());
        resolverEntries.add(new LastTwoCommitsPatternScmResolver());
        resolverEntries.add(new HeadPatternScmResolver());
        resolverEntries.add(new RevisionPatternScmResolver());
        Optional<ScmResolver> result = resolverEntries.stream().filter(resolver -> resolver.matches(range))
            .findFirst()
            .map(resolverEntry -> resolverEntry.getResolver(range));
        return result.orElseGet(() -> new LocalChangesScmResolver(scmRoot, manager, repository, log, statuses));
    }

    private abstract class AbstractPatternScmResolverEntry {

        protected String pattern;

        AbstractPatternScmResolverEntry(String pattern) {
            this.pattern = pattern;
        }

        abstract ScmResolver getResolver(String value);
        boolean matches(String value) {
            return Pattern.compile(pattern).matcher(value).matches();
        }
    }

    private class LocalChangesPatternScmResolver extends AbstractPatternScmResolverEntry {

        LocalChangesPatternScmResolver() {
            super("^local|localChanges$");
        }

        @Override
        ScmResolver getResolver(String value) {
            return new LocalChangesScmResolver(scmRoot, manager, repository, log, statuses);
        }
    }

    private class LastCommitPatternScmResolver extends AbstractPatternScmResolverEntry {
        LastCommitPatternScmResolver() {
            super("^lastCommit|last$");
        }

        @Override
        ScmResolver getResolver(String value) {
            return new HeadScmResolver(scmRoot, manager, repository, log, statuses, 1);
        }
    }

    private class LastTwoCommitsPatternScmResolver extends AbstractPatternScmResolverEntry {
        LastTwoCommitsPatternScmResolver() {
            super("^HEAD(?:\\^\\^|~~)$");
        }

        @Override
        ScmResolver getResolver(String value) {
            return new HeadScmResolver(scmRoot, manager, repository, log, statuses, 2);
        }
    }

    private class HeadPatternScmResolver extends AbstractPatternScmResolverEntry {

        HeadPatternScmResolver() {
            super("^HEAD~(\\d+)$");
        }

        @Override
        ScmResolver getResolver(String value) {
            int numberOfCommits = Integer.valueOf(value.trim().substring("HEAD~".length()));
            return new HeadScmResolver(scmRoot, manager, repository, log, statuses, numberOfCommits);
        }
    }

    private class RevisionPatternScmResolver extends AbstractPatternScmResolverEntry {

        RevisionPatternScmResolver() {
            super(".+");
        }

        @Override
        ScmResolver getResolver(String value) {
            return new VersionScmResolver(scmRoot, manager, repository, log, statuses, value);
        }
    }
}
