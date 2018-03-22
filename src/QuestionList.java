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

    public void removeQuestion(String question){
        Question toRemove = getQuestion(question);
        if(toRemove != null) {
            int i;
            for (i = 0; i < super.size(); ) {
                if (super.getElementAt(i) == toRemove){
                    break;
                }
                i++;
            }
            super.removeElementAt(i);
        }else {
            System.out.println("Failed: Couldn't find question to remove.");
        }
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
        // Randomly generate a question from the current list, matching the provided difficulty level (currentStage)
        ArrayList<Question> currentDifficulty = new ArrayList<Question>();
        for(int i = 0; i < this.size();){

            Question currentQuestion = this.getElementAt(i);

            if(currentQuestion.getDifficultyLevel() == currentStage){
                currentDifficulty.add(currentQuestion);
            }
            i++;
        }
        // Verifies if any questions, and chooses a question from this list, returning it.
        if(currentDifficulty.size() > 0) {
            Random r = new Random();
            int Low = 0;
            int High = currentDifficulty.size();
            int questionId = r.nextInt(High-Low) + Low;
            System.out.println("Size: " + currentDifficulty.size());
            return currentDifficulty.get(questionId);
        }
        // Return null if no questions were found for this specific difficulty level.
        return null;
    }

    public boolean hasQuestionForDifficulty(int difficultyLevel){

        // Check if there are any questions in this category, for the provided difficulty level.
        for(int i = 0; i < this.size();){
            Question currentQuestion = this.getElementAt(i);
            if(currentQuestion.getDifficultyLevel() == difficultyLevel){
                // Break out of the loop and return true if any question is found.
                return true;
            }
            i++;
        }
        // Return false if nothing has been found yet.
        return false;
    }
}
