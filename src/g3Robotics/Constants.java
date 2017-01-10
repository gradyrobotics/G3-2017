package g3Robotics;

/**
 *
 * @author Pratik
 */
public class Constants {

	//PWM Outputs
	public static final int leftDrivePWM1 = 5; //changed from 2 to 1
	public static final int rightDrivePWM1 = 10;
	public static final int leftDrivePWM2 = 6;
	public static final int rightDrivePWM2 = 4; //This is the correct value 
	
	//Solenoid Outputs
	
	//Relay Outputs
	
	//Digital Inputs
	
	//Analog Inputs
	
	//Other Constants
	public static final long LoopPeriodMs = 10L;
    public static final double LoopPeriodS = (double)LoopPeriodMs/1000.0;
	
    
    //Miscellaneous Quantities
    public static final double WheelSizeIn = 6;
	
}
