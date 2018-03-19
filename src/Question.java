public class Question {

	private int id;
	private String question;
	private String correctAnswer;
	private String[] wrongAnswers;
	private int difficultyLevel; // This should be between 1 and the MAX_QUESTIONS value.
	private int moneyAwarded = 0;
	private boolean answered = false;
	private Player answeredBy;

    public Question(int id, String question, String correctAnswer, String[] wrongAnswers, int difficultyLevel) {
		this.id = id;
		this.question = question;
		this.correctAnswer = correctAnswer;
		this.wrongAnswers = wrongAnswers;
		this.difficultyLevel = difficultyLevel;
		this.moneyAwarded = (difficultyLevel * 100);
    }

	public int getId() {
		return this.id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return this.question;
	}

	/**
	 *
	 * @param question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	public String getCorrectAnswer() {
		return this.correctAnswer;
	}

	/**
	 *
	 * @param correctAnswer
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public String[] getWrongAnswers() {
		return this.wrongAnswers;
	}

	/**
	 *
	 * @param wrongAnswers
	 */
	public void setWrongAnswers(String[] wrongAnswers) {
		this.wrongAnswers = wrongAnswers;
	}

	public int getDifficultyLevel() {
		return this.difficultyLevel;
	}

	/**
	 *
	 * @param difficultyLevel
	 */
	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public int getMoneyAwarded() {
		return this.moneyAwarded;
	}

	/**
	 *
	 * @param moneyAwarded
	 */
	public void setMoneyAwarded(int moneyAwarded) {
		this.moneyAwarded = moneyAwarded;
	}

	public boolean getAnswered() {
		return this.answered;
	}

	/**
	 *
	 * @param answered
	 */
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}

	public Player getAnsweredBy() {
		return this.answeredBy;
	}

	/**
	 *
	 * @param answeredBy
	 */
	public void setAnsweredBy(Player answeredBy) {
		this.answeredBy = answeredBy;
	}

	@Override
	public String toString() {
		return this.getQuestion() + "\n Correct: " + this.getCorrectAnswer();
	}

}