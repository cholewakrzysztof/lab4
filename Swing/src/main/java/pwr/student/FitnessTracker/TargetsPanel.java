package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetsPanel {
    BackGate gate;
    private JPanel Panel;
    private JList TargetsList;
    private JButton ButtonDelete;
    private JButton ButtonAdd;
    private JTextField ExerciseIdTextField;
    private JTextField DateTextField;
    private JScrollPane ScrollPane;
    private JButton ButtonUpdate;

    public TargetsPanel() {
        Pattern pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        ButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = null;
                if(((MyDateField)DateTextField).isProperDate())
                    input = DateTextField.getText();
                else
                    return;
                String input2 = ExerciseIdTextField.getText();
                Matcher matcher2 = pattern.matcher(input2);
                if(matcher2.find()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("exerciseid",input2);
                    map.put("date",input);
                    map.put("success","0");
                    Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"exerciseid","date","success"},map);
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
        ButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Proxy.manageDelete(gate,TargetsList.getSelectedValue().toString());
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String input = null;
                    if(((MyDateField)DateTextField).isProperDate())
                        input = DateTextField.getText();
                    else
                        return;
                    String input2 = ExerciseIdTextField.getText();
                    Matcher matcher2 = pattern.matcher(input2);
                    if(matcher2.find()){
                        HashMap<String,String> map = new HashMap<>();
                        map.put("exerciseid",input2);
                        map.put("date",input);
                        map.put("success","0");
                        map.put("table","targets");
                        Proxy.manageUpdate(gate,TargetsList.getSelectedValue().toString(),map);
                    }
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    private void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("exerciseid","0");
        map.put("date","0");
        map.put("success","0");
        gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        TargetsList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
    }

    private void createUIComponents() throws Exception {
        DateTextField = new MyDateField();
        ScrollPane = new JScrollPane();
        gate = new BackGate("targets");
        TargetsList = new JList();
        updateList();
        this.ScrollPane.add(TargetsList);
    }
}
