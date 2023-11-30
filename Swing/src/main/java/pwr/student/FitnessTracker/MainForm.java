package pwr.student.FitnessTracker;
import javax.swing.*;
public class MainForm {
    JFrame frame;
    private JPanel MainPanel;
    private JLabel fieldTitle;

    public MainForm(){
        //Create and set up the window.
        frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(MainPanel);
    }
    public void run() {
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
