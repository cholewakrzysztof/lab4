package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class TargetBuild extends JFrame{
    private JPanel Panel;
    private JTextField DateTextField;
    private JButton CreateTargetButton;
    private JList ListExercises;
    private JScrollPane ScrollPanel;
    private BackGate gateExercises;
    private BackGate gateTargets;
    private RefreshablePanel panel;

    public TargetBuild() {
        CreateTargetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyDateField dateField = (MyDateField)DateTextField;
                if(dateField.isProperDate()){
                    if(dateField.isDateInFuture()){
                        try {
                            String selectedExercise = ListExercises.getSelectedValue().toString();
                            String exId = Proxy.findPart(selectedExercise,"id");

                            HashMap<String,String> params = new HashMap<>();
                            params.put("success","0");
                            params.put("exerciseid",exId);
                            params.put("date",dateField.getText());

                            gateTargets.receiveRequest(RequestBuilder.buildRequest(Operation.INSERT,params.keySet().toArray(new String[0]),params));
                            panel.updateList();
                        }catch (Exception exeption){
                            InfoBox.show("Select exercise","No exercise selected");
                        }
                    }else InfoBox.show("Enter date in future","Deadline passed already");
                }
            }
        });
    }

    public static void start(RefreshablePanel panel) {
        JFrame frame = new JFrame("TargetBuild");
        TargetBuild tb = new TargetBuild();
        frame.setContentPane(tb.Panel);
        tb.panel = panel;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() throws Exception {
        DateTextField = new MyDateField();
        gateExercises = new BackGate("exercises");
        gateTargets = new BackGate("targets");
        ListExercises = new JList();
        updateList();

        ScrollPanel = new JScrollPane();
        ScrollPanel.add(ListExercises);
    }
    private void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("repeats","10");
        map.put("load","15");
        map.put("name","0");
        map.put("sql","SELECT load,repeats,name,exercises.id FROM exercises JOIN exercisestypes ON exercises.exercisetypeid = exercisestypes.id GROUP BY name,load,repeats");
        Proxy.manageSpecialOperation(gateExercises,"",map);
        ListExercises.setModel(JListBuilder.buildDFModel(map.keySet(),gateExercises.getRespond()));
        gateExercises.disconnect();
    }
}
