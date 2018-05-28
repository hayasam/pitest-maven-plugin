package com.spriadka.pitest.scm;

import java.io.File;
import java.util.function.Function;
import org.apache.maven.plugin.logging.Log;

public class PathToJavaClassConverter implements Function<String, String> {

    private final String sourceRoot;

    public PathToJavaClassConverter(String sourceRoot) {
        this.sourceRoot = sourceRoot;
    }

    @Override
    public String apply(String s) {
        final File f = new File(s);
        final String modifiedFilePath = f.getAbsolutePath();
        final String fileName = f.getName();
        if (modifiedFilePath.startsWith(this.sourceRoot)
            && (fileName.indexOf('.') != -1)) {
            return createClassGlobFromFilePath(this.sourceRoot, modifiedFilePath);
        }
        return "";
    }

    private String createClassGlobFromFilePath(final String sourceRoot,
        final String modifiedFilePath) {
        final String rootedPath = modifiedFilePath.substring(
            sourceRoot.length() + 1, modifiedFilePath.length());
        // some scms report paths in portable format, some in os specific format (i
        // think)
        // replace both possibilities regardless of host os
        return stripFileExtension(rootedPath).replace('/',
            '.').replace('\\', '.')
            + "*";
    }

    private String stripFileExtension(final String rootedPath) {
        return rootedPath.substring(0, rootedPath.lastIndexOf("."));
    }
}
