package pwr.student.FitnessTracker;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class MyDateField extends JTextField {
    public boolean isProperDate(){
        String input = this.getText();
        try{
            MyDate.getDate(input);
            return true;
        }catch (Exception e){
            InfoBox.show("Wrong format of date should be \"yyyy-MM-dd HH:mm:ss\"","Wrong date input");
            return false;
        }
    }
    public boolean isDateInFuture(){
        String input = this.getText();
        try{
            Date inputDate = MyDate.getDate(input);
            Date today = new Date();

            if(today.before(inputDate))
                return true;
            else
                return false;
        }catch (Exception e){
            InfoBox.show("Wrong format of date should be \"yyyy-MM-dd HH:mm:ss\"","Wrong date input");
            return false;
        }
    }


}
