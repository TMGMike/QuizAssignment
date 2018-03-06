import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
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
    private MediaPlayer player;
    private DeselectButtonGroup answerGroup = new DeselectButtonGroup();
    private JFrame frame;

    private boolean questionSelected = false;
    private int currentStage = 0;
    private QuestionList listofQuestions;
    private Question currentQuestion;

    public QuestionGUI(QuizShow main, QuestionList listofQuestions) {
        frame = new JFrame("Question GUI");
        frame.setContentPane(this.mainPnl);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.pack();
        questionLbl.setBackground(mainPnl.getBackground());
        this.listofQuestions = listofQuestions;

        currentQuestion = listofQuestions.getElementAt(currentStage);
        applyCurrentQuestion();

        answerOption1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionSelected = true;
                answerOption1.setSelected(true);
                confirmChoice.setEnabled(true);
            }
        });
        answerOption2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionSelected = true;
                answerOption2.setSelected(true);
                confirmChoice.setEnabled(true);
            }
        });
        answerOption3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionSelected = true;
                answerOption3.setSelected(true);
                confirmChoice.setEnabled(true);
            }
        });
        answerOption4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                questionSelected = true;
                answerOption4.setSelected(true);
                confirmChoice.setEnabled(true);


            }

        });

        answerGroup.add(answerOption1);
        answerGroup.add(answerOption2);
        answerGroup.add(answerOption3);
        answerGroup.add(answerOption4);

        confirmChoice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(answerOption1.isSelected()){


                    // A dummy panel from JFX had to be created to have it initialise the MediaPlayer from JFX.
                    JFXPanel dummyPnl = new JFXPanel();
                    Media media = new Media(new File(getRandomSoundString(true)).toURI().toString());
                    player = new MediaPlayer(media);
                    player.setVolume((double) main.getSoundEffectVolume() / 100);
                    player.play();

                    System.out.println("Correct!");
                    currentStage++;

                    answerOption1.setSelected(false);
                    System.out.println(answerOption1.isSelected());
                    answerOption2.setSelected(false);
                    answerOption3.setSelected(false);
                    answerOption4.setSelected(false);

                    currentQuestion = listofQuestions.getElementAt(currentStage);
                    applyCurrentQuestion();

                }
                else {
                    JFXPanel dummyPnl = new JFXPanel();
                    Media media = new Media(new File(getRandomSoundString(false)).toURI().toString());
                    player = new MediaPlayer(media);
                    player.setVolume((double) main.getSoundEffectVolume() / 100);
                    System.out.println("volume: "+ player.getVolume());
                    player.play();
                }
            }
        });
    }

    public String getRandomSoundString(boolean correctAnswer) {
        try {
            ArrayList<String> sounds = new ArrayList<>();
            String currentDirectory = (System.getProperty("user.dir").replace("\\", "/")) + "/src/audio/";

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
    }

    public void applyCurrentQuestion() {

        questionLbl.setText(currentQuestion.getQuestion());

        answerOption1.setText(currentQuestion.getCorrectAnswer());
        answerOption2.setText(currentQuestion.getWrongAnswers()[0]);
        answerOption3.setText(currentQuestion.getWrongAnswers()[1]);
        answerOption4.setText(currentQuestion.getWrongAnswers()[2]);
    }
}
