package pwr.student.FitnessTracker;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainForm extends JFrame{
    private JButton ButtonExecute;
    private JPanel JPanelTop;
    private JPanel JPanelCenter;
    private JPanel JPanelBottom;
    private JPanel JPanelMain;
    private JLabel JLabelTitle;
    private JTabbedPane JPanelAppCards;
    private JPanel TrainingsPanel;
    private JPanel SessionsPanel;
    private JPanel ExercisesPanel;
    private JPanel CustomTablePanel;
    private JList list1;
    private Integer counter =0;

    public MainForm(){
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Fitness Tracker");
        setSize(200,200);
        setLocationRelativeTo(null);
        setVisible(true);
        ButtonExecute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String src = JLabelTitle.getText()+counter.toString();
                JLabelTitle.setText(src);
                list1.add((new JLabel(src)));
            }
        });
        setContentPane(JPanelMain);
    }
    public void run() {
        pack();
        setVisible(true);
    }

    private void createUIComponents() {
        CustomTablePanel = new CustomJPanel();
        // TODO: place custom component creation code here
    }
}
