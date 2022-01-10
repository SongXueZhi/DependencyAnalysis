package maven;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MavenReader {
    private MavenXpp3Reader mavenReader = new MavenXpp3Reader();

    public List<Dependency> getPomDependencies(File pom) {
        List<Dependency> result = new ArrayList<>();
        try {
            Model pomModel = getPomModel(pom);
            DependencyManagement dependencyManagement = pomModel.getDependencyManagement();
            if (dependencyManagement == null) {
                return pomModel.getDependencies();
            } else {
                return pomModel.getDependencyManagement().getDependencies();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected Model getPomModel(File pomFile) throws Exception {
        Model pomModel = mavenReader.read(new FileReader(pomFile));
        return pomModel;
    }
}
