package g3Robotics;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;
import g3Robotics.utilities.*;
import g3Robotics.vision.Vision;

public class OI 
{
	private static OI instance = null;
	private final Drive mDrive;
	private final Shooter mShooter;
	private final Intake mIntake;
	private final Climber mClimber;
	private final Vision mVision;
	public XboxController driverGamepad;
	public XboxController operatorGamepad;
	private double speedCommand, turnCommand;
	public boolean lastButtonState = false;
	public boolean currentButtonState = false;
	public boolean currentInvertState = false;
	public boolean lastInvertState = false;
	public boolean lastPlateButtonState = false;
	public boolean currentPlateButtonState = false;
	
	private Timer timer;
	private Timer backupTimer;
	private Timer lineUpTimer;
	private double backupStartTime = 0;
	private boolean isBackupStartTimeSet = false;
	private double startTime = 0;
	private boolean isStartTimeSet = false;
	private boolean isLiningUp = false;
	private boolean isBackingUp = false;
	private boolean isLineUpTimeSet = false;
	public double lineUpStartTime = 0.0;
	
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
		mVision = Vision.getInstance();
		
		//Initialize gamepads
		driverGamepad = new XboxController(0);
		operatorGamepad = new XboxController(1);
		timer = new Timer();
		backupTimer = new Timer();
		lineUpTimer = new Timer();
		timer.start();
		backupTimer.start();
		lineUpTimer.start();
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
			mDrive.setOpenLoop(true);
		} else if (speedCommand < 0){
			speedCommand = (-1) * Math.pow(speedCommand,2); 
			mDrive.setOpenLoop(true);
		}
		
		if (turnCommand > 0) {
			turnCommand = Math.pow(turnCommand,3);
			mDrive.setOpenLoop(true);

		} else if (turnCommand < 0){
			turnCommand = Math.pow(turnCommand,3);
			mDrive.setOpenLoop(true);
		}
		
		if (driverGamepad.getRightTrigger()){
			mDrive.setOpenLoop(false);
			lineUpTimer.reset();
		}
		else{
			mDrive.setOpenLoop(true);
			lineUpTimer.reset();
		}
		
//		if (driverGamepad.getRightTrigger()){
//			lineUpTimer.reset();
//			mDrive.setOpenLoop(false);
//			mDrive.setLiningUp(true);
//			
//		} else{
//			mDrive.setOpenLoop(true);
//			mDrive.setLiningUp(false);
//		}
		//speedCommand = Math4.pow(speedCommand, 2);
		//turnCommand = Math.pow(turnCommand, 2);
		
		if(mDrive.isOpenLoop()){
			mDrive.driveSpeedTurn(speedCommand, turnCommand);
		}
		else{
			if(!mDrive.isLiningUp() || lineUpTimer.get() < 1.0)
				mDrive.lineUpGoal();
			else
				mDrive.brake();
		}
		
		if(driverGamepad.getLeftTrigger()){
			if(!isBackupStartTimeSet){
				backupStartTime = backupTimer.get();
				isBackupStartTimeSet = true;
			}
				if(backupTimer.get() - backupStartTime < 0.1){
					mDrive.driveSpeedTurn(-0.8, 0.0);
				}
				else {
					mDrive.driveSpeedTurn(0.0, 0.0);
				}
		}
		else {
			isBackupStartTimeSet = false;
			backupTimer.reset();
		}
		
		if(driverGamepad.getLB()){
			mDrive.highGear();
		}
		else {
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
		
		if(driverGamepad.getBButton())
		{
			currentInvertState = true;
		}
		else
		{
			currentInvertState = false;
		}
		
		if (currentInvertState && !lastInvertState)
		{
			mDrive.invert();
		}
		 
		
		lastInvertState = currentInvertState;
		
		/**
		 *  OPERATOR
		 *  This outlines the input received from the robot operator 
		 *  (every subsystem that isn't the drivetrain).
		 *  
		*/
		
				//Run the shooter wheels to speed
		if (operatorGamepad.getLeftTrigger())
		{
			//The values for the constant wheel speed shot need to be tuned
			if(!isStartTimeSet){
				startTime = timer.get();
				isStartTimeSet = true;
			}
				if(timer.get() - startTime < 1.0){
					mShooter.setConstantWheels(0.9);
				}
				else {
					//mShooter.setConstantWheels(-0.9);
					mShooter.setWheels(3075, 0.4, 1.0);
					//mShooter.setPWheels(3000);
				}
		}
			
		else {
			isStartTimeSet = false;
			timer.reset();
			
			mShooter.setWheels(0, 0, 0);
		}
		
		//Fire the fuel
		if(operatorGamepad.getRightTrigger())
		{
			if(mShooter.isTargetSpeed()){
				mShooter.setBallPath(0.8);
				mShooter.setCyclone(-0.5);
				mShooter.setTransport(1.0);
			}
		}
		else
		{
			mShooter.setBallPath(0.0);
			mShooter.setCyclone(0.0);
			mShooter.setTransport(0.0);
		}
		
		if (operatorGamepad.getLB()){
			mDrive.lowerPlate();
		}
		else if (operatorGamepad.getRB()){
			mDrive.raisePlate();
		}
		
		//Raise and lower intake
		if(operatorGamepad.getYButton()){
				//mIntake.deploy();
				mIntake.setSpeed(1.0);
		}
		else if(operatorGamepad.getBButton()){
				//mIntake.raise();
				mIntake.setSpeed(0.0);
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
		
		//Open/close the gear mech pistons
		if(operatorGamepad.getStartButton())
		{
			mShooter.setLargeAngle();
		}
		else if (operatorGamepad.getBackButton())
		{
			mShooter.setSmallAngle();
		}
	}
	
	public double getSpeedCommand(){
		return speedCommand;
	}
}
