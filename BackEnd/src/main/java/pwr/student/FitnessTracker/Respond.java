package pwr.student.FitnessTracker;

import java.sql.ResultSet;

public class Respond {
    private ResultSet resultSet;
    private String[] columns;
    private Operation operation;

    public ResultSet getResult(){
        return resultSet;
    }
    public void setResult(ResultSet resultSet) { this.resultSet = resultSet; }
    public String[] getColumns(){return columns;}
    public void setColumns(String[] columns) { this.columns = columns; }
    public Operation getOperation() {return operation;}
    public void setOperation(Operation op) {this.operation = op;}

}
