package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetsPanel{
    BackGate gate;
    private JPanel Panel;
    private JList TargetsList;
    private JButton ButtonDelete;
    private JButton ButtonAdd;
    private JTextField ExerciseIdTextField;
    private JTextField DateTextField;
    private JScrollPane ScrollPane;
    private JButton ButtonUpdate;
    private JButton BuildTargetButton;
    private JButton updateTargetsButton;
    private JButton showProgressButton;

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
                    ((RefreshablePanel)Panel).updateList();
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
                    ((RefreshablePanel)Panel).updateList();
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
                    ((RefreshablePanel)Panel).updateList();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        BuildTargetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        TargetBuild.start((RefreshablePanel) Panel);
                    }
                });
            }
        });
        updateTargetsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i =0; i<TargetsList.getModel().getSize(); i++){
                    String targetId = Proxy.findPart(TargetsList.getModel().getElementAt(i).toString(),"id");
                    String exId = Proxy.findPart(TargetsList.getModel().getElementAt(i).toString(),"exercisetypeid");
                    String tRepeats = Proxy.findPart(TargetsList.getModel().getElementAt(i).toString(),"repeats");
                    String tLoad = Proxy.findPart(TargetsList.getModel().getElementAt(i).toString(),"load");
                    try {
                        BackGate tmpGate = new BackGate("exercises");
                        HashMap<String,String> map = new HashMap<>();
                        map.put("repeats","10");
                        map.put("load","15");
                        map.put("sql","SELECT * FROM exercises WHERE exercisetypeid="+exId+" AND load>="+tLoad+" AND repeats>="+tRepeats+" AND trainingid != -1");//
                        Proxy.manageSpecialOperation(tmpGate,"id: "+exId,map);
                        tmpGate.disconnect();
                        if(tmpGate.getRespond().getResult().next()){ //
                            HashMap<String,String> map1 = new HashMap<>();
                            map1.put("success","1");
                            map1.put("table","targets");
                            Proxy.manageUpdate(gate,"id: "+targetId,map1);
                            ((RefreshablePanel)Panel).updateList();
                        }
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        showProgressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(TargetsList.getSelectedValue()!=null){
                    String exerciseId = Proxy.findPart(TargetsList.getSelectedValue().toString(),"exercisetypeid");
                    String targetLoad = Proxy.findPart(TargetsList.getSelectedValue().toString(),"load");
                    String targetRepeats = Proxy.findPart(TargetsList.getSelectedValue().toString(),"repeats");
                    javax.swing.SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                ProgressPanel.start(exerciseId,targetLoad,false);
                                ProgressPanel.start(exerciseId,targetRepeats,true);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }else InfoBox.show("Select target","No target selected");

                }
        });
    }

    private void createUIComponents() throws Exception {
        Panel = new RefreshablePanel() {
            @Override
            public void updateList() throws Exception {
                HashMap<String,String> map = new HashMap<>();
                map.put("id","0");
                map.put("exerciseid","0");
                map.put("date","0");
                map.put("success","0");
                map.put("exercisetypeid","0");
                map.put("load","0");
                map.put("repeats","0");
                map.put("sql","SELECT * FROM targets JOIN exercises ON targets.exerciseid=exercises.id");
                Proxy.manageSpecialOperation(gate,"",map);
                TargetsList.setModel(JListBuilder.buildDFModel(map.keySet(),gate.getRespond()));
                gate.disconnect();
            }
        };

        DateTextField = new MyDateField();
        ScrollPane = new JScrollPane();
        gate = new BackGate("targets");
        TargetsList = new JList();
        ((RefreshablePanel)Panel).updateList();
        this.ScrollPane.add(TargetsList);
    }
}
