package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomJPanel extends JPanel {
    JFrame frame = new JFrame();
    public Integer tmpcounter = 0;
    public CustomJPanel(){
        JLabel JLabelCounter = new JLabel();
        Button ButtonAdd = new Button();
        ButtonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tmpcounter++;
                String src = Integer.parseInt(JLabelCounter.getText())+tmpcounter.toString();
                JLabelCounter.setText(src);
            }
        });
        ButtonAdd.setLabel("ADD");
        frame.getContentPane().add(JLabelCounter);
        frame.getContentPane().add(ButtonAdd);
        this.add(ButtonAdd);
        this.add(JLabelCounter);
        frame.setContentPane(this);
    }
}
