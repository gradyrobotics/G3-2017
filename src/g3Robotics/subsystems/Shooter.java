package g3Robotics.subsystems;

import g3Robotics.Constants;
import edu.wpi.first.wpilibj.*;

public class Shooter extends G3Subsystem {
	private final VictorSP shooterMotors;
	private final VictorSP shooterTransport;
	private final VictorSP cyclone;
	private final VictorSP ballPath;
	private final Solenoid shooterHood;
	
	private Counter counter;
	
	private static Shooter instance = null;
	
	private Shooter()
	{
		shooterMotors = new VictorSP(Constants.shooterMotorsPWM);
		shooterTransport = new VictorSP(Constants.shooterTransportPWM);
		cyclone = new VictorSP(Constants.cycloneMotorPWM);
		ballPath = new VictorSP(Constants.ballPathMotorPWM);
		counter = new Counter(Constants.bannerSensorPWM);
		shooterHood = new Solenoid(Constants.shooterHoodSolenoid_1, Constants.shooterHoodSolenoid_2);
	}
	
	public static Shooter getInstance(){
		if( instance == null )
        {
        	instance = new Shooter();
        }
        return instance;
	}
	
	public void setWheels(double wheelSpeed, double lowMotorPower, double highMotorPower){
		if (getSpeed() < wheelSpeed)
			shooterMotors.set(highMotorPower);
		else
			shooterMotors.set(lowMotorPower);
	}
	
	public void setTransport(double speed){
		shooterTransport.set(speed);
	}
	
	public void setCyclone(double speed){
		cyclone.set(speed);
	}
	
	public void setBallPath(double speed){
		ballPath.set(speed);
	}
	
	public void setLargeAngle(){
		shooterHood.set(true);
	}
	
	public void setSmallAngle(){
		shooterHood.set(false);
	}

    public double getSpeed(){
    	return 60.0/counter.getPeriod(); 
    }
    
}