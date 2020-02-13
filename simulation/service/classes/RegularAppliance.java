package simulation.service.classes;

public class RegularAppliance extends Appliance {
	
	private String lvl;
	
	public RegularAppliance() {
		this.lvl = "Off";
	}
	public RegularAppliance(String newType, int newOnW, int newWatts, long newLocation, float newProb, String newLevel) {
		super(newType,newOnW,newWatts,newLocation,newProb);
		this.lvl = newLevel;
	}
	
	// accessors
	public String getLvl() {
		return this.lvl;
	}
	public String getThisType() {
		return "Regular";
	}
	
	// mutators
	public void setLvl(String newLevel) {
		this.lvl = newLevel;
	}
	
	// class-specific methods
	public String toString() {
		
		String regStr = "";
		regStr = super.toString() + regStr + "\nLevel: " + this.lvl;
		
		return regStr;
		
	}
	
	// method to turn off appliance
	public String onToOff() {
		this.lvl = "Off";
		
		return lvl;
	}

}
