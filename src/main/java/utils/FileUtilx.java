package utils;

import org.apache.tools.ant.DirectoryScanner;

import java.io.File;

public class FileUtilx {

    public static String[] getAllFilesFromDirByPattern(File meta){
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(meta);
        scanner.setIncludes(new String[] { "pom.xml","**\\pom.xml" });
        scanner.setCaseSensitive(true);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        return files;
    }
}
