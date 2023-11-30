package pwr.student.FitnessTracker;

import java.util.HashMap;

public class BackGate {
    public Respond respond;
    private final SQLExecutor sqlExecutor;
    private final SQLBuilder sqlBuilder;

    public BackGate(String table) throws Exception {
        this.sqlExecutor = new SQLExecutor();
        this.sqlBuilder = new SQLBuilder();
        sqlBuilder.choseTable(table);
    }
    public void receiveRequest(Request req) throws Exception {
        respond = new Respond();
        respond.setColumns(req.getColumns());
        String sql;
        switch (req.getOperation()){
            case SELECT -> {
                sql = sqlBuilder.buildSelect(req.getColumns());
                respond.setResult(sqlExecutor.select(sql));
                respond.setOperation(respond.getResult()!=null ? Operation.SELECT : Operation.ERROR);
            }
            case SEARCH_SELECT -> {
                HashMap<String,String> params = req.getParams();
                sql = sqlBuilder.buildSearchSelect(req.getColumns(), params);
                respond.setResult(sqlExecutor.select(sql));
                respond.setOperation(respond.getResult()!=null ? Operation.SEARCH_SELECT : Operation.ERROR);
            }
            case DELETE -> {
                sql = sqlBuilder.buildDelete(Integer.parseInt(req.getParams().get("start")),Integer.parseInt(req.getParams().get("end")));
                respond.setOperation(sqlExecutor.executeSQL(sql) ? Operation.DELETE : Operation.ERROR);

            }
            case INSERT -> {
                HashMap<String,String> params = req.getParams();
                sql = sqlBuilder.buildInsert(MyDate.getDate(params.get("date")),params.get("component"),params.get("person"),Integer.parseInt(params.get("priority")),params.get("description"));
                respond.setOperation(sqlExecutor.executeSQL(sql) ? Operation.INSERT : Operation.ERROR);
            }
            default -> throw new Exception("Not implemented");
        }
    }
    public Respond getRespond(){
        return respond;
    }
}
