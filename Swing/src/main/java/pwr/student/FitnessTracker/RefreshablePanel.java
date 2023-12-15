package pwr.student.FitnessTracker;

import javax.swing.*;

public abstract class RefreshablePanel extends JPanel {
    public abstract void updateList() throws Exception;
}
