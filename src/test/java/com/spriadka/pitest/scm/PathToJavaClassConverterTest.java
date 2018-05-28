package com.spriadka.pitest.scm;

import java.io.File;
import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

public class PathToJavaClassConverterTest {

    private static final String SOURCE_ROOT = new File("src/sample").getAbsolutePath();

    private final PathToJavaClassConverter pathToJavaClassConverter = new PathToJavaClassConverter(SOURCE_ROOT);

    @Test
    public void should_not_convert_file_without_extension() {
        Assert.assertTrue("File without extension should not be converted", applyWithSourceRoot("Hello").isEmpty());
    }

    @Test
    public void should_not_convert_files_not_in_source_root() {
        Assert.assertTrue("Files not contained in source root should not be converted",
            pathToJavaClassConverter.apply("not/under/source/Sample.java").isEmpty());
    }

    @Test
    public void should_strip_file_extension() {
        Assert.assertEquals("File extension should be stripped", "Stripped*", applyWithSourceRoot("Stripped.java"));
    }

    @Test
    public void should_return_fully_qualified_name() {
        Assert.assertEquals("File path should be converted to fully qualified name correctly", "com.example.Example*",
            applyWithSourceRoot("com/example/Example.java"));
    }

    private String applyWithSourceRoot(String value) {
        return pathToJavaClassConverter.apply(SOURCE_ROOT + "/" + value);
    }
}
