package simulation.client.classes;

import simulation.service.classes.*;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SimulationClient {
	
	public static void main(String [] args) {

		Scanner keyboard = new Scanner(System.in);		
		int numLocations = 100;
		
		
		// Appliance generator portion
		System.out.println("Beginning appliance generator portion.");
		
		System.out.println("Type \"D\" to run a default simulation");
		System.out.println("Type \"S\" to specify a simulation");
		String nav5 = keyboard.next();
		
		System.out.println("Enter the number of devices to generate:");
		int numDevices = Integer.parseInt(keyboard.next());
		
		while(numDevices < 0) {
			System.out.println("Try again: ");
			numDevices = Integer.parseInt(keyboard.next());
		}
		
		System.out.println("Enter the warning wattage for this simulation: ");
		int warning = Integer.parseInt(keyboard.next());
		
		if(nav5.equals("S")) {
			
			System.out.println("Enter the number of locations");
			numLocations = Integer.parseInt(keyboard.next());
			
		}
		
		ApplianceGenerator ag = new ApplianceGenerator(numLocations, numDevices);
		try {
			ag.generateFile();
		}catch (IOException e){
			e.printStackTrace();
		}
		
		// simulation portion
		String[] toks;
		long acct;
		String type;
		int onW;
		float prob;
		boolean isItSmart;
		double percentToLow;
		int lowW;
		String choice;
		int locationCount = 0;
		int appCount = 0;
		Appliance[][] appArr = new Appliance[numLocations][0];
		Appliance[] dummyArr;
		Appliance arrApp;
		long enterLocation = 0;
		String enterType = "";
		int enterOnWatts = 0, enterLowWatts = 0;
		float enterProb = 0;
		boolean enterSmart = false;
		SmartAppliance sa;
		RegularAppliance ra;
		int useNewLocation = 0;
		Appliance a;
		int deleteID = 0;
		int deleteCount = 0;
		int deleteIndex1 = 0;
		int deleteIndex2 = 0;
		
		
		System.out.println("Type \"E\" if you'd like to manually enter appliances.");
		System.out.println("Type \"D\" if you'd like to manually delete appliances.");
		System.out.println("Type \"Q\" if you'd like to quit and continue.");
		
		choice = keyboard.next();
		
		try {
			
			File file = new File("/Users/danieljsonger/eclipseIDE-workspace/danielsongerCS116v_3/output.txt");
			Scanner appReader = new Scanner(file);
			
			while(appReader.hasNext()){
				
				toks = appReader.nextLine().split(",");
				
				acct = (long)Integer.parseInt(toks[0]);
				type = toks[1];
				onW = Integer.parseInt(toks[2]);
				prob = Float.parseFloat(toks[3]);
				isItSmart = Boolean.parseBoolean(toks[4]);
				if (!isItSmart) {
					ra = new RegularAppliance(type, onW, 0, acct, prob, "Off");
					
					// think of how to store in a jagged array.
					arrApp = (Appliance)ra;
					
				}else{
					percentToLow = Double.parseDouble(toks[5]);
					lowW = (int)(onW*percentToLow);
					sa = new SmartAppliance(type, onW, 0, acct, prob, "Off", lowW);
					
					// think of how to store in a jagged array.
					arrApp = (Appliance)sa;
					
				}
				if(appCount >= 1 && arrApp.getAcct() != appArr[locationCount][appCount-1].getAcct()) {
					locationCount++;
					appCount = 0;
				}
				if(appCount >= appArr[locationCount].length) {
					dummyArr = new Appliance[appCount];
					for (int i = 0; i < appCount; i++) {
						dummyArr[i] = appArr[locationCount][i];
					}
					appArr[locationCount] = new Appliance[appCount+1];
					for(int l = 0; l < appCount; l++) {
						appArr[locationCount][l] = dummyArr[l];
					}
				}
				appArr[locationCount][appCount] = arrApp;
				appCount++;
				
			}
			
			appReader.close();
			
		}catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		}
		
		if(choice.equals("E")) {
			
			// give user the ability to enter manually
			System.out.println("Enter the type of the device to be added: ");
			enterType = keyboard.next();
			enterType = keyboard.nextLine();
			
			System.out.println("Enter the location of the device to be added: ");
			enterLocation = (long) Integer.parseInt(keyboard.next());
			
			System.out.println("Enter the on wattage of the device to be added: ");
			enterOnWatts = Integer.parseInt(keyboard.next());
			
			System.out.println("Enter the probablility that the device to be added is on: ");
			enterProb = Float.parseFloat(keyboard.next());
			
			System.out.println("Enter the \"true\" if the device to be added is smart, else, enter \"false\": ");
			enterSmart = Boolean.parseBoolean(keyboard.next());
			
			if(enterSmart) {
				
				System.out.println("Enter the low wattage of the device to be added: ");
				enterLowWatts = Integer.parseInt(keyboard.next());
				
				sa = new SmartAppliance(enterType,enterOnWatts,0,enterLocation,enterProb,"Off",enterLowWatts);
				a = (Appliance)sa;
				
			}else {
				
				ra = new RegularAppliance(enterType,enterOnWatts,0,enterLocation,enterProb,"Off");
				a = (Appliance)ra;
			}
			
			useNewLocation = (int)(a.getAcct() - 10000001);
			dummyArr = new Appliance[appArr[useNewLocation].length];
			
			for(int i = 0; i < appArr[useNewLocation].length; i++) {
				dummyArr[i] = appArr[useNewLocation][i];
			}
			appArr[useNewLocation] = new Appliance[dummyArr.length+1];
			for(int i = 0; i < dummyArr.length; i++) {
				appArr[useNewLocation][i] = dummyArr[i];
			}
			appArr[useNewLocation][dummyArr.length] = a;
			
		}else if(choice.equals("D")) {

			// find add and delete (while loop)
			System.out.println("Please enter the name of the ID of the device you would like to delete:");
			deleteID = Integer.parseInt(keyboard.next());
			
			for(int i = 0; i < appArr.length; i++) {
				for (int j = 0; j < appArr[i].length; j++) {
					if(appArr[i][j].getUniqueID() == deleteID) {
						
						deleteIndex1 = i;
						deleteIndex2 = j;
						
					}
				}
			}
			
			dummyArr = new Appliance[appArr[deleteIndex1].length-1];
			for (int k = 0; k < appArr[deleteIndex1].length; k++) {
				if(appArr[deleteIndex1][k].getUniqueID() != deleteID) {
					dummyArr[deleteCount] = appArr[deleteIndex1][k];
					deleteCount++;
				}
			}
			appArr[deleteIndex1] = new Appliance[dummyArr.length];
			for (int k = 0; k < dummyArr.length; k++) {
				appArr[deleteIndex1][k] = dummyArr[k];
			}
			
			
		}
		
		/*int randLength = 0;
		int loc = 0;
		int deviceCount = 0;
		
		// grab number of devices from random locations
		while(deviceCount < numDevices) {
			
			randLength = (int) (numDevices/numLocations);
			if(Math.random() >= .5) {
				randLength += (int)(Math.random()*.5*numDevices/numLocations);
			}else {
				randLength = randLength - (int)(Math.random()*.5*numDevices/numLocations) - 1;
			}
			
			for(int j = 0; j < randLength; j++) {
				
				if(j <= appArr[loc].length) {
					
					if(j >= simArr[loc].length) {
						dummyArr = new Appliance[j];
						for(int k = 0; k < simArr[loc].length; k++) {
							dummyArr[k] = simArr[loc][k];
						}
						simArr[loc] = new Appliance[j+1];
						for(int k = 0; k < dummyArr.length; k++) {
							simArr[loc][k] = dummyArr[k];
						}
					}
					simArr[loc][j] = appArr[loc][j];
					
					deviceCount++;
				}
				
			}
			loc++;
			
		}
		
		int lastDevice;
		
		for(int i = 0; i < simArr.length; i++) {
			lastDevice = simArr[i].length;
			System.out.println(simArr[i][1].toString());
		}*/
		
		String nav1 = "A";
		
		while(!nav1.equals("Q")) {
		
			System.out.println("Type \"S\" for summary reports");
			System.out.println("Type \"L\" for lists");
			System.out.println("Type \"Q\" to quit this portion and start the simulation");
			nav1 = keyboard.next();
			
			if(nav1.equals("S")) {
				
				System.out.println("Type \"T\" for total number of locations");
				System.out.println("Type \"A\" for total number of appliances of each type");
				String nav2 = keyboard.next();
				
				if(nav2.equals("T")){
					System.out.println("No. of Locations: " + appArr.length);
				}else {
					
					int smartCount = 0;
					int regCount = 0;
					
					for(int i = 0; i < appArr.length; i++) {
						for(int j =0; j < appArr[i].length; j++) {
							
							if(appArr[i][j].getThisType().equals("Smart")) {
								smartCount++;
							}else {
								regCount++;
							}
							
						}
					}
					
					System.out.println("No. of Smart Appliances: " + smartCount + "\nNo. of Regular Appliances: " + regCount + "\n");
					
				}
				
			}else if(nav1.equals("L")){
				
				System.out.println("Type \"L\" to list out all Appliances of a certain location");
				System.out.println("Type \"T\" to list out all Appliances of a certain type");
				String nav3 = keyboard.next();
				
				if(nav3.equals("L")) {
					System.out.println("Type the location you want to list out:");
					long locList = (long)Integer.parseInt(keyboard.next());
					int location = (int)locList - 10000001;
					
					for(int i = 0; i < appArr[location].length; i++) {
						System.out.println(appArr[location][i].toString());
					}
					
				}else {
					
					Appliance[] smartArr = new Appliance[1];
					Appliance[] regArr = new Appliance[1];
					Appliance[] tempArr = new Appliance[smartArr.length];
					
					int smartCounter = 0;
					int regCounter = 0;
					
					for(int i = 0; i < appArr.length; i++) {
						for(int j =0; j < appArr[i].length; j++) {
							if(appArr[i][j].getThisType().equals("Smart")) {
								
								if(smartArr.length <= smartCounter) {
									
									tempArr = new Appliance[smartArr.length];
									
									for(int k = 0; k < smartArr.length; k++) {
										tempArr[k] = smartArr[k];
									}
									smartArr = new Appliance[smartCounter + 1];
									for(int k = 0; k < tempArr.length; k++) {
										smartArr[k] = tempArr[k];
									}
									
								}
								
								smartArr[smartCounter] = appArr[i][j];
								smartCounter++;
								
							}else {
								
								if(regArr.length <= regCounter) {
									
									tempArr = new Appliance[regArr.length];
									
									for(int k = 0; k < regArr.length; k++) {
										tempArr[k] = regArr[k];
									}
									regArr = new Appliance[regCounter + 1];
									for(int k = 0; k < tempArr.length; k++) {
										regArr[k] = tempArr[k];
									}
									
								}
								
								regArr[regCounter] = appArr[i][j];
								regCounter++;
								
							}
						}
					}
					
					System.out.println("Type \"S\" to list Smart Appliances");
					System.out.println("Type \"R\" to list Regular Appliances");
					String nav4 = keyboard.next();
					
					if(nav4.equals("S")) {
						for (int l = 0; l < smartArr.length; l++) {
							System.out.println(smartArr[l].toString());
						}
					}else {
						for (int l = 0; l < regArr.length; l++) {
							System.out.println(regArr[l].toString());
						}
					}
				}
				
			}else {
				System.out.println("Quitting navigation portion");
			}
		}
		
		System.out.println("Starting simulation...");
		
		if(nav5.equals("D")) {
			
			Simulation simD = new Simulation(warning,appArr);
			System.out.println(appArr.length + " " + appArr[0].length);
			
			simD.start();
			
		}else {
			
			System.out.println("Enter a length of time, in hours, for the simulation");
			int simLength = Integer.parseInt(keyboard.next());
			
			System.out.println("Enter the length, in minutes, of each time step");
			int simStep = Integer.parseInt(keyboard.next());
			
			Simulation simS = new Simulation(warning,simLength,simStep,appArr);
			
			simS.start();
			
		}
		
		
		keyboard.close();
		
	}

}
