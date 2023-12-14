package pwr.student.FitnessTracker;

import java.util.HashMap;

public class RequestBuilder {
    public static Request buildRequest(Operation operation, String[] columns, HashMap<String,String> params){
        Request req = new Request();
        req.setOperation(operation);
        req.setColumns(columns);
        req.setParams(params);
        return req;
    }
}
