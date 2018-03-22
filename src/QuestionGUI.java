import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionGUI {
    private JPanel mainPnl;
    private JPanel northPnl;
    private JPanel eastPnl;
    private JPanel southPnl;
    private JPanel westPnl;
    private JPanel centrePnl;
    private JButton confirmChoice;
    private JToggleButton answerOption1;
    private JToggleButton answerOption2;
    private JToggleButton answerOption3;
    private JToggleButton answerOption4;
    private JLabel questionLbl;
    private JLabel playerTurnLbl;
    private JList playerListView;
    private JLabel gameOverLbl;
    private JLabel audienceLbl;
    private JLabel halfhalfLbl;
    private JLabel secondLifeLbl;
    private MediaPlayer player;
    private PlayerList playerList;
    private Player currentTurn;
    private boolean isSelectingCategory = false;
    private JToggleButton selected;
    private DeselectButtonGroup answerGroup = new DeselectButtonGroup();
    private JFrame frame;
    private boolean isSecondLifeActive = false;
    private boolean questionSelected = false;
    private int currentStage = 0; // The current Question stage. (In relation to MAX_QUESTIONS)
    private int turnNumber = 0;
    private int buttonTimerIndex = 0;
    private QuestionList listofQuestions;
    private Question currentQuestion;
    private QuizShow main;
    private QuestionList generalQuestions = new QuestionList();
    private QuestionList technologyQuestions = new QuestionList();
    private QuestionList entertainQuestions = new QuestionList();
    private QuestionList historyQuestions = new QuestionList();
    private Timer audienceTimer;
    public QuestionGUI(QuizShow main, PlayerList players) {

        frame = new JFrame("Question GUI");
        frame.setContentPane(this.mainPnl);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        questionLbl.setBackground(mainPnl.getBackground());
        questionLbl.setForeground(new Color(246,246,246));
        playerTurnLbl.setForeground(new Color(246,246,246));
        audienceLbl.setIcon(new ImageIcon(getClass().getResource("audience.png")));
        halfhalfLbl.setIcon(new ImageIcon(getClass().getResource("halfhalf.png")));
        secondLifeLbl.setIcon(new ImageIcon(getClass().getResource("secondlife.png")));

        this.main = main;
        this.listofQuestions = listofQuestions;
        this.playerList = players;
        this.currentTurn = playerList.elementAt(0);
        // Initialises the category lists.
        this.generalQuestions = this.main.getGeneralQuestions();
        this.technologyQuestions = this.main.getTechnologyQuestions();
        this.entertainQuestions = this.main.getEntertainQuestions();
        this.historyQuestions = this.main.getHistoryQuestions();

        this.playerListView.setModel(playerList);

        // Default the turn to the first player.
        playerTurnLbl.setText(currentTurn.getName() + "'s Turn: ");
        currentStage = 1;
        showCategories();

        // Add all answer option buttons to a ButtonGroup list, which will provide the radio button behaviour.
        answerGroup.add(answerOption1);
        answerGroup.add(answerOption2);
        answerGroup.add(answerOption3);
        answerGroup.add(answerOption4);

        // Sets the size of each button.
        Dimension buttonSize = new Dimension(708, 280);
        answerOption1.setPreferredSize(buttonSize);
        answerOption2.setPreferredSize(buttonSize);
        answerOption3.setPreferredSize(buttonSize);
        answerOption4.setPreferredSize(buttonSize);

        // Prevents the player from resizing too small for the buttons to format properly.
        frame.setMinimumSize(new Dimension(1652, 737));


        confirmChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(isSelectingCategory){
                    isSelectingCategory = false;
                    if(answerOption1.isSelected()){
                        generateNextQuestion(generalQuestions);
                    }
                    else if(answerOption2.isSelected()){
                        generateNextQuestion(technologyQuestions);
                    }
                    else if(answerOption3.isSelected()){
                        generateNextQuestion(entertainQuestions);
                    }
                    else {
                        generateNextQuestion(historyQuestions);
                    }
                    answerOption1.setEnabled(true);
                    answerOption2.setEnabled(true);
                    answerOption3.setEnabled(true);
                    answerOption4.setEnabled(true);
                    applyCurrentQuestion();
                }else {
                    currentQuestion.setAnswered(true);
                    currentQuestion.setAnsweredBy(currentTurn);
                    playerList.findPlayer(currentTurn.getId()).addAnsweredQuestion(currentQuestion);


                    if(selected != null && selected.getText().contains(currentQuestion.getCorrectAnswer())){


                        // A dummy panel from JFX had to be created to have it initialise the MediaPlayer from JFX.
                        if(main.isUseSoundEffects()){
                            JFXPanel dummyPnl = new JFXPanel();
                            Media media = new Media(new File(getRandomSoundString(true)).toURI().toString());
                            player = new MediaPlayer(media);
                            player.setVolume((double) QuestionGUI.this.main.getSoundEffectVolume() / 100);
                            player.play();
                        }else {
                            System.out.println("Sounds are disabled.");
                        }

                        currentTurn.getAnsweredQuestions().getQuestion(currentQuestion.getId()).
                                setRewarded(currentQuestion.getMoneyAwarded());

                        System.out.println("Correct!");
                        // currentStage++;



                        currentTurn.addMoney(currentQuestion.getMoneyAwarded());
                        playerListView.updateUI();
                        isSecondLifeActive = false;
                        LabelRunner runner = new LabelRunner();
                        Thread t = new Thread(runner);
                        runner.setLabel(questionLbl);
                        t.start();

                    }
                    else {
                        if(isSecondLifeActive){
                            selected.setEnabled(false);
                            selected.setText("2nd Life!");
                        }
                        else {
                            currentTurn.setCanPlay(false); // Stop the player from being able to participate. They are "out".
                            currentTurn.resetMoney();
                            currentTurn.getAnsweredQuestions().getQuestion(currentQuestion.getId()).setRewarded(0);
                            playerListView.updateUI();
                            if(main.isUseSoundEffects()){
                                // This is a dummy panel to initialise JavaFX, to allow sounds to play.
                                JFXPanel dummyPnl = new JFXPanel();

                                Media media = new Media(new File(getRandomSoundString(false))
                                        .toURI().toString());

                                player = new MediaPlayer(media);
                                player.setVolume((double) QuestionGUI.this.main.getSoundEffectVolume() / 100);
                                System.out.println("Volume: "+ player.getVolume());
                                player.play();

                            }
                        }
                    }

                    if(isSecondLifeActive){
                        isSecondLifeActive = false;
                    } else {

                        updateNextPlayer();
                        isSelectingCategory = true;
                        // Reset the "board" - re-enable any buttons that were previously disabled (from half-half)
                        answerOption1.setEnabled(true);
                        answerOption2.setEnabled(true);
                        answerOption3.setEnabled(true);
                        answerOption4.setEnabled(true);

                        showCategories();
                    }

                }
                answerOption1.setSelected(false);
                answerOption2.setSelected(false);
                answerOption3.setSelected(false);
                answerOption4.setSelected(false);
                confirmChoice.setEnabled(false);
            }
        });

        int index = 8;
        MouseAdapter helpFacilityMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Checks which help facility was selected, then checks if they're available for this player.
                if(e.getSource() == audienceLbl){
                    if(currentTurn.getPublicAvailable()){
                        // Sets it to disabled so it can't be used again, and changes the icon to show this.
                        audienceLbl.setIcon(new ImageIcon(getClass().getResource("audience_used.png")));
                        currentTurn.setPublicAvailable(false);

                        // The correct answer will always have random votes between 50-100%.
                        Random random = new Random();
                        int correctVote = random.nextInt(100-50) + 50;

                        // The other 3 incorrect options will be given random percentages of what is left.
                        int answer2Max = (100 - correctVote) / 3;
                        int answer2 = random.nextInt(100 - correctVote);

                        int answer3Max = answer2Max - answer2;
                        int answer3 = random.nextInt(100 - correctVote - answer2);

                        int answer4Max = answer3Max - answer3;
                        int answer4 = 100 - correctVote - answer2 - answer3;

                        String[] incorrect = currentQuestion.getWrongAnswers();
                        System.out.println("-- Total numbers -- \nCorrect Answer: " + correctVote + "%\n" + incorrect[0]
                                + ": " + answer2 + "%\n" + incorrect[1] +  "2: " + answer3 + "%\n" + incorrect[2] + "3: "
                                + answer4 + "%\nTotal Votes: " + (correctVote + answer2 + answer3 + answer4));
                        final int inc = 14;

                        final Dimension currentSize = new Dimension(708, 280);

                        Dimension d = new Dimension(answerOption1.getSize().width + inc,
                                answerOption1.getSize().height + inc);

                        /*
                            The below code checks which button has the correct answer, gives it the percentage from 50-100
                            then animates it to increase and decrease in size, to draw the player's attention to the
                            answer with the most votes.
                        */
                        if(answerOption1.getText().contains(currentQuestion.getCorrectAnswer())){
                            audienceTimer = new Timer();

                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    System.out.println(answerOption1.getPreferredSize());
                                    if(buttonTimerIndex == 0){
                                        answerOption1.setPreferredSize(d);
                                        answerOption1.setText(answerOption1.getText() + " [" + correctVote + "%]");

                                        answerOption2.setText(answerOption2.getText() + " [" + answer2 + "%]");
                                        answerOption3.setText(answerOption3.getText() + " [" + answer3 + "%]");
                                        answerOption4.setText(answerOption4.getText() + " [" + answer4 + "%]");
                                    }
                                    else if (buttonTimerIndex == 1) {
                                        answerOption1.setPreferredSize(currentSize);
                                    }
                                    else if (buttonTimerIndex == 2) {
                                        answerOption1.setPreferredSize(d);
                                    }
                                    else if (buttonTimerIndex == 3) {
                                        answerOption1.setPreferredSize(currentSize);
                                    }
                                    else {
                                        buttonTimerIndex = 0;
                                        audienceTimer.cancel();
                                    }
                                    answerOption4.updateUI();
                                    buttonTimerIndex++;
                                }
                            };
                            audienceTimer.scheduleAtFixedRate(task, 700,700);
                        }
                        else if(answerOption2.getText().contains(currentQuestion.getCorrectAnswer())){

                            audienceTimer = new Timer();

                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    System.out.println(answerOption2.getPreferredSize());
                                    if(buttonTimerIndex == 0){
                                        answerOption2.setPreferredSize(d);
                                        answerOption2.setText(answerOption2.getText() + " [" + correctVote + "%]");

                                        answerOption1.setText(answerOption1.getText() + " [" + answer2 + "%]");
                                        answerOption3.setText(answerOption3.getText() + " [" + answer3 + "%]");
                                        answerOption4.setText(answerOption4.getText() + " [" + answer4 + "%]");
                                    }
                                    else if (buttonTimerIndex == 1) {
                                        answerOption2.setPreferredSize(currentSize);
                                    }
                                    else if (buttonTimerIndex == 2) {
                                        answerOption2.setPreferredSize(d);
                                    }
                                    else if (buttonTimerIndex == 3) {
                                        answerOption2.setPreferredSize(currentSize);
                                    }
                                    else {
                                        buttonTimerIndex = 0;
                                        audienceTimer.cancel();
                                    }
                                    answerOption4.updateUI();
                                    buttonTimerIndex++;
                                }
                            };
                            audienceTimer.scheduleAtFixedRate(task, 700,700);
                        }
                        else if(answerOption3.getText().contains(currentQuestion.getCorrectAnswer())){

                            audienceTimer = new Timer();

                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    System.out.println(answerOption3.getPreferredSize());
                                    if(buttonTimerIndex == 0){
                                        answerOption3.setPreferredSize(d);
                                        answerOption3.setText(answerOption3.getText() + " [" + correctVote + "%]");

                                        answerOption1.setText(answerOption1.getText() + " [" + answer2 + "%]");
                                        answerOption2.setText(answerOption2.getText() + " [" + answer3 + "%]");
                                        answerOption4.setText(answerOption4.getText() + " [" + answer4 + "%]");
                                    }
                                    else if (buttonTimerIndex == 1) {
                                        answerOption3.setPreferredSize(currentSize);
                                    }
                                    else if (buttonTimerIndex == 2) {
                                        answerOption3.setPreferredSize(d);
                                    }
                                    else if (buttonTimerIndex == 3) {
                                        answerOption3.setPreferredSize(currentSize);
                                    }
                                    else {
                                        buttonTimerIndex = 0;
                                        audienceTimer.cancel();
                                    }
                                    answerOption4.updateUI();
                                    buttonTimerIndex++;
                                }
                            };
                            audienceTimer.scheduleAtFixedRate(task, 700,700);


                        }
                        else if(answerOption4.getText().contains(currentQuestion.getCorrectAnswer())){
                            audienceTimer = new Timer();

                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    System.out.println(answerOption4.getPreferredSize());
                                    if(buttonTimerIndex == 0){
                                        answerOption4.setPreferredSize(d);
                                        answerOption4.setText(answerOption4.getText() + " [" + correctVote + "%]");

                                        answerOption1.setText(answerOption1.getText() + " [" + answer2 + "%]");
                                        answerOption3.setText(answerOption3.getText() + " [" + answer3 + "%]");
                                        answerOption2.setText(answerOption2.getText() + " [" + answer4 + "%]");
                                    }
                                    else if (buttonTimerIndex == 1) {
                                        answerOption4.setPreferredSize(currentSize);
                                    }
                                    else if (buttonTimerIndex == 2) {
                                        answerOption4.setPreferredSize(d);
                                    }
                                    else if (buttonTimerIndex == 3) {
                                        answerOption4.setPreferredSize(currentSize);
                                    }
                                    else {
                                        buttonTimerIndex = 0;
                                        audienceTimer.cancel();
                                    }
                                    answerOption4.updateUI();
                                    buttonTimerIndex++;
                                }
                            };
                            audienceTimer.scheduleAtFixedRate(task, 700,700);
                        }

                    }
                }
                else if(e.getSource() == halfhalfLbl){
                    // Check if half-half is available, then use it if it is.
                    if(currentTurn.getHalfHalfAvailable()){
                        halfhalfLbl.setIcon(new ImageIcon(getClass().getResource("halfhalf_used.png")));
                        currentTurn.setHalfHalfAvailable(false);
                        ArrayList<JToggleButton> answerList = new ArrayList<>();
                        answerList.add(answerOption1);
                        answerList.add(answerOption2);
                        answerList.add(answerOption3);
                        answerList.add(answerOption4);

                        // Loop through all of the answer buttons, finding the one containing the
                        // correct answer. This is removed from the list so it cannot be disabled.
                        for (int i=0; i<answerList.size(); i++){
                            JToggleButton button = answerList.get(i);
                            if(button.getText().contains(currentQuestion.getCorrectAnswer())){
                                answerList.remove(i);
                            }

                        }

                        // Generate a number which represents one of the leftover incorrect answers,
                        // then removes this one from the list so it cannot be disabled when it has already.
                        Random rand = new Random();
                        int buttonId = rand.nextInt(answerList.size());
                        answerList.get(buttonId).setEnabled(false);
                        answerList.remove(answerList.get(buttonId));

                        // Does the same again, without the previous incorrect answer.
                        buttonId = rand.nextInt(answerList.size());
                        answerList.get(buttonId).setEnabled(false);
                    }
                }
                else if(e.getSource() == secondLifeLbl){
                    if(currentTurn.getSecondLifeAvailable()){
                        secondLifeLbl.setIcon(new ImageIcon(getClass().getResource("secondlife_used.png")));
                        currentTurn.setSecondLifeAvailable(false);
                        isSecondLifeActive = true;
                        JOptionPane.showMessageDialog(mainPnl, "Second life is a new ability, which gives" +
                                " you a second chance if the answer you submit is incorrect. \n NOTE: If you use" +
                                " this but still get the answer correct, you will still lose this help option.");
                    }
                }
            }
        };
        audienceLbl.addMouseListener(helpFacilityMouseListener);
        halfhalfLbl.addMouseListener(helpFacilityMouseListener);
        secondLifeLbl.addMouseListener(helpFacilityMouseListener);


        ActionListener answerButtonClick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionSelected = true;
                ((JToggleButton) e.getSource()).setSelected(true);
                confirmChoice.setEnabled(true);
                selected = ((JToggleButton) e.getSource());
            }
        };
        answerOption1.addActionListener(answerButtonClick);
        answerOption2.addActionListener(answerButtonClick);
        answerOption3.addActionListener(answerButtonClick);
        answerOption4.addActionListener(answerButtonClick);
    }

    private void showCategories() {
        // Checks if there are any questions for this specific difficulty level, and if so sets the text of each button
        // to the name of each category.
        questionLbl.setText("Please Choose Your Category!");

        if(generalQuestions.hasQuestionForDifficulty(currentStage)) {
            answerOption1.setText("General Knowledge");
        }else{
            answerOption1.setEnabled(false);
            answerOption1.setText("Out of questions!");
        }

        if(technologyQuestions.hasQuestionForDifficulty(currentStage)) {
            answerOption2.setText("Technology");
        }else{
            answerOption2.setEnabled(false);
            answerOption2.setText("Out of questions!");
        }
        if(entertainQuestions.hasQuestionForDifficulty(currentStage)) {
            answerOption3.setText("Entertainment");
        }else{
            answerOption3.setEnabled(false);
            answerOption3.setText("Out of questions!");
        }

        if(historyQuestions.hasQuestionForDifficulty(currentStage)) {
            answerOption4.setText("Historical");
        }else{
            answerOption4.setEnabled(false);
            answerOption4.setText("Out of questions!");
        }
        setHelpFacilitiesVisible(false);
        this.isSelectingCategory = true;
        confirmChoice.setText("Continue");
    }

    private PlayerList playersOut = new PlayerList();
    private void updateNextPlayer() {
        EndGameGUI end = new EndGameGUI(QuestionGUI.this, playerList);

        boolean lastTurn = false;
        if(playersOut.size() != playerList.size()){
            System.out.println(currentTurn + " can play: " + currentTurn.getCanPlay());
            if(playerList.getElementAt(playerList.size() - 1).getId() != currentTurn.getId()){
                turnNumber++;
                currentTurn = playerList.getElementAt(turnNumber);


            } else {
                System.out.println("All players have answered this stage. Moving on to next stage [Current" +
                        " Stage: " + currentStage + "]");
                turnNumber = 0;
                currentStage++;
                currentTurn = playerList.getElementAt(turnNumber);
                playerTurnLbl.setText(currentTurn.getName() + "'s Turn: ");
                lastTurn = true;
            }
            System.out.println(currentTurn + " can play: " + currentTurn.getCanPlay());
            System.out.println("Current stage: " + currentStage);
            if(lastTurn && currentStage > main.getMAX_QUESTIONS()){
                end.setVisible(true);
                this.setVisisble(false);
            }
            System.out.println("Out: " + playersOut);
            if(currentTurn.getCanPlay()){
                playerTurnLbl.setText(currentTurn.getName() + "'s Turn: ");
                if(currentTurn.getHalfHalfAvailable()){
                    halfhalfLbl.setIcon(new ImageIcon(getClass().getResource("halfhalf.png")));
                }else {
                    halfhalfLbl.setIcon(new ImageIcon(getClass().getResource("halfhalf_used.png")));
                }
                if(currentTurn.getSecondLifeAvailable()){
                    secondLifeLbl.setIcon(new ImageIcon(getClass().getResource("secondlife.png")));
                }
                else {
                    secondLifeLbl.setIcon(new ImageIcon(getClass().getResource("secondlife_used.png")));
                }
                if(currentTurn.getPublicAvailable()){
                    audienceLbl.setIcon(new ImageIcon(getClass().getResource("audience.png")));
                }
                else {
                    audienceLbl.setIcon(new ImageIcon(getClass().getResource("audience_used.png")));
                }
            }else {
                if(playersOut.findPlayer(currentTurn.getId()) == null){

                    playersOut.addPlayer(currentTurn.getId(), currentTurn.getName(),
                            currentTurn.getMoney(), currentTurn.getCanPlay());

                }

                System.out.println(currentTurn + " is out of the game. Skipping.");

                System.out.println("Out: " + playersOut);
                updateNextPlayer();
            }
        } else {
            System.out.println("All players are out of the game.");

            end.setVisible(true);
            this.setVisisble(false);
        }
    }

    private void generateNextQuestion(QuestionList category){
        if(category.getSize() > 0){
            try{
                currentQuestion = category.chooseQuestion(main.getMAX_QUESTIONS(), currentStage);
                category.removeQuestion(currentQuestion.getQuestion());
                System.out.println("Selected: " + currentQuestion.getQuestion());
            }catch (Exception err){
                System.out.println("Encountered error while choosing question: " + err);
            }
        }
        else {
            System.out.println("Failed to generate next question - there are none left in this category");
        }


    }

    public void setHelpFacilitiesVisible(boolean visible){
        halfhalfLbl.setVisible(visible);
        secondLifeLbl.setVisible(visible);
        audienceLbl.setVisible(visible);
    }

    public String getRandomSoundString(boolean correctAnswer) {
        // Adds all of the possible sounds to an array, then chooses one to return.
        try {
            ArrayList<String> sounds = new ArrayList<>();
            String currentDirectory = (System.getProperty("user.dir").replace("\\", "/"))
                    + "/src/audio/";
            File file = new File(currentDirectory);
            if(!file.exists()){
                currentDirectory = (System.getProperty("user.dir").replace("\\", "/"))
                        + "/audio/";
            }
            if (correctAnswer) {
                sounds.add(currentDirectory + "correct_clap.mp3");
                sounds.add(currentDirectory + "correct_donaldtrump.mp3");
                sounds.add(currentDirectory + "correct_champions.wav");
                sounds.add(currentDirectory + "correct_lambsauce.mp3");
                sounds.add(currentDirectory + "correct_looney.wav");
                sounds.add(currentDirectory + "correct_nerd.wav");
                sounds.add(currentDirectory + "correct_overwatch.wav");
                sounds.add(currentDirectory + "correct_smrt.wav");
                sounds.add(currentDirectory + "correct_sonic.mp3");
                sounds.add(currentDirectory + "correct_sponge.mp3");
            } else {
                sounds.add(currentDirectory + "incorrect_blewit.wav");
                sounds.add(currentDirectory + "incorrect_cod.mp3");
                sounds.add(currentDirectory + "incorrect_doh.mp3");
                sounds.add(currentDirectory + "incorrect_donkey.wav");
                sounds.add(currentDirectory + "incorrect_giggle.mp3");
                sounds.add(currentDirectory + "incorrect_gtav.mp3");
                sounds.add(currentDirectory + "incorrect_homer.mp3");
                sounds.add(currentDirectory + "incorrect_mario.mp3");
                sounds.add(currentDirectory + "incorrect_titanic.wav");
                sounds.add(currentDirectory + "incorrect_weakest.mp3");
            }
            // Chooses the random sound, and returns the file path as a string.
            Random r = new Random();
            int soundId = r.nextInt(sounds.size());
            return sounds.get(soundId);

        }catch(Exception e){
            return null;
        }
    }

    public void setVisisble(boolean visible){
        frame.setVisible(visible);
        System.out.println("Setting visible........");
    }

    public void applyCurrentQuestion() {

        questionLbl.setText(currentQuestion.getQuestion());

        // Sets the text of the option buttons to the potential answers.

        ArrayList<String> shuffled = new ArrayList<>();

        shuffled.addAll(Arrays.asList(currentQuestion.getWrongAnswers()));
        shuffled.add(currentQuestion.getCorrectAnswer());

        Collections.shuffle(shuffled);

        answerOption1.setText(shuffled.get(0));
        answerOption2.setText(shuffled.get(1));
        answerOption3.setText(shuffled.get(2));
        answerOption4.setText(shuffled.get(3));

        // Changes the text of the confirm button from confirming the category choice,
        // to submitting their chosen answer.
        confirmChoice.setText("Final Answer!");

        // Shows the help facilities, which are hidden when choosing categories.
        setHelpFacilitiesVisible(true);

    }
}
