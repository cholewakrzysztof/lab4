package pwr.student.FitnessTracker;

import javax.swing.*;
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


}
