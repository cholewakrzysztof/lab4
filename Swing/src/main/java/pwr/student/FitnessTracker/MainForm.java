package pwr.student.FitnessTracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

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
    private JPanel TargetsPanel;
    private JList JListBodyPart;


    public MainForm() {

        JPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (Component component : JPanel.getComponents()) {
                    try {
                        //((RefreshablePanel) component).updateList();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
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
