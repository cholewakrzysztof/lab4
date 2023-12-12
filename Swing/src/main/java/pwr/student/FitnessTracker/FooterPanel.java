package pwr.student.FitnessTracker;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FooterPanel extends JPanel {
    private JButton ButtonQuit;
    private JPanel Panel;

    public FooterPanel() {
        ButtonQuit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.exit(0);
            }
        });
    }
}
