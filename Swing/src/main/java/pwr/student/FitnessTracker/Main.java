package pwr.student.FitnessTracker;

public class Main {
    public static void main(String[] args) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        SQLExecutor.createNewDatabase("base");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    MainForm mainForm = new MainForm();
                    mainForm.run();
                }
            });
        }
}