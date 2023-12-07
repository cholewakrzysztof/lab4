package pwr.student.FitnessTracker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTime {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    public static String getRepresentation(Date date){
        return sdf.format(date.getTime());
    }
    public static Date getDate(String representation) throws ParseException {
        return sdf.parse(representation);
    }
}
