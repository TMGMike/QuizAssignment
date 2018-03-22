import java.io.Serializable;

public class Player implements Serializable{

	private int id;
	private String name;
	private int money;
	private boolean canPlay;
	private boolean publicAvailable;
	private boolean halfHalfAvailable;
	private boolean secondLifeAvailable;
	private int answeredCount;

	private QuestionList answeredQuestions = new QuestionList();

	public Player(int id, String name, int money, boolean canPlay) {
        this.name = name;
        this.id = id;
        this.money = money;
        this.canPlay = canPlay;

        this.publicAvailable = true;
        this.halfHalfAvailable = true;
        this.secondLifeAvailable = true;
    }
	/**
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}


	public String getName() {
		return this.name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getMoney() {
		return this.money;
	}

	/**
	 *
	 * @param amount
	 */
	public void addMoney(int amount) {
		// TODO - implement Player.addMoney
		this.money+= amount;
	}

	public void resetMoney(){
		this.money = 0;
	}

	public boolean getCanPlay() {
		return this.canPlay;
	}

	/**
	 *
	 * @param canPlay
	 */
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}

	public boolean getPublicAvailable() {
		return this.publicAvailable;
	}

	/**
	 *
	 * @param publicAvailable
	 */
	public void setPublicAvailable(boolean publicAvailable) {
		this.publicAvailable = publicAvailable;
	}

	public boolean getHalfHalfAvailable() {
		return this.halfHalfAvailable;
	}

	/**
	 *
	 * @param halfHalfAvailable
	 */
	public void setHalfHalfAvailable(boolean halfHalfAvailable) {
		this.halfHalfAvailable = halfHalfAvailable;
	}

	public boolean getSecondLifeAvailable() {
		return this.secondLifeAvailable;
	}

	/**
	 *
	 * @param secondLifeAvailable
	 */
	public void setSecondLifeAvailable(boolean secondLifeAvailable) {
		this.secondLifeAvailable = secondLifeAvailable;
	}

	@Override
    public String toString(){
	    return " [" + this.getId() + "] " + this.getName() + " - £" + this.getMoney();
    }

	public int getAnsweredCount() {
		return this.answeredCount;
	}

	public void setAnsweredCount(int answeredCount) {
		this.answeredCount = answeredCount;
	}

	public QuestionList getAnsweredQuestions() {
		return answeredQuestions;
	}
	public void addAnsweredQuestion(Question toAdd){
    	this.answeredQuestions.addQuestion(toAdd.getId(), toAdd.getQuestion(), toAdd.getCorrectAnswer(),
				toAdd.getWrongAnswers(), toAdd.getDifficultyLevel());
	}
}
