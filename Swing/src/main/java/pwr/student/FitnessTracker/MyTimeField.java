package pwr.student.FitnessTracker;

import javax.swing.*;

public class MyTimeField extends JTextField {
    public boolean isProperTime(){
        String input = this.getText();
        try{
            MyTime.getDate(input);
            return true;
        }catch (Exception e){
            InfoBox.show("Wrong format of time should be \"hh:mm:ss\"","Wrong time input");
            return false;
        }
    }
}
