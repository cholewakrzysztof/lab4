package pwr.student.FitnessTracker;

import javax.swing.*;

public class InfoBox {
    public static void show(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null, infoMessage, "Error: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }
}
