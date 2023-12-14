package pwr.student.FitnessTracker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Proxy {
    public static void manageDelete(BackGate gate, String input) throws Exception {
        HashMap<String, String> map = new HashMap<>();
        String id = findId(input);
        map.put("start", id);
        map.put("end", id);
        Request req = RequestBuilder.buildRequest(Operation.DELETE, new String[]{}, map);
        gate.receiveRequest(req);
    }

    public static void manageUpdate(BackGate gate, String input,HashMap<String,String> params) throws Exception {
        String id = findId(input);
        params.put("id",id);
        Request req = RequestBuilder.buildRequest(Operation.UPDATE, new String[]{}, params);
        gate.receiveRequest(req);
    }

    private static String findId(String input) {
        Pattern pattern = Pattern.compile(" id: \\d+|^id: \\d+");
        Matcher regex = pattern.matcher(input);
        Pattern numberPattern = Pattern.compile("[0-9]+");
        if (regex.find()) {
            String match = regex.group(0);
            Matcher number = numberPattern.matcher(match);
            if (number.find()) {
                return number.group(0);
            }
        }
        return "-1";
    }
}
