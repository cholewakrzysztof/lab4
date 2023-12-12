package pwr.student.FitnessTracker;
import javax.swing.*;

public class MainForm extends JFrame{
    private JPanel JPanelTop;
    private JPanel JPanelCenter;
    private JPanel JPanelBottom;
    private JPanel JPanelMain;
    private JLabel JLabelTitle;
    private JTabbedPane JPanel;
    private JPanel TrainingsPanel;
    private JPanel SessionsPanel;
    private JPanel ExercisesPanel;
    private JPanel JPanelListBody;
    private JPanel JPanelListExercises;
    private JList JListBodyPart;


    public static void start() {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().JPanelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.pack();
        frame.setVisible(true);
    }
}
