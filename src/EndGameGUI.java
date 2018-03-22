import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class EndGameGUI {
    private JPanel mainPnl;
    private JPanel introGIFPnl;
    private JPanel playerAwardsPnl;
    private JLabel q1ValueLbl;
    private JLabel q2ValueLbl;
    private JLabel q3ValueLbl;
    private JLabel q4ValueLbl;
    private JLabel q5ValueLbl;
    private JLabel audienceValueLbl;
    private JLabel halfHalfValueLbl;
    private JLabel secondLifeValueLbl;
    private JLabel totalValueLbl;
    private JLabel introGIFLbl;
    private JLabel playerNameLbl;
    private JLabel winnerLbl;
    private JPanel winnerPnl;
    private JFrame frame;
    private Timer animTimer;
    private Timer playerTimer;
    private QuestionGUI questionGUI;
    private PlayerList players;
    private int currentPlayerIndex = 0;
    private Player currentWinner;
    private int highest = 0;
    public EndGameGUI(QuestionGUI questionUI, PlayerList players) {
        this.questionGUI = questionUI;

        frame = new JFrame("End Game");
        frame.setContentPane(this.mainPnl);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(720, 480);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        this.players = players;

    }
    public void setVisible(boolean visibility){
        frame.setVisible(visibility);
        animTimer = new Timer();
        animTimer.scheduleAtFixedRate(animTask, 5000, 5000);
        introGIFLbl.setIcon(new ImageIcon(getClass().getResource("anim.gif")));
    }
    TimerTask animTask = new TimerTask() {
        @Override
        public void run() {
            introGIFPnl.setVisible(false);
            playerAwardsPnl.setVisible(true);
            playerTimer = new Timer();
            playerLoopTask.run();
            playerTimer.scheduleAtFixedRate(playerLoopTask, 6000, 6000);
            animTimer.cancel();
        }
    };

    // When the timer starts, it should loop through all players and display each player's prize.
    TimerTask playerLoopTask = new TimerTask() {
        @Override
        public void run() {
            if(currentPlayerIndex < players.size()){
                Player current = players.getElementAt(currentPlayerIndex);

                playerNameLbl.setText(current.getName());

                QuestionList answered = current.getAnsweredQuestions();
                int grandTotal = 0;
                try {
                    q1ValueLbl.setText("£" + answered.getElementAt(0).getRewarded());
                    grandTotal += answered.getElementAt(0).getRewarded();
                    q2ValueLbl.setText("£" + answered.getElementAt(1).getRewarded());
                    grandTotal += answered.getElementAt(1).getRewarded();
                    q3ValueLbl.setText("£" + answered.getElementAt(2).getRewarded());
                    grandTotal += answered.getElementAt(2).getRewarded();
                    q4ValueLbl.setText("£" + answered.getElementAt(3).getRewarded());
                    grandTotal += answered.getElementAt(3).getRewarded();
                    q5ValueLbl.setText("£" + answered.getElementAt(4).getRewarded());
                    grandTotal += answered.getElementAt(4).getRewarded();

                }catch (Exception ignore){

                }


                if(!current.getHalfHalfAvailable()){
                    halfHalfValueLbl.setText("£-30");
                    halfHalfValueLbl.setForeground(Color.red);
                    grandTotal -= 30;
                }
                else {
                    grandTotal += 30;
                    halfHalfValueLbl.setText("£30");
                    halfHalfValueLbl.setForeground(new Color(246,246,246));
                }
                if(!current.getPublicAvailable()){
                    audienceValueLbl.setText("£-50");
                    audienceValueLbl.setForeground(Color.red);
                    grandTotal -= 50;
                }else{
                    grandTotal += 50;
                    audienceValueLbl.setText("£50");
                    audienceValueLbl.setForeground(new Color(246,246,246));
                }
                if(!current.getSecondLifeAvailable()){
                    secondLifeValueLbl.setText("£-50");
                    secondLifeValueLbl.setForeground(Color.red);
                    grandTotal -= 50;
                }else {
                    grandTotal += 50;
                    secondLifeValueLbl.setText("£50");
                    secondLifeValueLbl.setForeground(new Color(246,246,246));
                }
                if(!current.getCanPlay()) {
                    grandTotal = 0;
                }

                totalValueLbl.setText("£" + grandTotal);
                if(grandTotal >= highest){
                    currentWinner = current;
                }
                currentPlayerIndex++;
            }
            else {
                winnerLbl.setText(currentWinner.getName().toUpperCase() + " WINS!");
                playerAwardsPnl.setVisible(false);
                winnerPnl.setVisible(true);
                playerTimer.cancel();
            }
        }
    };
}
