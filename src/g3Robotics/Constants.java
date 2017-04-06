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
	public static final int climberMotorPWM = 9;
	
	public static final int shooterMotorsPWM = 3;
	public static final int shooterTransportPWM = 5;
	public static final int cycloneMotorPWM = 4;
	public static final int ballPathMotorPWM = 6;

	//Solenoid Outputs
	public static final int shifterSolenoid_1 = 0;
	public static final int shifterSolenoid_2 = 7;
	public static final int intakeSolenoid_1 = 1;
	public static final int intakeSolenoid_2 = 2;
	public static final int shooterHoodSolenoid_1 = 3;
	public static final int shooterHoodSolenoid_2 = 4;
	public static final int humanPlayerPlateSolenoid_1 = 5;
	public static final int humanPlayerPlateSolenoid_2 = 6; 
	
	//Relay Outputs
	public static final int lightRelay = 0;
	
	//Digital Inputs
	public static final int leftDriveDI1 = 6;
	public static final int leftDriveDI2 = 7;
	public static final int rightDriveDI1 = 2;
	public static final int rightDriveDI2 = 3;
	
	public static final int bannerSensorPWM = 5;
	
	//Analog Inputs
	
	//Other Constants
	public static final long LoopPeriodMs = 10L;
    public static final double LoopPeriodS = (double)LoopPeriodMs/1000.0;
	
    //Vision
    
    //Miscellaneous Quantities
    public static final double WheelSizeIn = 4;
	
}
