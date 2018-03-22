import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeListener;

public class SettingsGUI {
    private JPanel mainPnl;
    private JPanel topPnl;
    private JPanel leftPnl;
    private JPanel rightPnl;
    private JPanel bottomPnl;
    private JPanel centrePnl;
    private JCheckBox effectsCheck;
    private JSlider effectSlider;
    private JLabel soundVolumeLbl;
    private JButton confirmBtn;
    private JButton cancelBtn;
    private JFrame frame;


    public SettingsGUI(QuizShow main) {

        frame = new JFrame("SettingsGUI");
        frame.setContentPane(this.mainPnl);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);

        // Sets the default values for the volume and sound effect check box.
        soundVolumeLbl.setText(effectSlider.getValue() + "%");
        effectsCheck.setSelected(true);

        effectSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                soundVolumeLbl.setText(effectSlider.getValue() + "%");
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisisble(false);
            }
        });

        // Controls what happens when the confirm button is pressed.
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Passes the user's choices back to the main form.
                main.setSoundEffectVolume(effectSlider.getValue());
                main.setUseSoundEffects(effectsCheck.isSelected());
                setVisisble(false);
            }
        });
    }

    public void setVisisble(boolean visible){
        frame.setVisible(visible);
    }
}
