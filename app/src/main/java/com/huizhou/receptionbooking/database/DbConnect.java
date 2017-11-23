package com.huizhou.receptionbooking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;


/**
 * 数据库对接
 * Created by Administrator on 2017/8/3.
 */
public class DbConnect
{
    private static Connection conn;

    private static void setConn(List<String> errorList)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            errorList.add("连接数据库失败，请检查网络或重试.");
            return;
        }

        String ip = DbConfig.DB_IP;
        String userName = DbConfig.DB_USERNAME;
        String password = DbConfig.DB_PASSWORD;
        String port = DbConfig.DB_PORT;
        String dbName = DbConfig.DB_NAME;

        try
        {
            //链接数据库语句
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?user="
                    + userName + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";

            conn = (Connection) DriverManager.getConnection(url); //链接数据库

            if (null == conn)
            {
                conn = (Connection) DriverManager.getConnection(url); //链接数据库
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            errorList.add("连接数据库失败，请检查网络或重试.");
        }
    }

    public static Connection getConnection(List<String> errorList)
    {
        if (conn != null)
        {
            return conn;
        }
        else
        {
            setConn( errorList);
            return conn;
        }
    }
}

