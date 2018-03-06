import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class QuestionList extends DefaultListModel<Question>{
    public QuestionList() {
        super();
    }
    public void addQuestion(int id, String questionMessage, String correctAnswer,
                            String[] incorrectAnswers, int difficultyLevel) {

        super.addElement(new Question(id, questionMessage, correctAnswer,
                incorrectAnswers, difficultyLevel));
    }

    public Question getQuestion(String questionMessage) {
        Question question;
        int index = -1;

        for(int i = 0; i < super.size(); i++){
            question = (Question)super.elementAt(i);
            if(question.getQuestion().equals(questionMessage)){
                index = i;
                break;
            }
        }

        if(index == -1) {
            return null;
        }
        else{
            return (Question)super.elementAt(index);
        }
    }

    public Question getQuestion(int questionId){
        Question question;
        int index = -1;
        for(int i = 0; i < super.size(); i++){
            question = (Question)super.elementAt(i);
            if(question.getId() == questionId){
                index = i;
                break;
            }
        }

        if(index == -1) {
            return null;
        }
        else{
            return (Question)super.elementAt(index);
        }
    }

    public Question chooseQuestion(final int MAX_QUESTIONS, int currentStage){

        ArrayList<Question> currentDifficulty = new ArrayList<Question>();
        // System.out.println(generalQuestions.getQuestion(0));
        for(int i = 0; i < this.size();){
            // TODO: Document the change from getQuestion(i) to getElementAt(i)
            // Get question ID wouldn't work when a question is removed, where it would no longer match i

            Question currentQuestion = this.getElementAt(i);

            if(currentQuestion.getDifficultyLevel() == currentStage){
                currentDifficulty.add(currentQuestion);
                //  System.out.println("Found question for difficulty '" + currentStage + ": " + currentQuestion.getQuestion() + "\n");
            }
            i++;
        }
        if(currentDifficulty.size() > 0) {
            Random r = new Random();
            int Low = 0;
            int High = currentDifficulty.size();
            int questionId = r.nextInt(High-Low) + Low;
            System.out.println("Size: " + currentDifficulty.size());
            return currentDifficulty.get(questionId);
        }

        // System.out.println("current stage: " + currentStage);

        return null;
    }

}
