package pwr.student.FitnessTracker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Ref;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;

public class SessionBuild extends JFrame{
    private BackGate gateTrainings;
    private BackGate gateSessions;
    private DefaultListModel selectedTrainingsModel;
    private JList SelectedTrainings;
    private JList TrainingList;
    private JTextField DateTextField;
    private JTextField TimeTextField;
    private JButton ButtonCreate;
    private JScrollPane ScrollPaneSelected;
    private JScrollPane ScrollPaneList;
    private JPanel Panel;
    private RefreshablePanel panel;

    public SessionBuild() {
        TrainingList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String toAdd = TrainingList.getSelectedValue().toString();
                if(!selectedTrainingsModel.contains(toAdd))
                    selectedTrainingsModel.addElement(toAdd);
                SelectedTrainings.setModel(selectedTrainingsModel);
            }
        });
        SelectedTrainings.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(SelectedTrainings.getSelectedValue()!=null){
                    String toDelete = SelectedTrainings.getSelectedValue().toString();
                    selectedTrainingsModel.removeElement(toDelete);
                    SelectedTrainings.setModel(selectedTrainingsModel);
                }
            }
        });
        ButtonCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(((MyDateField)DateTextField).isProperDate()){
                    if(((MyTimeField)TimeTextField).isProperTime()){
                        HashMap<String, String> map = new HashMap<>();
                        map.put("date", DateTextField.getText());
                        map.put("time", TimeTextField.getText());
                        Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"date","time"},map);
                        try {
                            gateSessions.receiveRequest(req);
                            map.clear();
                            map.put("sql","SELECT max(id) as id FROM sessions");
                            Proxy.manageSpecialOperation(gateSessions,"",map);
                            Integer sessionId = gateSessions.getRespond().getResult().getInt("id");
                            gateSessions.disconnect();
                            Arrays.stream(selectedTrainingsModel.toArray()).forEach(s -> {
                                map.clear();
                                String idTraining = Proxy.findPart(s.toString(), "id");
                                System.out.println("Should add exercises related to"+idTraining);
                                map.put("bodypartid", Proxy.findPart(s.toString(), "bodypartid"));
                                map.put("sessionid", sessionId.toString());
                                Request tmpReq = RequestBuilder.buildRequest(Operation.INSERT, new String[]{"sessionid", "bodypartid"}, map);
                                try {
                                    gateTrainings.receiveRequest(tmpReq);
                                    gateTrainings.disconnect();
                                    gateSessions.disconnect();
                                    //ADD exercises
                                    BackGate tmpExerciseGate = new BackGate("sessions");
                                    map.clear();
                                    map.put("sql","SELECT * FROM exercises WHERE trainingid = "+idTraining);
                                    Proxy.manageSpecialOperation(tmpExerciseGate,"",map);
                                    ResultSet result = tmpExerciseGate.getRespond().getResult();

                                    gateTrainings.disconnect();
                                    gateSessions.disconnect();

                                    while (result.next()){
                                        BackGate tmpTmp = new BackGate("exercises");
                                        map.clear();
                                        map.put("repeats",result.getString("repeats"));
                                        map.put("load",result.getString("load"));
                                        map.put("exercisetypeid",result.getString("exercisetypeid"));
                                        map.put("trainingid",idTraining);

                                        tmpReq = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"repeats","load","exercisetypeid","trainingid"},map);
                                        tmpTmp.receiveRequest(tmpReq);
                                        tmpTmp.disconnect();
                                    }
                                    tmpExerciseGate.disconnect();
                                    panel.updateList();
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }




                            });
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        });
    }

    public static void start(RefreshablePanel panel) {
        JFrame frame = new JFrame("SessionBuild");
        SessionBuild tb = new SessionBuild();
        frame.setContentPane(tb.Panel);
        tb.panel = panel;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("bodypartid","15");
        map.put("name","0");
        map.put("sql","SELECT bodypartid,name,trainings.id FROM trainings JOIN bodyparts ON trainings.bodypartid = bodyparts.id WHERE trainings.sessionid=-1");//
        Proxy.manageSpecialOperation(gateTrainings,"",map);
        TrainingList.setModel(JListBuilder.buildDFModel(map.keySet(),gateTrainings.getRespond()));
        gateTrainings.disconnect();
    }

    private void createUIComponents() throws Exception {
        DateTextField = new MyDateField();
        TimeTextField = new MyTimeField();

        gateTrainings = new BackGate("trainings");
        gateSessions = new BackGate("sessions");
        TrainingList = new JList();
        TrainingList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        SelectedTrainings = new JList();
        selectedTrainingsModel = new DefaultListModel();
        SelectedTrainings.setModel(selectedTrainingsModel);
        ScrollPaneSelected = new JScrollPane();
        ScrollPaneSelected.add(SelectedTrainings);
        updateList();
    }
}
