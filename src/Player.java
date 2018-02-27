import java.io.Serializable;

public class Player implements Serializable{

    private String name;
	private int id;
	private int money;
	private boolean canPlay;
	private boolean publicAvailable;
	private boolean halfHalfAvailable;
	private boolean secondLifeAvailable;

    public Player(int id, String name, int money, boolean canPlay) {
        this.name = name;
        this.id = id;
        this.money = money;
        this.canPlay = canPlay;
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
		throw new UnsupportedOperationException();
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
	    return this.getName() + " [" + this.getId() + "]";
    }
}
