package pwr.student.FitnessTracker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

public class TrainingBuild extends JFrame {
    BackGate gateExercises;
    BackGate gateBodyParts;
    BackGate gateTrainings;

    private JPanel Panel;
    private JList ExercisesList;
    private JScrollPane ScrollPane;
    private JButton Add;
    private JList SelectedExercises;
    private JButton CreateTrainingButton;
    private JList BodyPatrsList;
    private JScrollPane ScrollPaneBodyPatrs;
    private DefaultListModel selectedExercisesModel;

    public TrainingBuild() {
        ExercisesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String toAdd = ExercisesList.getSelectedValue().toString();
                if(!selectedExercisesModel.contains(toAdd))
                    selectedExercisesModel.addElement(toAdd);
                SelectedExercises.setModel(selectedExercisesModel);
            }
        });
        SelectedExercises.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(SelectedExercises.getSelectedValue()!=null){
                    String toDelete = SelectedExercises.getSelectedValue().toString();
                    selectedExercisesModel.removeElement(toDelete);
                    SelectedExercises.setModel(selectedExercisesModel);
                }
            }
        });
        CreateTrainingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(BodyPatrsList.getSelectedValue()!=null){
                    if(selectedExercisesModel.toArray().length>0) {
                        HashMap<String, String> map = new HashMap<>();
                        String idBodyPart = Proxy.findPart(BodyPatrsList.getSelectedValue().toString(),"id");
                        map.put("sessionid", "-1");
                        map.put("bodypartid", idBodyPart);
                        Request req = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"sessionid","bodypatrid"},map);
                        try {
                            gateTrainings.receiveRequest(req);
                            map.clear();
                            map.put("sql","SELECT max(id) as id FROM trainings");
                            Proxy.manageSpecialOperation(gateTrainings,"",map);
                            Integer trainingId = gateTrainings.getRespond().getResult().getInt("id");
                            gateTrainings.disconnect();
                            Arrays.stream(selectedExercisesModel.toArray()).forEach(s -> {
                                map.clear();
                                map.put("repeats",Proxy.findPart(s.toString(),"repeats"));
                                map.put("load",Proxy.findPart(s.toString(),"load"));
                                map.put("exercisetypeid",Proxy.findPart(s.toString(),"id"));
                                map.put("trainingid",trainingId.toString());
                                Request tmpReq = RequestBuilder.buildRequest(Operation.INSERT,new String[]{"repeats","load","exercisetypeid","trainingid"},map);
                                try {
                                    gateExercises.receiveRequest(tmpReq);
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else InfoBox.show("Please select at least one exercise","No exercise selected");
                }else InfoBox.show("Please select body part","No body part selected");

            }
        });
    }

    public static void start() {
        JFrame frame = new JFrame("TrainingBuild");
        frame.setContentPane(new TrainingBuild().Panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() throws Exception {
        gateExercises = new BackGate("exercises");
        gateBodyParts = new BackGate("bodyparts");
        gateTrainings = new BackGate("trainings");
        ExercisesList = new JList();
        ExercisesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        SelectedExercises = new JList();
        selectedExercisesModel = new DefaultListModel();
        SelectedExercises.setModel(selectedExercisesModel);
        ScrollPane = new JScrollPane();
        ScrollPane.add(ExercisesList);

        BodyPatrsList = new JList<>();
        BodyPatrsList.setModel(new DefaultListModel());
        ScrollPaneBodyPatrs = new JScrollPane();
        ScrollPaneBodyPatrs.add(BodyPatrsList);
        updateList();

    }
    private void updateList() throws Exception {
        HashMap<String,String> map = new HashMap<>();
        map.put("repeats","10");
        map.put("load","15");
        map.put("name","0");
        map.put("sql","SELECT load,repeats,name,exercises.id FROM exercises JOIN exercisestypes ON exercises.exercisetypeid = exercisestypes.id GROUP BY name,load,repeats");//
        Proxy.manageSpecialOperation(gateExercises,"",map);
        ExercisesList.setModel(JListBuilder.buildDFModel(map.keySet(),gateExercises.getRespond()));
        gateExercises.disconnect();

        map.clear();
        map.put("id","0");
        map.put("name","0");
        gateBodyParts.receiveRequest(RequestBuilder.buildRequest(Operation.SELECT,new String[]{"*"},map));
        BodyPatrsList.setModel(JListBuilder.buildDFModel(map.keySet(), gateBodyParts.getRespond()));
        gateBodyParts.disconnect();
    }
}
