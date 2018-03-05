import javax.swing.*;

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

}
