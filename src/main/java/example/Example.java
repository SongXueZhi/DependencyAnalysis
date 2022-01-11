package example;

import maven.MavenReader;
import org.apache.maven.model.Dependency;
import utils.FileUtilx;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Example {
    public static void main(String[] args) {

        String projectPath = "/Users/lsn/Desktop/regminer/project_name/85b0550d-dc6b-4962-b1ec-6864cf046ca9";
        File projectFile = new File(projectPath);
        String[] pomFilePathArray = FileUtilx.getAllFilesFromDirByPattern(projectFile);

        MavenReader mavenReader = new MavenReader();
        List<Dependency> dependencies = new ArrayList<>();

        for (String pomFilePath : pomFilePathArray) {
            dependencies.addAll(mavenReader.getPomDependencies(new File(projectFile,pomFilePath)));
        }
        System.out.println("project dependency size: "+dependencies.size());
    }

}
