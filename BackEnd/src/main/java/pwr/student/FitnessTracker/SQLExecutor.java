package pwr.student.FitnessTracker;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
    public static void createNewDatabase(String fileName) {
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
    public static void createNewTable(String sql) {
        String url = urlPath+"database.db";
        try (Connection conn = SQLExecutor.connect()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    public static void main(String[] args) throws Exception {
        //SQLExecutor.createNewDatabase("database.db");
        String sql = "CREATE TABLE IF NOT EXISTS exercisestypes (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	name text\n" +
                ");";
        //SQLExecutor sqlExecutor = new SQLExecutor();
        SQLExecutor.createNewTable(sql);
        sql = "CREATE TABLE IF NOT EXISTS exercises (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	repeats integer NOT NULL,\n" +
                "	load integer,\n" +
                "	exercisetypeid integer,\n" +
                "	trainingid integer \n" +
                ");";
        SQLExecutor.createNewTable(sql);
        sql = "CREATE TABLE IF NOT EXISTS bodyparts (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	name text\n" +
                ");";
        SQLExecutor.createNewTable(sql);
        sql = "CREATE TABLE IF NOT EXISTS trainings (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	bodypartid integer,\n" +
                "	sessionid integer\n" +
                ");";
        SQLExecutor.createNewTable(sql);
        sql = "CREATE TABLE IF NOT EXISTS sessions (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	date date,\n" +
                "	time time\n" +
                ");";
        SQLExecutor.createNewTable(sql);
        SQLExecutor sqlExecutor = new SQLExecutor();
        SQLBuilder sqlBuilder = new SQLBuilder();
        sqlBuilder.choseTable("bodyparts");
        Map<String,String> mapInsert = new HashMap<>();
        mapInsert.put("name","legs");
        sqlExecutor.executeSQL(sqlBuilder.buildInsert(mapInsert));
        String[] col = new String[]{"*"};
        ResultSet rs =sqlExecutor.select(sqlBuilder.buildSelect(col));

        while (rs.next()){
            System.out.println(rs.getInt("id"));
            System.out.println(rs.getString("name"));
        }
    }
}
