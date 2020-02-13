package simulation.service.classes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Simulation {
	
	public long warningWatts;
	public int simLength;
	public int timeStep;
	private Appliance[][] appArr;
	
	
	public Simulation(long newWarningWatts, Appliance[][] newAppArr) {
		
		this.warningWatts = newWarningWatts;
		this.appArr = newAppArr;
		this.simLength = 24;
		this.timeStep = 5;
		
	}
	public Simulation(long newWarningWatts, int newSimLength, int newTimeStep, Appliance[][] newAppArr) {
		
		this.warningWatts = newWarningWatts;
		this.simLength = newSimLength;
		this.timeStep = newTimeStep;
		this.appArr = newAppArr;
		
	}
	
	// get and set
	public Appliance[][] getAppArr() {
		return this.appArr;
	}
	public void setAppArr(Appliance[][] newAppArr) {
		this.appArr = newAppArr;
	}
	
	public void start() {
		/* 
		 * use object attributes to start simulation.
		 */
		long actualWatts = 0;
		SmartAppliance sa = findSmart();
		float rand;
		int topWatts = 0;
		long smallestLocation = 0;
		int brownOutCount = 0;
		int[] locationArr = new int[appArr.length];
		int locationEffected = 0;
		int mostEffectedLocation = 0;
		
		try {
			FileWriter fw = new FileWriter( "report.txt", false);
			BufferedWriter bw = new BufferedWriter( fw );
			
			for (int timeStepCount = 0; timeStepCount < this.simLength*60/this.timeStep; timeStepCount++) {
				
				bw.write("\nTime Step: " + timeStepCount + "\n");
	
				// Determine at each time step which devices are probably on and turn them on, else off
				for(int i = 0; i < this.appArr.length; i++) {
					for(int j = 0; j < this.appArr[i].length; j++) {
						
						rand = (float)Math.random();
						
						if(rand > this.appArr[i][j].getProb()) {
							// turn appliance off
							this.appArr[i][j].toOff();
						}else {
							// turn appliance on
							this.appArr[i][j].toOn();
						}
						
					}
				}
				
				actualWatts = 0;
				
				for (int j = 0; j < this.appArr.length; j++) {
					for(int k = 0; k < this.appArr[j].length; k++) {
						
						actualWatts = actualWatts + appArr[j][k].getWatts();
						
					}	
				}
				
				while(!sa.getLvl().equals("Low") && actualWatts > warningWatts) {
					// smart device is not low and (should be or but findSmart() is not working, thus infinite loop) wattage is over the warning
	
					sa.toLow();
					System.out.println("\nSmart appliances switched to low: \n" + sa.toString());
					bw.write("\nSmart appliances switched to low: \n" + sa.toString());
					
					sa = findSmart();
						
					actualWatts = 0;
					//calculate actual watts
					for (int j = 0; j < this.appArr.length; j++) {
						for(int k = 0; k < this.appArr[j].length; k++) {
							
							actualWatts = actualWatts + appArr[j][k].getWatts();
							
						}	
					}
				}
				
				while(actualWatts > warningWatts) {
					
					smallestLocation = this.findLocale();
					brownOut(smallestLocation);
					locationEffected = (int) smallestLocation - 10000001;
					locationArr[locationEffected]++;
					
					bw.write("\nBrowned out location: " + smallestLocation);
					
					actualWatts = 0;
					
					//calculate actual watts
					for (int j = 0; j < this.appArr.length; j++) {
						for(int k = 0; k < this.appArr[j].length; k++) {
							
							actualWatts = actualWatts + appArr[j][k].getWatts();
							
						}	
					}
					
					brownOutCount++;
					
				}
				
				System.out.println("At time step #" + (timeStepCount+1) + "\nWatts: " + actualWatts + "\n");
				
			}
			
			locationEffected = 0;
			
			for(int i = 0; i < locationArr.length; i++) {
				
				if(locationArr[i] > locationEffected) {
					mostEffectedLocation = i + 10000001;
					locationEffected = locationArr[i];
				}
				
			}
			
			System.out.println("Locations Effected: " + brownOutCount);
			System.out.println("Most Effected Location: " + mostEffectedLocation);
				
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// method for finding smart appliances with highest power consumption
	public SmartAppliance findSmart() {
		
		int topWatts = 0;
		SmartAppliance topSmartApp = new SmartAppliance();
		
		for (int j = 0; j < this.appArr.length; j++) {
			for(int k = 0; k < this.appArr[j].length; k++) {
				if(this.appArr[j][k].getThisType().equals("Smart")) {
					if(topWatts < this.appArr[j][k].getWatts()) {
						topSmartApp = (SmartAppliance)this.appArr[j][k];
						topWatts = this.appArr[j][k].getWatts();
					}
				}
			}
		}
		
		return topSmartApp;
		
	}
	
	// method for brown out
	public void brownOut(long location) {
		
		for(int i = 0; i < appArr.length; i++) {
			for (int j = 0; j < appArr[i].length; j++) {
				
				if(appArr[i][j].getAcct() == location) {
					appArr[i][j].toOff();
				}
			}
		}
		
		System.out.println("Browned out location: " + location);
		
	}
	
	// method for finding locations with smallest wattage intake
	public long findLocale() {
		
		long locale = 0;
		int intake = 0;
		int currentIntake = 0;
		
		for(int i = 0; i < this.appArr.length; i++) {
			
			for(int j = 0; j < appArr[i].length; j++) {
				currentIntake = currentIntake + appArr[i][j].getWatts();
			}
			
			if(intake < currentIntake) {
				locale = this.appArr[i][0].getAcct();
				intake = currentIntake;
			}
			currentIntake = 0;
			
		}
		
		return locale;
		
	}

}
