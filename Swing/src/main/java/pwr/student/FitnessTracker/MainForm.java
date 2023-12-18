package pwr.student.FitnessTracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class MainForm extends JFrame{
    private JPanel JPanelTop;
    private JPanel JPanelCenter;
    private JPanel JPanelBottom;
    private JPanel JPanelMain;
    private JLabel JLabelTitle;
    private JTabbedPane TabbedPanel;
    private JPanel TrainingsPanel;
    private JPanel SessionsPanel;
    private JPanel ExercisesPanel;
    private JPanel JPanelListBody;
    private JPanel JPanelListExercises;
    private JPanel TargetsPanel;
    private JList JListBodyPart;


    public MainForm() {

        TabbedPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RefreshablePanel fu = (RefreshablePanel)((JPanel)((JTabbedPane)e.getSource()).getSelectedComponent()).getComponent(0);
                try {
                    fu.updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void start() {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().JPanelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.pack();
        frame.setVisible(true);
    }
}
