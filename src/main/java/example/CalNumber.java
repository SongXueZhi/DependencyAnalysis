package example;

import maven.MavenReader;
import org.apache.maven.model.Dependency;
import utils.FileUtilx;
import utils.ListUtil;
import utils.MySQLUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CalNumber {

    public static void main(String[] args) {

        String projectPath = "/Users/lsn/Desktop/regminer/project_name";
        File projectFile = new File(projectPath);
        String[] pomFilePathArray = FileUtilx.getAllFilesFromDirByPattern(projectFile);
        System.out.println("pom file size: " + pomFilePathArray.length);//234

        MavenReader mavenReader = new MavenReader();
        List<Dependency> dependencies = new ArrayList<>();

        for (String pomFilePath : pomFilePathArray) {
            dependencies.addAll(mavenReader.getPomDependencies(new File(projectFile,pomFilePath)));
        }
        System.out.println("project dependency size: "+dependencies.size());//1937
        Map<String,Integer> dependencyMap =  ListUtil.frequencyOfListElements(dependencies);
        System.out.println("dependency map size: "+dependencyMap.size());//821
        for (Map.Entry<String, Integer> entry : dependencyMap.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        add2MySQL(dependencyMap);
    }

    public static void add2MySQL(Map<String, Integer> dependencyMap) {
        Connection conn = MySQLUtil.Connect();

        for (Map.Entry<String, Integer> entry : dependencyMap.entrySet()) {
            String sql = "insert into project.dependency(lib, number) values(?, ?)";
            PreparedStatement ps = null;
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, entry.getKey());
                ps.setInt(2, entry.getValue());
                ps.executeUpdate();
                System.out.println(entry.getKey() + " " + entry.getValue() + " 插入成功");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        MySQLUtil.closeConnect();
    }

}
