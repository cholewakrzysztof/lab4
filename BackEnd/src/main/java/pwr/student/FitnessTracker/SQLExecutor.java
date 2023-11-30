package pwr.student.FitnessTracker;

import java.sql.*;

public class SQLExecutor {
    private static final String urlPath = "jdbc:sqlite:.\\db\\";
    private final Connection conn;
    private static Connection connect() throws Exception {
        Connection conn;
        try {
            String url = urlPath+"database.db";
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            if (conn != null)
                return conn;
            else
                throw new Exception(urlPath);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public SQLExecutor() throws Exception {
        conn = connect();
    }
    public void createNewDatabase(String fileName) {
        String url = urlPath + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void createNewTable() {
        String url = urlPath+"database.db";

        String sql = "CREATE TABLE IF NOT EXISTS decision (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	date date NOT NULL,\n" +
                "	component text,\n" +
                "	person text text,\n" +
                "	priority integer, \n" +
                "	description text \n" +
                ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean executeSQL(String sql){
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
                if(stmt.getUpdateCount()!=0)
                    return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public ResultSet select(String sql){
        try{
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void close() throws SQLException {
        this.conn.close();
    }
}
