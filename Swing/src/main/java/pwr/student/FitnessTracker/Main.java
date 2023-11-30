package pwr.student.FitnessTracker;

public class Main {
    public static void main(String[] args) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        BackGate backGateToTrainings = new BackGate("trainings");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    MainForm mainForm = new MainForm();
                    mainForm.run();
                }
            });
        }
}