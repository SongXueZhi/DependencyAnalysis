package example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.MySQLUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Tags {
    static Connection conn = MySQLUtil.Connect();
    public static void main(String []args) throws IOException {

        String sql = "select * from project.defects4j_dependency_string";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int NoTagNum = 0;
        int HttpErrorNum = 0;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                String lib = rs.getString("lib");
                org.jsoup.Connection.Response response = (org.jsoup.Connection.Response) Jsoup.connect("https://mvnrepository.com/artifact/" + lib)
                        .userAgent("Mozilla")
                        .timeout(50000).ignoreHttpErrors(true).execute();
                if(response.statusCode() != 200){
                    HttpErrorNum++;
                    System.out.println(lib + " " + response.statusCode());
                }else{
                    Document doc = response.parse();
                    Elements links = doc.getElementsByClass("tag");
                    if(links.isEmpty()){
                        NoTagNum++;
                        System.out.println(lib + " noTag");
                    }
                    else {
                        System.out.println(lib + " : ");
                        for (Element link : links) {
                            String linkHtml = link.html();
                            add2MySQL(lib,linkHtml);
//                            System.out.println(lib + " " + linkHtml);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //project:
        //No Tag Num = 142
        //HTTP Error Num = 22
        //defects4j_project:
        //No Tag Num = 10
        //HTTP Error Num = 7
        System.out.println("No Tag Num = " + NoTagNum);//142
        System.out.println("HTTP Error Num = " + HttpErrorNum);//22
        MySQLUtil.closeConnect();

    }

    public static void add2MySQL(String lib, String tag) {
        String sql = "insert into project.defects4j_lib_tag(lib, tag) values(?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, lib);
            ps.setString(2, tag);
            ps.executeUpdate();
            System.out.println(lib + " " + tag+ " 插入成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
