package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyPartTypesPanel extends RefreshablePanel{
    BackGate gate;
    private JButton ButtonAdd;
    private JButton ButtonDelete;
    private JPanel Panel;
    private JList BodyPartTypesList;
    private JTextField TextFieldName;
    private JButton ButtonUpdate;

    public BodyPartTypesPanel() {
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
                    Proxy.manageDelete(gate,BodyPartTypesList.getSelectedValue().toString());
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
                        map.put("table","bodyparts");
                        Proxy.manageUpdate(gate,BodyPartTypesList.getSelectedValue().toString(),map);
                    }
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void createUIComponents() throws Exception {
        gate = new BackGate("bodyparts");
        BodyPartTypesList = new JList();
        updateList();
        Panel = new JPanel();
        this.Panel.add(BodyPartTypesList);
    }
    public void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("name","0");
        gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        BodyPartTypesList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
        gate.disconnect();
    }
}
