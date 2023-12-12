package pwr.student.FitnessTracker;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Set;

public class JListBuilder {
    public static DefaultListModel buildDFModel(Set<String> keys, Respond respond) throws SQLException {
        DefaultListModel df = new DefaultListModel();
        while(respond.getResult().next()){
            StringBuilder toprint = new StringBuilder();
            for (String s : keys) {
                if (s.contains("id"))
                    toprint.append(s).append(": ").append(respond.getResult().getInt(s)).append(" ");
                else
                    toprint.append(s).append(": ").append(respond.getResult().getString(s)).append(" ");
            }
            df.addElement(toprint.toString());
        }
        return df;
    }
}
