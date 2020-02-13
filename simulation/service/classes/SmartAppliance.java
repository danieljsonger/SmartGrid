package simulation.service.classes;

public class SmartAppliance extends Appliance {
	
	private String lvl;
	private int lowW;
	
	public SmartAppliance() {
		this.lvl = "Off";
	}
	public SmartAppliance(String newType, int newOnW, int newWatts, long newLocation, float newProb, String newLevel, int newLowW) {
		super(newType,newOnW,newWatts,newLocation,newProb);
		this.lvl = newLevel;
		this.lowW = newLowW;
	}
	
	// accessor
	public String getLvl() {
		return this.lvl;
	}
	public int getLowW() {
		return this.lowW;
	}
	public String getThisType() {
		return "Smart";
	}
	
	// mutators
	public void setLvl(String newLevel) {
		this.lvl = newLevel;
	}
	public void setLowW(int newLowW) {
		this.lowW = newLowW;
	}
	
	// class-specific methods
	public void toLow() {
		setWatts(this.lowW);
		setLvl("Low");
	}
	public String toString() {
		
		String smartStr = "";
		smartStr = super.toString() + smartStr + "\nLevel: " + this.lvl;
		
		return smartStr;
		
	}
	
	// method for turning smart appliance to low
	

}
