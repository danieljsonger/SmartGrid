package simulation.service.classes;

public abstract class Appliance {
	
	private String type;
	private int onW, watts;
	private long acct;
	private float prob;
	private int uniqueID;
	private String lvl;
	private static int count = 0;
	
	public Appliance() {
		this.type = "No type";
		this.onW = 0;
		this.watts = 0;
		this.prob = (float)Math.random();
		this.acct = 0;
		setUniqueID();
	}
	public Appliance(String newType, int newOnW, int newWatts, long newAcct, float newProb) {
		setType(newType);
		setOnW(newOnW);
		setWatts(newWatts);
		setAcct(newAcct);
		setProb(newProb);
		setUniqueID();
	}
	
	// accessors
	public String getType() {
		return this.type;
	}
	public int getOnW() {
		return this.onW;
	}
	public int getWatts() {
		return this.watts;
	}
	public long getAcct() {
		return this.acct;
	}
	public float getProb() {
		return this.prob;
	}
	public int getUniqueID() {
		return this.uniqueID;
	}
	public abstract String getThisType();
	public abstract String getLvl();
	
	// mutators
	public void setType(String newType) {
		this.type = newType;
	}
	public void setOnW(int newOnW) {
		this.onW = newOnW;
	}
	public void setWatts(int newWatts) {
		this.watts = newWatts;
	}
	public void setAcct(long newLocation) {
		this.acct = newLocation;
	}
	public void setProb(float newProb) {
		this.prob = newProb;
	}
	public void setUniqueID() {
		this.uniqueID = 1000000 + count;
		count++;
	}
	public abstract void setLvl(String newLevel);
	
	// class specific methods
	public void toOff() {
		this.setWatts(0);
		this.setLvl("Off");
	}
	public void toOn() {
		this.setWatts(this.onW);
		this.setLvl("On");
	}
	public String toString() {
		
		String retStr = "";
		retStr = retStr + "\nUnique ID: " + this.uniqueID + "\nAppliance Type: " + this.type + "\nLocation: " + this.acct + "\nProbability (on): " + this.prob +"\nOn Wattage: " + this.onW;
		
		return retStr;
	}

}
