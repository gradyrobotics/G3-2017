package g3Robotics;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import g3Robotics.autonomous.*;
import g3Robotics.fileio.*;
import g3Robotics.logger.Logger;
import g3Robotics.vision.*;
import g3Robotics.subsystems.*;
import g3Robotics.loops.*;
import g3Robotics.paths.*;
import g3Robotics.trajectorylib.*;
import g3Robotics.utilities.XboxController;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    //SendableChooser chooser;
	
    Vision mVision;
    Shooter mShooter;
    Drive mDrive;
    OI mOI;
    Logger mLogger;
    TrajectoryDriveController mDrivebaseController;
    
    PropertyReader mPropertyReader;
    PropertySet mProperties;
    Path testPath;
    double heading;
    
    String autonomousName = "";
    
    StateMachine mAutonomousStateMachine;
    boolean mLastIterationButtonState = false;
    boolean resetState = false; 
    boolean wasEnabledFlag = true;
    int buttonCounter;
    
    Timer timer;
    int autoStepNumber = 1;
    
    public void robotInit() {
    	
    	mDrive = Drive.getInstance();
    	mOI = OI.getInstance();
    	mShooter = Shooter.getInstance();	
    	mLogger = Logger.getInstance();
    	//mVision = Vision.getInstance();
    	mDrivebaseController = new TrajectoryDriveController();
    	
    	mDrive.calibrate();
    	mDrive.reset();
    	
    	mVision = Vision.getInstance();
        mVision.VisionInit();
        mVision.findTarget();
        
        mPropertyReader = new PropertyReader();
        mProperties = PropertySet.getInstance();
        timer = new Timer();
        
    }
    
    /**
     * Function that is called periodically while robot is disabled. 
     * Publishing to smart dashboard here will be a live status of the 
     * robot in disabled mode. 
     */
    public void disabledPeriodic() {
    	logToDashboard();
    	//mVision.findTarget();
    	
		if(mOI.driverGamepad.getYButton() && !mLastIterationButtonState)
    	{
    		buttonCounter++;
    		readAutoMode(buttonCounter);
    		
	    	if(buttonCounter > 8)
	    	{
	    		buttonCounter = 0;
	    	}

    	//mAutonomousStateMachine = new StateMachine(new AutonomousParser().parseStates());
    	}
		
		if(mOI.driverGamepad.getBButton() && !resetState)
		{
	   		 mDrive.reset();
	   		 mDrive.zeroGyro();
	   		 //mPropertyReader.parseFile("/home/lvuser/properties.txt");
	   		 //loadAllProperties();
	   	} 
   	 	mLastIterationButtonState = mOI.driverGamepad.getYButton();
   	 	resetState  = mOI.driverGamepad.getBButton();
    }
    
    public void disabledInit() {
    	//mLogger.isEnabled = false;
//    	if (wasEnabledFlag) {
//    		mLogger.writeLog();
//
//    		wasEnabledFlag = false;
//    		System.out.println("Wrote to file");
//    	}
    	
    }
    
    public void autonomousInit() {
    	timer.start();
    	mDrive.reset();
    	timer.reset();
    	mDrive.zeroGyro();	
    	autoStepNumber = 1;
    	//wasEnabledFlag = true;
    	
    }

    /** 
     *This function is called periodically during autonomous  
     */
    
    public void autonomousPeriodic() {
    	//mAutonomousStateMachine.run();
    	//timer.start();
    	logToDashboard();
    	if(autonomousName.equals("Drive Forward")) {
	    	if(timer.get() < 4.0){
	    		mDrive.driveSpeedTurn(1.0, 0.0);
	    	}
	    	else {
	    		mDrive.driveSpeedTurn(0.0, 0.0);
	    		timer.stop();
	    	}
    	} else if(autonomousName.equals("Trajectory Test")){
    		mDrive.reset();
    		testPath = LoadAutoPaths.get("TestPath");
    	    mDrivebaseController.loadProfile(testPath.getLeftWheelTrajectory(), testPath.getRightWheelTrajectory(), 1.0, heading);
    	    mDrivebaseController.enable();
    	    if(mDrivebaseController.isEnabled())
    	    	mDrivebaseController.run();
    	    else
    	    	mDrive.brake();
    	} else if (autonomousName.equals("Ten Balls Blue")){
    		switch(autoStepNumber) {
    		case 1:
    			if (mShooter.getSpeed() < 3250){
    				mShooter.setWheels(3200, 0.0, 1.0);
    			}
    			else{
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		case 2:
    			if (timer.get() < 3.0){
    				mShooter.setWheels(3250, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
    				mShooter.setCyclone(0.5);
    				mShooter.setTransport(-1.0);
    			}
    			else{
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		case 3:
    			if (timer.get() < 0.5 && timer.get() >= 0.0){
    				mShooter.setWheels(3250, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
    				mShooter.setCyclone(0.5);
    				mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 1.25 && timer.get() >= 0.5){
    				mShooter.brake();
    			}
    			else if (timer.get() < 1.75 && timer.get() >= 1.25){
    				mShooter.setWheels(3250, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 2.5 && timer.get() >= 1.75){
    				mShooter.brake();
    			}
    			else if (timer.get() < 3.0 && timer.get() >= 2.5){
    				mShooter.setWheels(3250, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 3.75 && timer.get() >= 3.0){
    				mShooter.brake();
    			}
    			else if (timer.get() < 4.25 && timer.get() >= 3.75){
    				mShooter.setWheels(3250, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 5.0 && timer.get() >= 4.25){
    				mShooter.brake();
    			}
    			else if (timer.get() < 5.5 && timer.get() >= 5.0){
    				mShooter.setWheels(3250, 0.0, 1.0);
					mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 6.25 && timer.get() >= 5.5){
    				mShooter.brake();
    			}
    			else if (timer.get() < 6.75 && timer.get() >= 6.25){
    				mShooter.setWheels(3250, 0.0, 1.0);
					mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 7.0 && timer.get() >= 6.75){
    				mShooter.brake();
    			}
    			else if (timer.get() >= 7.0){
    				mShooter.brake();
    				mShooter.setWheels(0.0, 0.0, 0.0);
    				mDrive.reset();
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		case 4:
    			if(Math.abs(mDrive.getGyroAngle()) < 90){
    				mDrive.driveSpeedTurn(0.0, -0.8);
    			}
    			else{
    				System.out.println("Finished turning step.");
    				mDrive.brake();
    				mDrive.reset();
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		case 5:
    			if (timer.get() < 2.0){
    				System.out.println("In driving step.");
    				mDrive.driveSpeedTurn(1.0, 0.0);
    			}
    			else{
    				mDrive.brake();
    				mDrive.reset();
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		default:
    			mDrive.brake();
    		}
    	}
    	else if (autonomousName.equals("Ten Balls Red")){
    		System.out.print(timer.get());
    		switch(autoStepNumber) {
    		case 1:
    			if (mShooter.getSpeed() < 3100){
    				mShooter.setWheels(3100, 0.0, 1.0);
    			}
    			else{
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		case 2:
    			if (timer.get() < 3.0){
    				mShooter.setWheels(3100, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
    				mShooter.setCyclone(0.5);
    				mShooter.setTransport(-1.0);
    			}
    			else{
    				timer.reset();
    				autoStepNumber++;
    			}
    			break;
    		case 3:
    			if (timer.get() < 0.5 && timer.get() >= 0.0){
    				mShooter.setWheels(3100, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
    				mShooter.setCyclone(0.5);
    				mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 1.25 && timer.get() >= 0.5){
    				mShooter.brake();
    			}
    			else if (timer.get() < 1.75 && timer.get() >= 1.25){
    				mShooter.setWheels(3100, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 2.5 && timer.get() >= 1.75){
    				mShooter.brake();
    			}
    			else if (timer.get() < 3.0 && timer.get() >= 2.5){
    				mShooter.setWheels(3100, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 3.75 && timer.get() >= 3.0){
    				mShooter.brake();
    			}
    			else if (timer.get() < 4.25 && timer.get() >= 3.75){
    				mShooter.setWheels(3100, 0.0, 1.0);
    				mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 5.0 && timer.get() >= 4.25){
    				mShooter.brake();
    			}
    			else if (timer.get() < 5.5 && timer.get() >= 5.0){
					mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 6.25 && timer.get() >= 5.5){
    				mShooter.brake();
    			}
    			else if (timer.get() < 6.75 && timer.get() >= 6.25){
					mShooter.setBallPath(0.8);
					mShooter.setCyclone(0.5);
					mShooter.setTransport(-0.8);
    			}
    			else if (timer.get() < 7.0 && timer.get() >= 6.75){
    				mShooter.brake();
    			}
    			else if (timer.get() >= 7.0) {
    				mShooter.brake();
    				mShooter.setWheels(0.0, 0.0, 0.0);
    				mDrive.reset();
    				timer.reset();
    				System.out.println("Finished shooter step.");
    				autoStepNumber++;
    			}
    			break;
    		case 4:
    			System.out.println("Starting driving step.");
    			if (timer.get() < 2.0){
    				System.out.println("In driving step.");
    				mDrive.driveSpeedTurn(1.0, 0.0);
    			}
    			else {
    				mDrive.brake();
    				System.out.println("Finished driving step.");
    				mDrive.reset();
    				autoStepNumber++;
    			}
    			break;
    		default:
    			mDrive.brake();
    		}
    	}
    	else if (autonomousName.equals("Gear Middle")) {
    		if (timer.get() < 4.0) {
    			mDrive.driveSpeedTurn(0.8, -0.05 * mDrive.getGyroAngle());
    		} else {
    			mDrive.brake();
    			timer.stop();
    		}
    	} else if(autonomousName.equals("Gear Left Side Blue")) {
    		switch(autoStepNumber) {
    			case 1: 
    				if(Math.abs(mDrive.getLeftDistance()) <= 31) {
    					mDrive.driveSpeedTurn(1.0, 0.0);
    				} else {
    					mDrive.brake();
    					mDrive.reset();
    					mDrive.zeroGyro();
    					autoStepNumber++;
    				}
    				break;
    			case 2:
    				if(Math.abs(mDrive.getGyroAngle()) <= 42) {
    					mDrive.driveSpeedTurn(0.0, 0.8);
    				} else {
    					mDrive.brake();
    					mDrive.reset();
    					autoStepNumber++;
    				}
    				break;
    			case 3: 
    				if(Math.abs(mDrive.getLeftDistance()) <= 60) {
    					mDrive.driveSpeedTurn(1.0, 0.0);
    				} else {
    					mDrive.brake();
    					mDrive.zeroGyro();
    					mDrive.reset();
    					autoStepNumber++;
    				}
    				break;
    			case 4:
    				mDrive.brake();
    				break;
    			default: 
    				mDrive.brake();
    		}
    	} else if(autonomousName.equals("Gear Right Side Blue")) {
    		switch(autoStepNumber) {
				case 1: 
					if(Math.abs(mDrive.getLeftDistance()) <= 70) {
						mDrive.driveSpeedTurn(1.0, 0.0);
					} else {
						mDrive.brake();
						mDrive.reset();
						autoStepNumber++;
					}
					break;
				case 2:
					if(Math.abs(mDrive.getGyroAngle()) <= 45) {
						mDrive.driveSpeedTurn(0.0, -0.8);
					} else {
						mDrive.brake();
						autoStepNumber++;
					}
					break;
				case 3: 
					if(Math.abs(mDrive.getLeftDistance()) <= 34) {
						mDrive.driveSpeedTurn(1.0, 0.0);
					} else {
						mDrive.brake();
						autoStepNumber++;
					}
					break;
				case 4:
					mDrive.brake();
					break;
				default: 
				
    		}	
    	} else if(autonomousName.equals("Gear Left Side Red")) {
        	switch(autoStepNumber) {
        		case 1: 
        			if(Math.abs(mDrive.getAverageDistance()) <= 31) {
        				mDrive.driveSpeedTurn(1.0, 0.0);
        			} else {
        				mDrive.brake();
        				mDrive.reset();
        				mDrive.zeroGyro();
        				autoStepNumber++;
        			}
    				break;
    			case 2:
    				if(Math.abs(mDrive.getGyroAngle()) <= 42) {
    					mDrive.driveSpeedTurn(0.0, 0.8);
    				} else {
    					mDrive.brake();
    					mDrive.reset();
    					autoStepNumber++;
    				}
    				break;
        		case 3: 
    				if(Math.abs(mDrive.getAverageDistance()) <= 60) {
    					mDrive.driveSpeedTurn(1.0, 0.0);
    				} else {
    					mDrive.brake();
    					mDrive.zeroGyro();
    					mDrive.reset();
    					autoStepNumber++;
    				}
    				break;
    			case 4:
    				mDrive.brake();
    				break;
    			default: 
    				mDrive.brake();
    		}
    	} else if(autonomousName.equals("Gear Right Side Red")) {
    		switch(autoStepNumber) {
				case 1: 
					if(Math.abs(mDrive.getAverageDistance()) <= 70) {
						mDrive.driveSpeedTurn(1.0, 0.0);
					} else {
						mDrive.brake();
						autoStepNumber++;
					}
					break;
				case 2:
					if(Math.abs(mDrive.getGyroAngle()) <= 45) {
						mDrive.driveSpeedTurn(0.0, -0.8);
					} else {
						mDrive.brake();
						autoStepNumber++;
					}
					break;
				case 3: 
					if(Math.abs(mDrive.getAverageDistance()) <= 34) {
						mDrive.driveSpeedTurn(1.0, 0.0);
					} else {
						mDrive.brake();
						autoStepNumber++;
					}
					break;
				case 4:
					mDrive.brake();
					break;
				default: 
					mDrive.brake();
    		}	
    	} 
    
    	logToDashboard();
    }
    
    private void readAutoMode(int lWhich)
    {
    	switch(lWhich)
    	{
    		//Add autonomous modes as they are made
	    	case 1:
	    		//mPropertyReader.parseAutonomousFile("/home/lvuser/DriveForward.txt");
	    		autonomousName = "Drive Forward";
	    		break;
	    	case 2:
	    		//mPropertyReader.parseAutonomousFile("home/lvuser/GearMiddleClose.txt");
	    		autonomousName = "Gear Middle";
	    		break;
	    	case 3:
	    		//mPropertyReader.parseAutonomousFile("home/lvuser/GearLeftSide.txt");
	    		autonomousName = "Gear Left Side Blue";
	    		break;
	    	case 4:
	    		//mPropertyReader.parseAutonomousFile("home/lvuser/GearRightSide.txt");
	    		autonomousName = "Gear Right Side Blue";
	    		break;
	    	case 5: 
	    		autonomousName = "Gear Left Side Red";
	    		break;
	    	case 6:
	    		autonomousName = "Gear Right Side Red";
	    		break;
	    	case 7:
	    		autonomousName = "Ten Balls Blue";
	    		break;
	    	case 8:
	    		autonomousName = "Trajectory Test";
	    		break;
	    	case 9:
	    		autonomousName = "Ten Balls Red";
	    		break;
    		default:
    			//mPropertyReader.parseAutonomousFile("/home/lvuser/DoNothing.txt");
    			autonomousName = "Gear Middle";
    			break;
    	}
    }
    
    public void teleopInit() {
    	mLogger.isEnabled = true;
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	wasEnabledFlag = true;
        mOI.processInputs();
        logToDashboard();
        
        //mVision.findTarget();
//        if(mLogger.isEnabled) {
//        	mLogger.log();
//        }
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void loadAllProperties() {
    	
    }
    
    public void logToDashboard() {
    	SmartDashboard.putNumber("X Offset from Target", mVision.getXOffset());
    	SmartDashboard.putNumber("Y Offset from Target", mVision.getYOffset());
    	SmartDashboard.putNumber("X Center:", mVision.getCenterX());
    	SmartDashboard.putNumber("Y Center:", mVision.getCenterY());
    	SmartDashboard.putBoolean("Target found?", mVision.isTargetFound());
    	SmartDashboard.putNumber("Left Encoder Distance: ", mDrive.getLeftDistance());
    	SmartDashboard.putNumber("Right Encoder Distance: ", mDrive.getRightDistance());
    	SmartDashboard.putNumber("Left Encoder Speed: ", mDrive.getLeftSpeed());
    	SmartDashboard.putNumber("Right Encoder Speed: ", mDrive.getRightSpeed());
    	SmartDashboard.putNumber("Gyro: ", mDrive.getGyroAngle());
    	SmartDashboard.putString("Auto mode: ", autonomousName);
    	SmartDashboard.putNumber("Speed", mShooter.getSpeed());
    	SmartDashboard.putBoolean("Is shooter ready?", mShooter.isTargetSpeed());
    	SmartDashboard.putBoolean("Is drive inverted?", mDrive.isInverted());
    	SmartDashboard.putNumber("Yaw Angle From Target: ", mVision.getYawAngleTarget());
    	
    }
    
}
