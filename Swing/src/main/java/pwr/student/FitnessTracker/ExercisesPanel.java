package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExercisesPanel {
    BackGate gate;
    private JPanel Panel;
    private JList ExercisesList;
    private JButton ButtonDelete;
    private JButton ButtonAdd;
    private JTextField RepeatTextField;
    private JTextField LoadTextField;
    private JTextField ExercisetypeIdTextField;
    private JTextField TrainingIdTextField;
    private JScrollPane ScrollPane;
    private JButton ButttonUpdate;

    public ExercisesPanel() {
        ButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Proxy.manageDelete(gate,ExercisesList.getSelectedValue().toString());
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonAdd.addActionListener(new ActionListener() {
            Pattern pattern = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);

            @Override
            public void actionPerformed(ActionEvent e) {
                String repeats = RepeatTextField.getText();
                String load = RepeatTextField.getText();
                String exID = ExercisetypeIdTextField.getText();
                String trainingID = TrainingIdTextField.getText();
                Matcher matcher = pattern.matcher(repeats);
                Matcher matcher2 = pattern.matcher(load);
                Matcher matcher3 = pattern.matcher(exID);
                Matcher matcher4 = pattern.matcher(trainingID);
                if(matcher.find()&& matcher2.find()&&matcher3.find()&&matcher4.find()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("repeats",repeats);
                    map.put("load",load);
                    map.put("exercisetypeid",exID);
                    map.put("trainingid",trainingID);
                    Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"repeats","load","exercisetypeid","trainingid"},map);
                    try {
                        gate.receiveRequest(req);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                try {
                    updateList();
                    System.out.println("Update list!");
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Pattern pattern = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
                    String repeats = RepeatTextField.getText();
                    String load = RepeatTextField.getText();
                    String exID = ExercisetypeIdTextField.getText();
                    String trainingID = TrainingIdTextField.getText();
                    Matcher matcher = pattern.matcher(repeats);
                    Matcher matcher2 = pattern.matcher(load);
                    Matcher matcher3 = pattern.matcher(exID);
                    Matcher matcher4 = pattern.matcher(trainingID);
                    if(matcher.find()&& matcher2.find()&&matcher3.find()&&matcher4.find()){
                        HashMap<String,String> map = new HashMap<>();
                        map.put("repeats",repeats);
                        map.put("load",load);
                        map.put("exercisetypeid",exID);
                        map.put("trainingid",trainingID);
                        map.put("table","exercises");
                        Proxy.manageUpdate(gate,ExercisesList.getSelectedValue().toString(),map);
                    }
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void createUIComponents() throws Exception {
        ScrollPane = new JScrollPane();
        gate = new BackGate("exercises");
        ExercisesList = new JList();
        updateList();
        this.ScrollPane.add(ExercisesList);
    }
    private void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("exercisetypeid","2");
        map.put("id","0");
        map.put("trainingid","1");
        map.put("repeats","10");
        map.put("load","15");
        gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        ExercisesList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
    }
}
