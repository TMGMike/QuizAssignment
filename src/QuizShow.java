import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class QuizShow {
    private JPanel mainPnl;
    private JTextField textField1;
    private JButton addPlyrBtn;
    private JButton rmvPlyrBtn;
    private JList playerList; //TODO Rename this so it isn't similar to PlayerList playersList
    private JButton continueBtn;
    private JButton playGameBtn;
    private JButton exitBtn;
    private JButton settingsBtn;
    private JButton questionEditor;
    private JPanel playersPnl;
    private JPanel playerListPnl;
    private JPanel menuPnl;
    PlayerList playersList;
    private DefaultListModel<String> playerModel = new DefaultListModel<>();
    public QuizShow() {
        playersList = new PlayerList();
        playerList.setModel(playersList);
        addPlyrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new player with a random ID, always with same number of digits.
                Random r = new Random();
                int Low = 11111111;
                int High = 99999999;
                int Result = r.nextInt(High-Low) + Low;
                playersList.addPlayer(Result, textField1.getText(), 0, true);

            }
        });
        rmvPlyrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = playerList.getSelectedIndex();
                if(selected == -1) {
                    JOptionPane.showMessageDialog(mainPnl, "No player was selected.");
                }else {
                    Player toRemove = playersList.getElementAt(selected);

                    if(JOptionPane.showConfirmDialog(mainPnl, "Are you sure you wish to delete " + toRemove + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        playersList.removePlayer(toRemove.getId());
                        playerList.clearSelection();
                    }
                }
            }
        });
        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playersPnl.setVisible(false);
                playerListPnl.setVisible(false);
                menuPnl.setVisible(true);
            }
        });
    }


    public int getPlayerID (String name) {
        for(int i = -1; i < playerModel.getSize(); i++){
            i++;
            if(playerModel.getElementAt(i).equals(name)){
                return i;
            }

        }
        return -1;

    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("QuizShow");
        frame.setContentPane(new QuizShow().mainPnl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }
}
