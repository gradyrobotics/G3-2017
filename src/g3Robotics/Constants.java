package g3Robotics;

/**
 *
 * @author Pratik
 */
public class Constants {

	//PWM Outputs
	public static final int leftDrivePWM = 1; 
	public static final int rightDrivePWM = 0; 
	public static final int shooterMotorPWM_1 = 2;
	public static final int shooterMotorPWM_2 = 3;

	
	//Solenoid Outputs
	public static final int shifterSolenoid = 0;
	
	//Relay Outputs
	
	//Digital Inputs
	public static final int leftDriveDI1 = 0;
	public static final int leftDriveDI2 = 1;
	public static final int rightDriveDI1 = 2;
	public static final int rightDriveDI2 = 3;
	
	public static final int bannerSensorPWM = 4;
	
	//Analog Inputs
	
	//Other Constants
	public static final long LoopPeriodMs = 10L;
    public static final double LoopPeriodS = (double)LoopPeriodMs/1000.0;
	
    //Vision
    
    //Miscellaneous Quantities
    public static final double WheelSizeIn = 4;
	
}
