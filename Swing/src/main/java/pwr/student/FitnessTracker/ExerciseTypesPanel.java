package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExerciseTypesPanel extends RefreshablePanel{
    BackGate gate;
    private JButton ButtonAdd;
    private JButton ButtonDelete;
    private JPanel Panel;
    private JList ExerciseTypes;
    private JTextField TextFieldName;
    private JButton ButtonUpdate;
    private JScrollPane ScrollPane;

    public ExerciseTypesPanel() {
        Pattern pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
        ButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = TextFieldName.getText();
                Matcher matcher = pattern.matcher(input);
                if(!input.isEmpty() && !matcher.find()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("name",input);
                    Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"name"},map);
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
                    Proxy.manageDelete(gate,ExerciseTypes.getSelectedValue().toString());
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
                    String input = TextFieldName.getText();
                    Matcher matcher = pattern.matcher(input);
                    if(!input.isEmpty() && !matcher.find()){
                        HashMap<String,String> map = new HashMap<>();
                        map.put("name",input);
                        map.put("table","exercisestypes");
                        Proxy.manageUpdate(gate,ExerciseTypes.getSelectedValue().toString(),map);
                    }
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void createUIComponents() throws Exception {
        Panel = new RefreshablePanel() {
            @Override
            public void updateList() throws Exception {
                HashMap<String,String> map = new HashMap<>();
                map.put("id","0");
                map.put("name","0");
                gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
                ExerciseTypes.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
            }
        };

        gate = new BackGate("exercisestypes");
        ExerciseTypes = new JList();
        updateList();
        ScrollPane = new JScrollPane();
        this.ScrollPane.add(ExerciseTypes);
    }
    public void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("name","0");
        gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        ExerciseTypes.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
    }
}
