package g3Robotics.subsystems;

import g3Robotics.Constants;
import g3Robotics.utilities.*;
import edu.wpi.first.wpilibj.*;

public class Shooter extends G3Subsystem {
	private final VictorSP shooterMotors;
	private final VictorSP shooterMotors2;
	private final VictorSP shooterTransport;
	private final VictorSP cyclone;
	private final VictorSP ballPath;
	private final DoubleSolenoid shooterHood;
	
	public double targetSpeed;
	
	private Counter counter;
	private double mSetpoint;
	private boolean isTargetSpeed;
	
	private static Shooter instance = null;
	private VelocityPID wheelController;
	
	private Shooter()
	{
		counter = new Counter(Constants.bannerSensorPWM);

		shooterMotors = new VictorSP(Constants.shooterMotorsPWM);
		shooterMotors2 = new VictorSP(Constants.intakeMotorPWM);
		shooterTransport = new VictorSP(Constants.shooterTransportPWM);
		cyclone = new VictorSP(Constants.cycloneMotorPWM);
		ballPath = new VictorSP(Constants.ballPathMotorPWM);
		shooterHood = new DoubleSolenoid(Constants.shooterHoodSolenoid_1, Constants.shooterHoodSolenoid_2);
		
		wheelController = new VelocityPID(0.4, 0.0, 0.0);
	}
	
	public static Shooter getInstance(){
		if( instance == null )
        {
        	instance = new Shooter();
        }
        return instance;
	}

	
	public void setWheels(double wheelSpeed, double lowMotorPower, double highMotorPower){
		if (getSpeed() < wheelSpeed){
			shooterMotors.set(-highMotorPower);
			shooterMotors2.set(highMotorPower);
		}
		else{
			shooterMotors.set(-lowMotorPower);
			shooterMotors2.set(lowMotorPower);
		}
		targetSpeed = wheelSpeed;
		
	}
	
	public void setPWheels(double setpoint){
		mSetpoint = setpoint;
		wheelController.setSetpoint(mSetpoint);
		shooterMotors.set(wheelController.calculate(getSpeed()));
		
	}
	
	public void setConstantWheels(double speed)
	{
		shooterMotors2.set(speed);
		shooterMotors.set(-speed);
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
		shooterHood.set(DoubleSolenoid.Value.kForward);
	}
	
	public void setSmallAngle(){
		shooterHood.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void brake(){
		shooterTransport.set(0.0);
		cyclone.set(0.0);
		ballPath.set(0.0);
	}

    public double getSpeed(){
    	return 60.0/counter.getPeriod(); 
    }
    
    public boolean isTargetSpeed(){
    	if (getSpeed() >= targetSpeed && getSpeed() < targetSpeed + 50){
    		isTargetSpeed = true;
    	} else{
    		isTargetSpeed = false;
    	}
    	return isTargetSpeed;
    }
    
}