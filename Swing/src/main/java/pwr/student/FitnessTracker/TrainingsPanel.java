package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrainingsPanel{
    BackGate gate;
    private JPanel Panel;
    private JButton ButtonAdd;
    private JTextField SessionIdTextField;
    private JTextField BodyPartIdTextField;
    private JList TrainingList;
    private JScrollPane JScrollPaneList;
    private JButton ButtonDelete;
    private JButton ButtonUpdate;
    private JButton BuildTrainingButton;

    public TrainingsPanel() {
        Pattern pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        ButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Proxy.manageDelete(gate,TrainingList.getSelectedValue().toString());
                    ((RefreshablePanel)Panel).updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = SessionIdTextField.getText();
                String input2 = BodyPartIdTextField.getText();
                Matcher matcher = pattern.matcher(input);
                Matcher matcher2 = pattern.matcher(input2);
                if(!input.isEmpty() && matcher.find() && matcher2.find()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("sessionid",input);
                    map.put("bodypartid",input2);
                    Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"sessionid","bodypatrid"},map);
                    try {
                        gate.receiveRequest(req);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                try {
                    ((RefreshablePanel)Panel).updateList();
                    System.out.println("Update list!");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String input = SessionIdTextField.getText();
                    String input2 = BodyPartIdTextField.getText();
                    Matcher matcher = pattern.matcher(input);
                    Matcher matcher2 = pattern.matcher(input2);
                    if(!input.isEmpty() && matcher.find() && matcher2.find()){
                        HashMap<String,String> map = new HashMap<>();
                        map.put("sessionid",input);
                        map.put("bodypartid",input2);
                        map.put("table","trainings");
                        Proxy.manageUpdate(gate,TrainingList.getSelectedValue().toString(),map);
                    }
                    ((RefreshablePanel)Panel).updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        BuildTrainingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        TrainingBuild.start((RefreshablePanel) Panel);
                    }
                });
            }
        });
    }
    private void createUIComponents() throws Exception {
        Panel = new RefreshablePanel() {
            @Override
            public void updateList() throws Exception {
                HashMap<String,String> map = new HashMap<>();
                map.put("id","0");
                map.put("sessionid","0");
                map.put("bodypartid","0");
                gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
                TrainingList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
                gate.disconnect();
            }
        };
        JScrollPaneList = new JScrollPane();
        gate = new BackGate("trainings");
        TrainingList = new JList();
        ((RefreshablePanel)Panel).updateList();
        this.JScrollPaneList.add(TrainingList);
        gate.disconnect();
    }
}
