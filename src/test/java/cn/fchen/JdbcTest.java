package cn.fchen;

import java.sql.*;

/**
 * @Classname JdbcTest
 * @Description jdbc test
 * @Date 2019/2/22 11:38
 * @Author by Fchen
 */
public class JdbcTest {
    public static void main(String[] args) {
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://localhost:3306/db_test";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password = "root";
        //遍历查询结果集
        try {
            //1.加载驱动程序
            Class.forName(driver);
            //2.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //3.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
            String sql = "select * from user_test where id = 1";
            //4.执行sql语句获取结果集
            ResultSet rs = statement.executeQuery(sql);
            String name = null;
            while(rs.next()){
                //获取name这列数据
                name = rs.getString("name");
                System.out.println("name:"+name);
            }
            //5.释放资源
            rs.close();
            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
