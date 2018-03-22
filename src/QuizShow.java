import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class QuizShow {
    // GUI Components
    private JPanel mainPnl;
    private JTextField playerNameTF;
    private JButton addPlyrBtn;
    private JButton rmvPlyrBtn;
    private JList playerList; //TODO Rename this so it isn't similar to PlayerList players
    private JButton continueBtn;
    private JButton playGameBtn;
    private JButton exitBtn;
    private JButton settingsBtn;
    private JPanel playersPnl;
    private JPanel playerListPnl;
    private JPanel menuPnl;
    private JLabel logoLbl;

    // Set how many questions must be answered in order to win. This helps in asking in order of difficulty.
    // A question's difficulty level should be between 1 and this value.
    private final int MAX_QUESTIONS = 5;

    // Set the maximum amount of players allowed in one game.
    private final int MAX_PLAYERS = 10;

    PlayerList players;

    private QuestionList generalQuestions = new QuestionList();
    private QuestionList technologyQuestions = new QuestionList();
    private QuestionList entertainQuestions = new QuestionList();
    private QuestionList historyQuestions = new QuestionList();

    // Properties from the settings window. These are used within the game.
    private boolean useSoundEffects = true;
    private int soundEffectVolume = 25;

    private SettingsGUI settingsGUI = new SettingsGUI(this);

    public QuizShow() {
        players = new PlayerList();
        playerList.setModel(players);

        logoLbl.setIcon(new ImageIcon(getClass().getResource("Zillionaire_Logo.png")));

        playerNameTF.requestFocus();

        addPlyrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new player with a random ID, always with same number of digits.
                if(!playerNameTF.getText().equals("") && players.size() < MAX_PLAYERS){
                    Random r = new Random();
                    int Low = 11111111;
                    int High = 99999999;
                    int Result = r.nextInt(High-Low) + Low;
                    players.addPlayer(Result, playerNameTF.getText(), 0, true);
                    playerNameTF.setText("");
                }
                System.out.println(players.size());
                if(players.size() == MAX_PLAYERS){
                    addPlyrBtn.setEnabled(false);
                    addPlyrBtn.setText("Max Players Reached");
                }
            }
        });
        rmvPlyrBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Checks if a player has been selected, and prompts for confirmation before deleting them.
                int selected = playerList.getSelectedIndex();
                if(selected == -1) {
                    JOptionPane.showMessageDialog(mainPnl, "No player was selected.");
                }else {
                    Player toRemove = players.getElementAt(selected);


                    if(JOptionPane.showConfirmDialog(mainPnl, "Are you sure you wish to delete " + toRemove +
                            "?", "Confirm Removal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                        players.removePlayer(toRemove.getId());
                        playerList.clearSelection();
                        addPlyrBtn.setEnabled(true);
                        addPlyrBtn.setText("Add Player");
                    }
                }
            }
        });
        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(players.size() > 0){
                    playersPnl.setVisible(false);
                    playerListPnl.setVisible(false);
                    menuPnl.setVisible(true);

                }else {
                    JOptionPane.showMessageDialog(mainPnl,"Please provide at least one player.");
                }

            }
        });
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting Game...");
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
                addInitialQuestions();
                PlayerList newPlayerList = new PlayerList();

                /*
                    This will reset any players currently in the list, defaulting back to no answered questions,
                    and no money, so the user doesn't have to restart the software to start a new game.
                 */
                for(int i = 0; i < players.size(); i++) {
                    Player current = players.getElementAt(i);
                    newPlayerList.addPlayer(current.getId(), current.getName(), 0, true);
                }
                players.clear();
                players = newPlayerList;

                QuestionGUI questionGUI = new QuestionGUI(QuizShow.this, players);
                questionGUI.setVisisble(true);

            }
        });
        playerNameTF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!playerNameTF.getText().equals("") && players.size() < MAX_PLAYERS){

                    // Generates a random ID number unique for each player.
                    Random r = new Random();
                    int Low = 11111111;
                    int High = 99999999;
                    int Result = r.nextInt(High-Low) + Low;
                    players.addPlayer(Result, playerNameTF.getText(), 0, true);
                    playerNameTF.setText("");
                }
                if(players.size() == MAX_PLAYERS){
                    addPlyrBtn.setEnabled(false);
                    addPlyrBtn.setText("Max Players Reached");
                }
            }
        });
    }

    private void addInitialQuestions() {
        String[] incorrect =  new String[]{"Dublin", "Huddersfield", "Manchester"};
        generalQuestions.addQuestion(0, "What is the capital city of England?",
                "London", incorrect, 1 );

        incorrect =  new String[]{"29th December", "30th December", "1st January"};
        generalQuestions.addQuestion(1, "On which day is New Years' Eve?",
                "31st December", incorrect, 1 );

        incorrect = new String[]{"Australian Dollhair", "Australian Dollop", "Australian Pound"};
        generalQuestions.addQuestion(2, "What is the currency used in Australia?",
                "Australian Dollar", incorrect, 1);

        incorrect =  new String[]{"Gordon Ramsay", "Barack Obama", "Theresa May"};
        generalQuestions.addQuestion(3, "<html>Who became famous for the quote: \"You can always " +
                        "count on <br>Americans to do the right thing - after they've tried everything else.\"?</html>",
                "Winston Churchill", incorrect, 2);

        incorrect =  new String[]{"Sky, weather, thunder, and lightning",
                "Fire, metalworking, and crafts", "War, bloodshed, and violence"};
        generalQuestions.addQuestion(4, "Poseidon is a Greek God. What was he the God of?",
                "Sea, rivers, floods, droughts, and earthquakes", incorrect, 2 );

        incorrect =  new String[]{"Influenza", "Yellow fever", "Leprosy"};
        generalQuestions.addQuestion(5, "Which virus causes bleeding in the body due to the " +
                        "destruction of blood vessels?",
                "Ebola", incorrect, 2 );

        incorrect = new String[]{"Earth's Moon", "Europa", "Deimos"};
        generalQuestions.addQuestion(6, "Which is the largest Moon in the solar system?",
                "Ganymede", incorrect, 3 );

        incorrect =  new String[]{"Isaac Newton", "Theresa May", "Winston Churchill"};
        generalQuestions.addQuestion(7, "Which famous person was known for the following quote:" +
                        "\n\"The difference between stupidity and genius is that genius has its limits\"?",
                "Albert Einstein", incorrect, 3 );

        incorrect =  new String[]{"France", "Germany", "Sweden"};
        generalQuestions.addQuestion(8, "Which country’s airline is KLM?",
                "Holland", incorrect, 3 );

        incorrect =  new String[]{"Queen", "King", "Knight"};
        generalQuestions.addQuestion(9, "Which chess piece can only move diagonally?",
                "Bishop", incorrect, 4 );

        incorrect =  new String[]{"Stegosaurus", "Saurophaganax", "Carnotaurus"};
        generalQuestions.addQuestion(10, "Previously, which dinosaur was commonly misinterpreted by " +
                        "movies?","Velociraptor", incorrect, 4 );

        incorrect =  new String[]{"South Africa", "Canada", "United Kingdom"};
        generalQuestions.addQuestion(11, "Which nation was the first to give women the right to vote?",
                "New Zealand", incorrect, 4 );

        incorrect =  new String[]{"Martin Luther King", "Charles Babbage", "Winston Churchill"};
        generalQuestions.addQuestion(11, "Which famous person said the following quote: \"Life " +
                        "would be tragic if it weren't funny\"?","Stephen Hawking", incorrect,5);
        incorrect =  new String[]{"Martin Luther King", "Charles Babbage", "Winston Churchill"};

        generalQuestions.addQuestion(12, "Which famous person said the following quote: \"Life " +
                "would be tragic if it weren't funny\"?","Stephen Hawking", incorrect,5);


        incorrect = new String[]{"Microsoft", "Google", "JetBrains"};
        technologyQuestions.addQuestion(0, "Firefox is a web browser for many platforms. Which " +
                "company created it?", "Mozilla", incorrect, 1);

        incorrect = new String[]{"Samsung Galaxy", "Google Pixel", "Apple iPhone"};
        technologyQuestions.addQuestion(1, "In November 2017, a new smartphone was announced that \n" +
                "featured the world's first 120GHz screen in a phone. Which was it?", "Razer Phone",
                incorrect, 1);

        incorrect = new String[]{"1280x720", "1080x1080", "1600x1080"};
        technologyQuestions.addQuestion(2, "Which screen resolution is generally described as 1080p HD?",
                "1920x1080", incorrect, 1);

        incorrect = new String[]{"Nescafe", "Typhoo", "Brick"};
        technologyQuestions.addQuestion(3, "Previously, what was Java known as?",
                "Oak", incorrect, 2);

        incorrect = new String[]{"Pascal", "Haswell", "Kabylake"};
        technologyQuestions.addQuestion(4, "What was the architecture name of the i7-6700K?",
                "Skylake", incorrect, 2);

        incorrect = new String[]{"Hypertext Training Protocol", "Hypertext Transmission Protocol",
                "Hypertext Terminal Protocol"};
        technologyQuestions.addQuestion(5, "What does HTTP stand for?",
                "Hypertext Transfer Protocol", incorrect, 2);

        incorrect = new String[]{"'Standard'", "'Silly'", "'Simple'"};
        technologyQuestions.addQuestion(6, "What does the last 'S' stand for in: HTTPS, WSS, FTPS?",
                "'Secure'", incorrect, 3);

        incorrect = new String[]{"AWS", "OVH", "DreamHost"};
        technologyQuestions.addQuestion(7, "Which of the following is Microsoft's Cloud service provider?",
                "Azure", incorrect, 3);

        incorrect = new String[]{"My Area Network", "Misc Area Network", "Main Area Network"};
        technologyQuestions.addQuestion(8, "What does the network architecture \"MAN\" stand for?",
                "Metropolitan Area Network", incorrect, 3);

        incorrect = new String[]{"AWS Runner", "AWS Execute", "AWS Cloud"};
        technologyQuestions.addQuestion(9, "AWS provides ways of executing code in the cloud. What " +
                        "is this service called?",
                "AWS Lambda", incorrect, 4);

        incorrect = new String[]{"Elastic Computer", "Extraordinary Cloud", "Electronic Cloud"};
        technologyQuestions.addQuestion(10, "What does the AWS service 'EC2' stand for?",
                "Elastic Cloud Compute", incorrect, 4);

        incorrect = new String[]{"Sony", "Panasonic", "Dolby"};
        technologyQuestions.addQuestion(11, "Which company's first product was an audio Oscillator?",
                "Hewlett-Packard", incorrect, 4);

        incorrect = new String[]{"Barry Carlyon", "Dallas Tester", "TravistyOJ"};
        technologyQuestions.addQuestion(12, "Which developer was the first to create a Subathon Tool" +
                        " for Twitch streamers?","Mike Binks", incorrect, 5);



        incorrect = new String[]{"Frosty Knickers", "The Beast", "The Dark Destroyer"};
        entertainQuestions.addQuestion(0, "Who is the newest chaser on the TV show \"The Chase\"?",
                "The Vixen", incorrect, 1);

        incorrect = new String[]{"Winston Churchill", "Oliver Queen", "Alan Barry"};
        entertainQuestions.addQuestion(1, "What is DC's The Flash's real name?",
                "Barry Allen", incorrect, 1);

        incorrect = new String[]{"Call of Duty: Modern Warfare 2", "Tom Clancy's: The Division", "Grand Theft Auto IV"};
        entertainQuestions.addQuestion(3, "In which game does the quote \"Drop your weapon, or drop " +
                        "your drawers!\" appear?", "7 Days to Die", incorrect, 3);

        incorrect = new String[]{"Call of Duty: Modern Warfare 2", "Tom Clancy's: The Division", "Grand Theft Auto IV"};
        entertainQuestions.addQuestion(4, "In which film does the quote \"It's just good business, Jack. " +
                "It's just good business.\" appear?", "Pirates of the Caribbean: At World's End", incorrect, 3);

        incorrect = new String[]{"Barney", "Dinosaurs", "Jurassic Park"};
       entertainQuestions.addQuestion(5, "Which TV series featured a baby Triceratops called \"Chomp\"?", "Dinosaur King",
                incorrect, 3);
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

    public int getSoundEffectVolume() { return soundEffectVolume; }

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

    public int getMAX_QUESTIONS() {
        return MAX_QUESTIONS;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("QuizShow");
        frame.setContentPane(new QuizShow().mainPnl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}
