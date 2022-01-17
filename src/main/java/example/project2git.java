package example;

import org.eclipse.jgit.api.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

import java.util.UUID;

public class project2git {

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static Connection conn = null;
    static Statement stmt = null;
    static final String DB_URL = "jdbc:mysql://10.177.21.179:3306/project?useSSL=false&serverTimezone=UTC";

    // 数据库的用户名与密码
    static final String USER = "root";
    static final String PASS = "liusn991019";


    public static void main(String[] args){
        startMySQL();
        File file = new File("/Users/lsn/Desktop/regminer/project/project2git/projects_add.txt");
        BufferedReader reader=null;
        String temp=null;
        int line=1;
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                System.out.println("line "+line+": "+temp);
                gitClone(temp);
                line++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void gitClone(String projectName) {
        String remotePath = "https://github.com/"+projectName+".git";
        String uuid = getUUID();
        String filePath = "/Users/lsn/Desktop/regminer/project/dependency/" + projectName;

        try {
            CloneCommand cloneCommand = Git.cloneRepository();
            Git git = cloneCommand.setURI(remotePath)//要从中克隆的uri
                    .setDirectory(new File(filePath))//克隆到的目录
                    .setCloneAllBranches(true)
                    .call();

            // 添加到数据库中
//            add2MySQL(uuid, projectName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startMySQL() {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("加载数据库驱动成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("连接数据库驱动成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void add2MySQL(String uuid, String projectName){
        String url = "https://github.com/"+projectName+".git";

        String organization = "";
        String project_name = projectName;
        if(projectName.contains("/")) {
            organization = projectName.substring(0, projectName.indexOf("/"));
            project_name = projectName.substring(organization.length() + 1, projectName.length());
        }

        String sql = "insert into project.project_name(uuid,organization, project_name,url) values(?, ?, ?, ?)";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, uuid);
            ps.setString(2, organization);
            ps.setString(3, project_name);
            ps.setString(4, url);
            ps.executeUpdate();
            System.out.println(uuid + " " +organization + " " + project_name + " " + url + " 插入成功");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
