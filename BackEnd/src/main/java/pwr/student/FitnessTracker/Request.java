package pwr.student.FitnessTracker;

import java.util.HashMap;

public class Request {
    private Operation operation;
    private String[] columns;
    private HashMap<String,String> params;
    public void setOperation(Operation op){ operation = op; }
    public Operation getOperation() { return operation; }
    public void setColumns(String[] s) { columns = s; }
    public String[] getColumns() {return columns; }
    public void setParams(HashMap<String,String> params) { this.params = params; }
    public HashMap<String,String> getParams(){ return params;}

}
