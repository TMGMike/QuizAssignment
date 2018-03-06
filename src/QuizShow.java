import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class QuizShow {
    // GUI Components
    private JPanel mainPnl;
    private JTextField textField1;
    private JButton addPlyrBtn;
    private JButton rmvPlyrBtn;
    private JList playerList; //TODO Rename this so it isn't similar to PlayerList players
    private JButton continueBtn;
    private JButton playGameBtn;
    private JButton exitBtn;
    private JButton settingsBtn;
    private JButton questionEditor;
    private JPanel playersPnl;
    private JPanel playerListPnl;
    private JPanel menuPnl;

    // Set how many questions must be answered in order to win. This helps in asking in order of difficulty.
    // A question's difficulty level should be between 1 and this value.
    private final int MAX_QUESTIONS = 10;

    // Player models
    PlayerList players;
    private DefaultListModel<String> playerModel = new DefaultListModel<>();

    // Question properties
    private QuestionList generalQuestions = new QuestionList();
    private QuestionList technologyQuestions = new QuestionList();
    private QuestionList entertainQuestions = new QuestionList();
    private QuestionList historyQuestions = new QuestionList();

    // Define the list of questions that should be asked to the user. This will be used later.
    private QuestionList questionList = new QuestionList();

    // Properties from the settings window. These are used within the game.
    private boolean useSoundEffects = true;
    private int soundEffectVolume = 25;

    // Create the setting frame and assign its properties.
    private SettingsGUI settingsGUI = new SettingsGUI(this);

    public QuizShow() {
        // Creates the player list and sets the model.
        players = new PlayerList();
        playerList.setModel(players);

        addInitialQuestions();

        addPlyrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new player with a random ID, always with same number of digits.
                Random r = new Random();
                int Low = 11111111;
                int High = 99999999;
                int Result = r.nextInt(High-Low) + Low;
                players.addPlayer(Result, textField1.getText(), 0, true);

            }
        });
        rmvPlyrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = playerList.getSelectedIndex();
                if(selected == -1) {
                    JOptionPane.showMessageDialog(mainPnl, "No player was selected.");
                }else {
                    Player toRemove = players.getElementAt(selected);

                    if(JOptionPane.showConfirmDialog(mainPnl, "Are you sure you wish to delete " + toRemove +
                            "?", "Confirm Removal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        players.removePlayer(toRemove.getId());
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

                int currentStage = 0;
                while(currentStage <= MAX_QUESTIONS){
                    Question newQuestion = generalQuestions.chooseQuestion(MAX_QUESTIONS, currentStage);
                    if(newQuestion != null){
                        System.out.println(newQuestion + "\n");
                        questionList.addQuestion(currentStage, newQuestion.getQuestion(), newQuestion.getCorrectAnswer(),
                                newQuestion.getWrongAnswers(), newQuestion.getDifficultyLevel());
                    }
                    currentStage++;
                }

            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        settingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsGUI.setVisisble(true);
            }
        });
        playGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QuestionGUI questionGUI = new QuestionGUI(QuizShow.this, questionList);
                questionGUI.setVisisble(true);

            }
        });
    }

    private void addInitialQuestions() {
        String[] q1Incorrect =  new String[]{"Dublin", "Huddersfield", "Manchester"};
        generalQuestions.addQuestion(0, "What is the capital city of England?",
                "London", q1Incorrect, 1 );

        String[] q2Incorrect =  new String[]{"29th December", "30th December", "1st January"};
        generalQuestions.addQuestion(1, "On which day is New Years' Eve?",
                "31st December", q2Incorrect, 1 );

        String[] q3Incorrect =  new String[]{"Australian Dollhair", "Australian Dollop", "Australian Pound"};
        generalQuestions.addQuestion(2, "What is the currency used in Australia?",
                "Australian Dollar", q3Incorrect, 1);

        String[] q4Incorrect =  new String[]{"Gordon Ramsay", "Barack Obama", "Theresa May"};
        generalQuestions.addQuestion(3, "<html>Who became famous for the quote: \"You can always " +
                        "count on <br>Americans to do the right thing - after they've tried everything else.\"?</html>",
                "Winston Churchill", q4Incorrect, 2);

        String[] q5Incorrect =  new String[]{"Sky, weather, thunder, and lightning",
                "Fire, metalworking, and crafts", "War, bloodshed, and violence"};
        generalQuestions.addQuestion(4, "Poseidon is a Greek God. What was he the God of?",
                "Sea, rivers, floods, droughts, and earthquakes", q5Incorrect, 2 );

        String[] q6Incorrect =  new String[]{"Influenza", "Yellow fever", "Leprosy"};
        generalQuestions.addQuestion(5, "Which virus causes bleeding in the body due to the " +
                        "destruction of blood vessels?",
                "Ebola", q6Incorrect, 2 );

        String[] q7Incorrect =  new String[]{"Earth's Moon", "Europa", "Deimos"};
        generalQuestions.addQuestion(6, "Which is the largest Moon in the solar system?",
                "Ganymede", q7Incorrect, 3 );

        String[] q8Incorrect =  new String[]{"Isaac Newton", "Theresa May", "Winston Churchill"};
        generalQuestions.addQuestion(7, "<html>Which famous person was known for the following quote:" +
                        "<br>\"The difference between stupidity and genius is that genius has its limits\"?</html>",
                "Albert Einstein", q8Incorrect, 3 );

        String[] q9Incorrect =  new String[]{"France", "Germany", "Sweden"};
        generalQuestions.addQuestion(8, "Which country’s airline is KLM?",
                "Holland", q9Incorrect, 3 );

        /* String[] q10Incorrect =  new String[]{"Dublin", "Huddersfield", "Manchester"};
        generalQuestions.addQuestion(1, "What is the capital city of England?",
                "London", q10Incorrect, 4, 100 );

        String[] q11Incorrect =  new String[]{"Dublin", "Huddersfield", "Manchester"};
        generalQuestions.addQuestion(1, "What is the capital city of England?",
                "London", q11Incorrect, 4, 100 );

        String[] q12Incorrect =  new String[]{"Dublin", "Huddersfield", "Manchester"};
        generalQuestions.addQuestion(1, "What is the capital city of England?",
                "London", q12Incorrect, 4, 100 ); */
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

    public boolean isUseSoundEffects() {
        return useSoundEffects;
    }

    public void setUseSoundEffects(boolean useSoundEffects) {
        this.useSoundEffects = useSoundEffects;
        if(useSoundEffects) {
            System.out.println("Sound effects are now enabled.");
        }else{
            System.out.println("Sound effects are now disabled.");
        }
    }

    public int getSoundEffectVolume() {
        return soundEffectVolume;
    }

    public void setSoundEffectVolume(int soundEffectVolume) {
        this.soundEffectVolume = soundEffectVolume;
        System.out.println("Sound effects volume is now: " + soundEffectVolume + "%");
    }
    public QuestionList getGeneralQuestions() {
        return generalQuestions;
    }

    public QuestionList getTechnologyQuestions() {
        return technologyQuestions;
    }

    public QuestionList getEntertainQuestions() {
        return entertainQuestions;
    }

    public QuestionList getHistoryQuestions() {
        return historyQuestions;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("QuizShow");
        frame.setContentPane(new QuizShow().mainPnl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
