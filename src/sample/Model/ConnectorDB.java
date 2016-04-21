package sample.Model;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Wojciech on 2015-10-21.
 */

/**
 * Class connect to DataBase
 * You need drivers JDBC for sqlite
 */
public class ConnectorDB {
    private Connection conn;
    public ConnectorDB()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Vehicles.sqlite");
        }
        catch(Exception e) {
            conn = null;
        }
    }

    public Connection getConn() {
        return conn;
    }
    public void closeDB(){
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        closeDB();
    }
}