package simulation.service.classes;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class ApplianceGenerator {
	
	int numLoc;
	int numDev;
	
	public ApplianceGenerator(int newNumLoc, int newNumDev) {
		this.numLoc = newNumLoc;
		this.numDev = newNumDev;
	}
	
	public static class ApplianceGen {
		public String name;
		public int onW, offW; 
		public double probOn; 
		public boolean smart; 
		public double probSmart;

		public ApplianceGen (String n, int on, int off, double pOn, boolean s, double pSmart)
		{ name=n; onW=on; offW=off; probOn=pOn; smart=s; probSmart=pSmart; }

		public String toString () {
			return name + "," + onW + "," + offW + "," + probOn + "," + smart + "," + probSmart;
		}
	}

	public void generateFile() throws IOException {
		ApplianceGen [] app = new ApplianceGen[100];  // default 100 possible appliance types
		File inputFile = new File( "/Users/danieljsonger/eclipseIDE-workspace/danielsongerCS116v_3/Project/simulation/service/classes/ApplianceDetail.txt" );
		Scanner scan = new Scanner( inputFile );
		int count=0;
		while ( scan.hasNext( ) ) {
			StringTokenizer stringToken = new StringTokenizer(scan.nextLine());
			app[count] = new ApplianceGen(stringToken.nextToken(","),
						Integer.parseInt(stringToken.nextToken(",")),
						Integer.parseInt(stringToken.nextToken(",")),
						Double.parseDouble(stringToken.nextToken(",")),
						Boolean.parseBoolean(stringToken.nextToken(",")),
						Double.parseDouble(stringToken.nextToken()));
			count++;
		}
/*
output a comma delimited file
the location (represented by an 8 digit numeric account number)
type (string)
"on" wattage used (integer)
probability (floating point, i.e..01=1%) that the appliance is "on" at any time
smart (boolean) 
Smart appliances (if "on") power reduction percent when changed to "low" status(floating point, i.e..33=33%).
*/
		try
		{
			FileWriter fw = new FileWriter( "output.txt", false);
			BufferedWriter bw = new BufferedWriter( fw );
			for (long location=1;location<=this.numLoc ;location++ ) {   // default 100 locations
				int randLength = (int) (this.numDev/this.numLoc);
				if(Math.random() >= .5) {
					randLength += (int)(Math.random()*.5*numDev/this.numLoc);
				}else {
					randLength = randLength - (int)(Math.random()*.5*this.numDev/this.numLoc) - 1;
				}  //15-20 appliances per location 
				for (int i=1;i<=randLength;i++ ){
					int index=(int)(Math.random()*count);  // pick an appliance randomly
					bw.write(String.valueOf(10000000+location));
					bw.write( "," );		
					bw.write(app[index].name);
					bw.write( "," );		
					bw.write(String.valueOf(app[index].onW));
					bw.write( "," );									
					bw.write(String.valueOf(app[index].probOn));
					bw.write( "," );		
					bw.write(String.valueOf(app[index].smart));
					bw.write( "," );
					bw.write(String.valueOf(app[index].probSmart));
					bw.newLine( );
					bw.flush();
				}
			}
		}
		catch( IOException ioe )
		{
			ioe.printStackTrace( );
		}
	}
}