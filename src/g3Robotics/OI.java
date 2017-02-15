package g3Robotics;

import g3Robotics.subsystems.*;
import g3Robotics.utilities.*;

public class OI 
{
	private static OI instance = null;
	private final Drive mDrive;
	private final Shooter mShooter;
	private final Intake mIntake;
	private final Climber mClimber;
	public XboxController driverGamepad;
	public XboxController operatorGamepad;
	private double speedCommand, turnCommand;
	public boolean lastButtonState = false;
	public boolean currentButtonState = false;
	
	public static OI getInstance()
    {
        if( instance == null )
        {
            instance = new OI();
        }
        return instance;
    }
	
	private OI()
	{
		//Initialize subsystems
		mDrive = Drive.getInstance();
		mShooter = Shooter.getInstance();
		mIntake = Intake.getInstance();
		mClimber = Climber.getInstance();
		
		//Initialize gamepads
		driverGamepad = new XboxController(0);
		operatorGamepad = new XboxController(1);
	}

	public void processInputs()
	{
		/**
		 *  PILOT 
		 * 
		 *  This outlines the inputs received from the robot driver.*/
		
		speedCommand = G3Math.applyDeadband(driverGamepad.getLeftYAxis(), 0.2);
		turnCommand = G3Math.applyDeadband(driverGamepad.getRightXAxis(), 0.2);
		
		if (speedCommand > 0) {
			speedCommand = Math.pow(speedCommand,2);
		} else {
			speedCommand = (-1.) * Math.pow(speedCommand,2); 
		}
		
		if (turnCommand > 0) {
			turnCommand = Math.pow(turnCommand,2);
		} else {
			turnCommand = (-1.) * Math.pow(turnCommand,2); 
		}
		//speedCommand = Math.pow(speedCommand, 2);
		//turnCommand = Math.pow(turnCommand, 2);
		mDrive.driveSpeedTurn(speedCommand, turnCommand);
		
		if (driverGamepad.getLB())
		{
			mDrive.highGear();
		}
		else
		{
			mDrive.lowGear();
		}
		
		//Overly complicated state machine for the LED ring
		if(driverGamepad.getAButton())
		{
			currentButtonState = true;
		}
		else
		{
			currentButtonState = false;
		}
		
		if (currentButtonState && mDrive.getLightState() && !lastButtonState)
		{
			mDrive.lightsOff();
		}
		else if (currentButtonState && !mDrive.getLightState() && !lastButtonState)
		{
			mDrive.lightsOn();
		} 
		
		lastButtonState = currentButtonState;
		
		/**
		 *  OPERATOR
		 *  This outlines the input received from the robot operator 
		 *  (every subsystem that isn't the drivetrain).
		 *  
		 *  Every value here as of 2/11/17 needs to be tuned when the practice robot runs.
		*/
		
		//Change shooter hood angle (only two states)
		if (operatorGamepad.getRB()){
			mShooter.setLargeAngle();
		}
		else if (operatorGamepad.getLB()){
			mShooter.setSmallAngle();
		}
		
		if (operatorGamepad.getLeftTrigger())
		{
			mShooter.setWheels(0.5);
		}
		else
		{
			mShooter.setWheels(0.0);
		}
		//Run the shooter wheels to speed
//		if (operatorGamepad.getLeftTrigger())
//		{
//			//The values for the constant wheel speed shot need to be tuned
//			mShooter.setWheels(1300, 0.5, 0.7);
//		}
//		else {
//			mShooter.setWheels(0, 0, 0);
//		}
		
		//Fire the fuel
		if(operatorGamepad.getRightTrigger())
		{
			//These values need to be tuned
			mShooter.setBallPath(0.5);
			mShooter.setCyclone(-0.5);
			mShooter.setTransport(-0.5);
		}
		else {
			mShooter.setBallPath(0.0);
			mShooter.setCyclone(0.0);
			mShooter.setTransport(0.0);
		}

		//Preload balls
		if(operatorGamepad.getBButton())
		{
			mShooter.setBallPath(0.4);
			mShooter.setCyclone(0.4);
		}
		else
		{
			mShooter.setBallPath(0.0);
			mShooter.setCyclone(0.0);
		}
		
		//Raise and lower intake
		if(operatorGamepad.getYButton())
		{
			if(mIntake.getState() == true){
				mIntake.deploy();
				mIntake.setSpeed(0.6);
			}
			else if(mIntake.getState() == false){
				mIntake.raise();
				mIntake.setSpeed(0.0);
			}
		}
		//Run climber; hold down to run
		if(operatorGamepad.getXButton())
		{
			mClimber.setSpeed(0.8);
		}
		else
		{
			mClimber.setSpeed(0.0);
		}
		
	}
}
