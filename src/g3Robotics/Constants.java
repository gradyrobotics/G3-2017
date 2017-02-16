package g3Robotics;

/**
 *
 * @author Pratik and Alex!
 */
public class Constants {

	//PWM Outputs
	public static final int leftDrivePWM = 0; 
	public static final int rightDrivePWM = 1; 
	public static final int intakeMotorPWM = 7;
	public static final int climberMotorPWM = 2;
	
	public static final int shooterMotorsPWM = 3;
	public static final int shooterTransportPWM = 5;
	public static final int cycloneMotorPWM = 4;
	public static final int ballPathMotorPWM = 6;

	//Solenoid Outputs
	public static final int shifterSolenoid = 0;
	public static final int intakeSolenoid_1 = 3;
	public static final int intakeSolenoid_2 = 4;
	public static final int shooterHoodSolenoid_1 = 1;
	public static final int shooterHoodSolenoid_2 = 2;
	
	//Relay Outputs
	public static final int lightRelay = 0;
	
	//Digital Inputs
	public static final int leftDriveDI1 = 10;
	public static final int leftDriveDI2 = 11;
	public static final int rightDriveDI1 = 12;
	public static final int rightDriveDI2 = 13;
	
	public static final int bannerSensorPWM = 5;
	
	//Analog Inputs
	
	//Other Constants
	public static final long LoopPeriodMs = 10L;
    public static final double LoopPeriodS = (double)LoopPeriodMs/1000.0;
	
    //Vision
    
    //Miscellaneous Quantities
    public static final double WheelSizeIn = 4;
	
}
