package edu.hncst.lchat.home.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class JDBCUtils {
    private static DataSource dataSource;

    static {
        try {
            Properties pro = new Properties();
            //加载配置文件
            InputStream resourceAsStream = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            pro.load(resourceAsStream);
            //初始化连接对象
            dataSource = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * 关闭资源对象
     * @param con
     * @param statement
     * @param rs
     */
    public static void close(Connection con, Statement statement, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if(statement != null){
                statement.close();
            }
            if(con != null){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            rs=null;
            statement=null;
            con=null;
        }
    }

    /**
     * 关闭资源对象
     * @param con
     * @param statement
     */
    public static void close(Connection con,Statement statement){

    }
}
