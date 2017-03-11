package g3Robotics.logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.Shooter;

public class Logger {
	private static Logger instance = null;
	private String path = "/media/sda1/ShooterData.txt";
	private Shooter mShooter = Shooter.getInstance();
	private StringBuilder outString;
	private PrintWriter writer;
	private long startTime;
	public boolean isEnabled;
	
	public static Logger getInstance() {
		if (instance == null) {
			return new Logger();
		} else {
			return instance;
		}
	}
	
	private Logger() {
		try {
			writer = new PrintWriter(new File(path));
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found. Ask Pratik for more details.");
		}
		outString = new StringBuilder();
		outString.append("Time");
		outString.append(", ");
		outString.append("Data");
		outString.append('\n');
		startTime = System.currentTimeMillis();
	}
	
	public void log (){
		outString.append(System.currentTimeMillis() - startTime);
		outString.append(", ");
		outString.append(mShooter.getSpeed());
		outString.append('\n');
		System.out.println(outString.toString());
		writer.write(outString.toString());
		writer.flush();
		outString = new StringBuilder();
		
	}
	
	public void writeLog() {
		writer.write(outString.toString());
		writer.flush();
		writer.close();
	}
}
