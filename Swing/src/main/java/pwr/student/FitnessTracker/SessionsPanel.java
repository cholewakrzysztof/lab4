package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SessionsPanel {
    BackGate gate;
    private JPanel Panel;
    private JButton ButtonDelete;
    private JList SessionList;
    private JButton ButtonAdd;
    private JTextField TimeTextField;
    private JTextField DateTextField;
    private JScrollPane ScrollPane;

    public SessionsPanel() {
        ButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HashMap<String,String> map =new HashMap<>();
                System.out.println(SessionList.getSelectedIndex());
                if(SessionList.getSelectedIndex()>-1){
                    Pattern pattern = Pattern.compile("[0-9]+");
                    String item = SessionList.getSelectedValue().toString();
                    Matcher regex = pattern.matcher(item);
                    if(regex.find()){
                        String id = regex.group(0);
                        map.put("start",id);
                        map.put("end",id);
                    }
                    Request req = RequestBuilder.buildRequest(Operation.DELETE,new String[]{}, map);
                    try {
                        gate.receiveRequest(req);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                try {
                    System.out.println("Update list!");
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern pattern = Pattern.compile("[0-9]", Pattern.CASE_INSENSITIVE);
                String time = TimeTextField.getText();
                String date = DateTextField.getText();
                Matcher matcher = pattern.matcher(time);
                Matcher matcher2 = pattern.matcher(date);
                if(matcher.find()&& matcher2.find()){
                    HashMap<String,String> map = new HashMap<>();
                    map.put("time",time);
                    map.put("date",date);
                    Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"time","date"},map);
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
    }

    private void createUIComponents() throws Exception {
        ScrollPane = new JScrollPane();
        gate = new BackGate("sessions");
        SessionList = new JList();
        updateList();
        this.ScrollPane.add(SessionList);
    }
    private void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("date","1");
        map.put("time","10");
        gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        SessionList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
    }
}
