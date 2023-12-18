package pwr.student.FitnessTracker;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ProgressPanel extends JFrame{
    private JPanel Panel;
    private BackGate gateTargets;
    private String targetId;
    private Integer targetLoad;
    public static void start(String targetId,String tLoad,boolean repeats) throws Exception {
        JFrame frame = new JFrame("ProgressPanel");
        ProgressPanel tb = new ProgressPanel();
        frame.setContentPane(tb.Panel);
        tb.targetId = targetId;
        tb.targetLoad = Integer.parseInt(tLoad);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        drawGraph(targetId,tb.targetLoad,repeats);
    }

    public static void drawGraph(String tID,Integer tLoad,boolean repeats) throws Exception {
        DrawGraph.createAndShowGui(getResultLoads(tID,repeats),tLoad,repeats);
    }

    public static List<Integer> getResultLoads(String targetId,boolean repeats) throws Exception {
        List<Integer> results = new ArrayList<>();

        BackGate gate = new BackGate("exercises");
        HashMap<String,String> map = new HashMap<>();
        map.put("sql","SELECT * FROM exercises WHERE exercisetypeid="+targetId+" AND trainingid != -1 ORDER BY trainingid");
        Proxy.manageSpecialOperation(gate,"",map);
        ResultSet rs = gate.getRespond().getResult();
        while (rs.next()){
            if(repeats)
                results.add(rs.getInt("repeats"));
            else
                results.add(rs.getInt("load"));
        }
        gate.disconnect();
        return results;
    }
}
