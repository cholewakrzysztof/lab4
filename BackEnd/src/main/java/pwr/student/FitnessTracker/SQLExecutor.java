package pwr.student.FitnessTracker;

import java.sql.*;
import java.util.Date;
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
        SQLExecutor sqlExecutor = new SQLExecutor();
        SQLBuilder sqlBuilder = new SQLBuilder();
        Map<String,String> mapInsert = new HashMap<>();
        String sql;

        //tmpBaseBuild();
        //tmpInserts(sqlExecutor,sqlBuilder);
        String[] col = new String[]{"*"};

        //select all from bodyparts
        sqlBuilder.choseTable("bodyparts");
        mapInsert.put("id","0");
        mapInsert.put("name","arms");
        ResultSet rs =sqlExecutor.select(sqlBuilder.buildSelect(col));
        tmpPrinter(mapInsert, rs);


        //select all from exercisestypes
        sqlBuilder.choseTable("exercisestypes");
        mapInsert.put("id","0");
        mapInsert.put("name","pushup");
        rs =sqlExecutor.select(sqlBuilder.buildSelect(col));
        tmpPrinter(mapInsert, rs);


        //select all from exercises
        sqlBuilder.choseTable("exercises");
        mapInsert.put("exercisetypeid","2");
        mapInsert.put("id","0");
        mapInsert.put("trainingid","1");
        mapInsert.put("repeats","10");
        mapInsert.put("load","15");
        rs =sqlExecutor.select(sqlBuilder.buildSelect(col));
        tmpPrinter(mapInsert, rs);


        //select all from sessions
        sqlBuilder.choseTable("sessions");
        mapInsert.put("date",MyDate.getRepresentation(new Date(0)));
        mapInsert.put("id","0");
        mapInsert.put("time",MyTime.getRepresentation(new Date(0)));
        rs =sqlExecutor.select(sqlBuilder.buildSelect(col));
        tmpPrinter(mapInsert, rs);


        //select all from trainings
        sqlBuilder.choseTable("trainings");
        mapInsert.put("bodypartid","2");
        mapInsert.put("id","0");
        mapInsert.put("sessionid","1");
        rs =sqlExecutor.select(sqlBuilder.buildSelect(col));
        tmpPrinter(mapInsert, rs);
    }

    private static void tmpPrinter(Map<String, String> mapInsert, ResultSet rs) throws SQLException {
        while (rs.next()) {
            String toprint = "";
            for (String s : mapInsert.keySet()) {
                if (s.contains("id"))
                    toprint += s + ": " + rs.getInt(s)+" ";
                else
                    toprint += s + ": " + rs.getString(s)+" ";
            }
            System.out.println(toprint);
        }
        mapInsert.clear();
    }

    private static void tmpBaseBuild(){
        SQLExecutor.createNewDatabase("database.db");
        String sql = "CREATE TABLE IF NOT EXISTS exercisestypes (\n" +
                "	id integer PRIMARY KEY,\n" +
                "	name text UNIQUE\n" +
                ");";
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
                "	name text UNIQUE\n" +
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
    }

    private static void tmpInserts(SQLExecutor sqlExecutor,SQLBuilder sqlBuilder){
        Map<String,String> mapInsert = new HashMap<>();
        String sql;
        //insert to bodypatrs
        sqlBuilder.choseTable("bodyparts");
        mapInsert.put("name","arms");
        sqlExecutor.executeSQL(sqlBuilder.buildInsert(mapInsert));

        //insert to exercisestypes
        sqlBuilder.choseTable("exercisestypes");
        mapInsert.put("name","pushup");
        sqlExecutor.executeSQL(sqlBuilder.buildInsert(mapInsert));

        //insert to exercises
        sqlBuilder.choseTable("exercises");
        mapInsert.put("exercisetypeid","2");
        mapInsert.put("trainingid","1");
        mapInsert.put("repeats","10");
        mapInsert.put("load","15");
        sqlExecutor.executeSQL(sqlBuilder.buildInsert(mapInsert));

        //insert to trainings
        sqlBuilder.choseTable("trainings");
        mapInsert.put("bodypartid","2");
        mapInsert.put("sessionid","1");
        sqlExecutor.executeSQL(sqlBuilder.buildInsert(mapInsert));

        //insert to sessions
        sqlBuilder.choseTable("sessions");
        mapInsert.put("date",MyDate.getRepresentation(new Date(0)));
        mapInsert.put("time",MyTime.getRepresentation(new Date(0)));
        sql = sqlBuilder.buildInsert(mapInsert);
        sqlExecutor.executeSQL(sql);
    }
}
