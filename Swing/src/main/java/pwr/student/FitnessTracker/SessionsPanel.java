package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SessionsPanel extends RefreshablePanel{
    BackGate gate;
    private JPanel Panel;
    private JButton ButtonDelete;
    private JList SessionList;
    private JButton ButtonAdd;
    private JTextField TimeTextField;
    private JTextField DateTextField;
    private JScrollPane ScrollPane;
    private JButton ButtonUpdate;
    private JButton BuildSession;

    public SessionsPanel() {
        ButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Proxy.manageDelete(gate,SessionList.getSelectedValue().toString());
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ButtonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(((MyDateField)DateTextField).isProperDate() && ((MyTimeField)TimeTextField).isProperTime()){
                    String time = TimeTextField.getText();
                    String date = DateTextField.getText();
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
        ButtonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(((MyDateField)DateTextField).isProperDate() && ((MyTimeField)TimeTextField).isProperTime()) {
                        String time = TimeTextField.getText();
                        String date = DateTextField.getText();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("table","sessions");
                        map.put("time", time);
                        map.put("date", date);
                        Proxy.manageUpdate(gate,SessionList.getSelectedValue().toString(),map);
                    }
                    updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        BuildSession.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            SQLExecutor.createNewDatabase("base");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        SessionBuild.start();
                    }
                });
            }
        });
    }

    private void createUIComponents() throws Exception {
        ScrollPane = new JScrollPane();
        DateTextField = new MyDateField();
        TimeTextField = new MyTimeField();
        gate = new BackGate("sessions");
        SessionList = new JList();
        updateList();
        this.ScrollPane.add(SessionList);
    }
    public void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("id","0");
        map.put("date","1");
        map.put("time","10");
        gate.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        SessionList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
    }
}
