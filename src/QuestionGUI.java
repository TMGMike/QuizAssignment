import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

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
    private QuestionList listofQuestions;
    private Question currentQuestion;
    private QuizShow main;
    private QuestionList generalQuestions = new QuestionList();
    private QuestionList technologyQuestions = new QuestionList();
    private QuestionList entertainQuestions = new QuestionList();
    private QuestionList historyQuestions = new QuestionList();

    public QuestionGUI(QuizShow main) {

        frame = new JFrame("Question GUI");
        frame.setContentPane(this.mainPnl);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        questionLbl.setBackground(mainPnl.getBackground());

        audienceLbl.setIcon(new ImageIcon(getClass().getResource("audience.png")));
        halfhalfLbl.setIcon(new ImageIcon(getClass().getResource("halfhalf.png")));
        secondLifeLbl.setIcon(new ImageIcon(getClass().getResource("secondlife.png")));

        this.main = main;
        this.listofQuestions = listofQuestions;
        this.playerList = this.main.players;
        this.currentTurn = playerList.elementAt(0);
        this.generalQuestions = this.main.getGeneralQuestions();
        this.technologyQuestions = this.main.getTechnologyQuestions();
        this.entertainQuestions = this.main.getEntertainQuestions();
        this.historyQuestions = this.main.getHistoryQuestions();

        this.playerListView.setModel(playerList);

        playerTurnLbl.setText(currentTurn.getName() + "'s Turn: ");
        currentStage = 1;

        this.isSelectingCategory = true;

        questionLbl.setText("Please Choose Your Category!");
        answerOption1.setText("General Knowledge");
        answerOption2.setText("Technology");
        answerOption3.setText("Entertainment");
        answerOption4.setText("Historical");

        setHelpFacilitiesVisible(false);

        confirmChoice.setText("Continue");


        answerGroup.add(answerOption1);
        answerGroup.add(answerOption2);
        answerGroup.add(answerOption3);
        answerGroup.add(answerOption4);

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
                    applyCurrentQuestion();
                }else {
                    currentQuestion.setAnswered(true);
                    currentQuestion.setAnsweredBy(currentTurn);

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
                            playerListView.updateUI();
                            if(main.isUseSoundEffects()){
                                JFXPanel dummyPnl = new JFXPanel();
                                Media media = new Media(new File(getRandomSoundString(false)).toURI().toString());
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

                        questionLbl.setText("Please Choose Your Category!");
                        answerOption1.setText("General Knowledge");
                        answerOption2.setText("Technology");
                        answerOption3.setText("Entertainment");
                        answerOption4.setText("Historical");

                        setHelpFacilitiesVisible(false);

                        confirmChoice.setText("Continue");
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
                /** TODO - Implement the Timer to animate the correct answer button to increase and decrease in size.
                    Maybe gradually increase and decrease in size?     **/
                if(e.getSource() == audienceLbl){
                    if(currentTurn.getPublicAvailable()){
                        audienceLbl.setIcon(new ImageIcon(getClass().getResource("audience_used.png")));
                        currentTurn.setPublicAvailable(false);

                        Random random = new Random();
                        int correctVote = random.nextInt(100-50) + 50; // 60

                        int answer2Max = (100 - correctVote) / 3; // 60 / 3 = 20
                        int answer2 = random.nextInt(100 - correctVote); // 15

                        int answer3Max = answer2Max - answer2; // 20 - 15 = 5
                        int answer3 = random.nextInt(100 - correctVote - answer2); // 3

                        int answer4Max = answer3Max - answer3; // 5 - 3 = 2
                        int answer4 = 100 - correctVote - answer2 - answer3;
                        String[] incorrect = currentQuestion.getWrongAnswers();
                        System.out.println("-- Total numbers -- \nCorrect Answer: " + correctVote + "%\n" + incorrect[0]
                                + ": " + answer2 + "%\n" + incorrect[1] +  "2: " + answer3 + "%\n" + incorrect[2] + "3: "
                                + answer4 + "%\nTotal Votes: " + (correctVote + answer2 + answer3 + answer4));
                        final int inc = 8;
                        Dimension d = new Dimension(answerOption1.getSize().width + inc,
                                answerOption1.getSize().height + inc);
                        if(answerOption1.getText().contains(currentQuestion.getCorrectAnswer())){
                            answerOption1.setPreferredSize(d);
                            answerOption1.setText(answerOption1.getText() + " [" + correctVote + "%]");

                            answerOption2.setText(answerOption2.getText() + " [" + answer2 + "%]");
                            answerOption3.setText(answerOption3.getText() + " [" + answer3 + "%]");
                            answerOption4.setText(answerOption4.getText() + " [" + answer4 + "%]");
                        }
                        else if(answerOption2.getText().contains(currentQuestion.getCorrectAnswer())){
                            answerOption2.setPreferredSize(d);
                            answerOption2.setText(answerOption2.getText() + " [" + correctVote + "%]");

                            answerOption1.setText(answerOption1.getText() + " [" + answer2 + "%]");
                            answerOption3.setText(answerOption3.getText() + " [" + answer3 + "%]");
                            answerOption4.setText(answerOption4.getText() + " [" + answer4 + "%]");
                        }
                        else if(answerOption3.getText().contains(currentQuestion.getCorrectAnswer())){
                            answerOption3.setPreferredSize(d);
                            answerOption3.setText(answerOption3.getText() + " [" + correctVote + "%]");

                            answerOption1.setText(answerOption1.getText() + " [" + answer2 + "%]");
                            answerOption2.setText(answerOption2.getText() + " [" + answer3 + "%]");
                            answerOption4.setText(answerOption4.getText() + " [" + answer4 + "%]");
                        }
                        else if(answerOption4.getText().contains(currentQuestion.getCorrectAnswer())){
                            answerOption4.setPreferredSize(d);
                            answerOption4.setText(answerOption4.getText() + " [" + correctVote + "%]");

                            answerOption1.setText(answerOption1.getText() + " [" + answer2 + "%]");
                            answerOption3.setText(answerOption3.getText() + " [" + answer3 + "%]");
                            answerOption2.setText(answerOption2.getText() + " [" + answer4 + "%]");
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
    private int playersOut = 0;
    private void updateNextPlayer() {
        if(playersOut != playerList.size()){
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
            }

            System.out.println(currentTurn + " can play: " + currentTurn.getCanPlay());

            if(currentTurn.getCanPlay()){
                playerTurnLbl.setText(currentTurn.getName() + "'s Turn: ");
            }else {
                playersOut++;
                System.out.println(currentTurn + " is out of the game. Skipping.");
                updateNextPlayer();
            }
        } else {
            System.out.println("All players are out of the game.");
            centrePnl.setVisible(false);
            northPnl.setVisible(false);
            westPnl.setVisible(true);
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
