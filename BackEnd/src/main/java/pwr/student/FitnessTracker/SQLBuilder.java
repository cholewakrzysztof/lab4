package pwr.student.FitnessTracker;
import java.util.Date;
import java.util.Map;

public class SQLBuilder {
    private String table;
    public String buildInsert(Map<String,String> values) {
        String sql =  "INSERT INTO " +this.table+" (";
        for (String key : values.keySet()) {
            sql += key+",";
        }
        sql = sql.substring(0,sql.lastIndexOf(','))+") VALUES(";
        for (String key : values.keySet()) {
            sql += getProperString(key,values.get(key))+",";
        }
        sql = sql.substring(0,sql.lastIndexOf(','))+");";
        return sql;
    }
    public String getProperString(String key, String value){
        switch (key){
            case "name" : return "'"+value+"'";
            case "repeats","load","sessionid","exercisetypeid","trainingid","bodypartid","date","time": return value;

        }
        return "";
    }
    public String buildSelect(String[] columns){
        StringBuilder sql = new StringBuilder("SELECT ");
        for (String col : columns)
            sql.append(col).append(",");
        sql = new StringBuilder(sql.substring(0, sql.length() - 1));
        sql.append(" FROM ").append(table);
        return sql.toString();
    }

    public String buildSearchSelect(String[] columns, Map<String,String> conditions){
        StringBuilder sql = new StringBuilder(buildSelect(columns));
        sql.append(" WHERE ");

        for (String key : conditions.keySet())
            sql.append(key).append("=").append(conditions.get(key)).append(" AND ");
        sql = new StringBuilder(sql.substring(0, sql.length() - 5));
        return sql.toString();
    }
    public String buildDelete(int start, int stop){
        return "DELETE from "+table+" WHERE id BETWEEN "+start+" AND "+stop;
    }

    public void choseTable(String name){
        this.table = name;
    }
}
