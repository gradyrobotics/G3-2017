package g3Robotics;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import g3Robotics.autonomous.*;
import g3Robotics.fileio.*;
import g3Robotics.vision.*;
import g3Robotics.subsystems.*;
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
	
   // Vision mVision;
    Shooter mShooter;
    Drive mDrive;
    OI mOI;
    
    PropertyReader mPropertyReader;
    PropertySet mProperties;
    
    String autonomousName = "";
    
    StateMachine mAutonomousStateMachine;
    boolean mLastIterationButtonState = false;
    boolean resetState = false; 
    int buttonCounter;
    
    Timer timer;
    int autoStepNumber = 1;
    
    public void robotInit() {
    	
    	
    	mDrive = Drive.getInstance();
    	mOI = OI.getInstance();
    	mShooter = Shooter.getInstance();
    	
    	mDrive.calibrate();
    	mDrive.reset();
    	
//    	mVision = Vision.getInstance();
//        mVision.VisionInit();
//        mVision.findTarget();
        
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
//    	
		if(mOI.driverGamepad.getYButton() && !mLastIterationButtonState)
    	{
    		buttonCounter++;
    		readAutoMode(buttonCounter);
    		
	    	if(buttonCounter > 4)
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
    
    public void autonomousInit() {
    	timer.start();
    	mDrive.reset();
    	timer.reset();
    	mDrive.zeroGyro();
    	autoStepNumber = 1;
    	
    }

    /** 
     *This function is called periodically during autonomous  
     */
    
    public void autonomousPeriodic() {
    	//mAutonomousStateMachine.run();
    	//timer.start();
    	if(autonomousName.equals("Drive Forward")) {
	    	if(timer.get() < 4.0){
	    		mDrive.driveSpeedTurn(1.0, 0.0);
	    	}
	    	else {
	    		mDrive.driveSpeedTurn(0.0, 0.0);
	    		timer.stop();
	    	}
    	} else if (autonomousName.equals("Gear Middle")) {
    		if (timer.get() < 6.0) {
    			mDrive.driveSpeedTurn(0.8, -0.05 * mDrive.getGyroAngle());
    		} else {
    			mDrive.brake();
    			timer.stop();
    		}
    	} else if(autonomousName.equals("Gear Left Side")) {
    		switch(autoStepNumber) {
    			case 1: 
    				if(Math.abs(mDrive.getRightDistance()) <= 36) {
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
    			default: 
    				mDrive.brake();
    		} // end switch
//    	} else if (autonomousName.equals("Hopper and Shoot Bloo")){
//    		switch(autoStepNumber) {
//    			case 1:
//    				mDrive.lowerPlate();
//    				autoStepNumber++;
//    				break;
//    			case 2:
//    				if(Math.abs(mDrive.getAverageDistance()) <= 65){
//    					mDrive.driveSpeedTurn(-1.0, 0.0);
//    				} else {
//    					mDrive.brake();
//    					mDrive.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 3:
//    				if(Math.abs(mDrive.getGyroAngle()) <= 90){
//    					mDrive.driveSpeedTurn(0.0, -0.8);
//    				} else{
//    					mDrive.brake();
//    					mDrive.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 4:
//    				if(Math.abs(mDrive.getAverageDistance()) <= 5.0){
//    					mDrive.driveSpeedTurn(-1.0, 0.0);
//    				} else{
//    					mDrive.brake();
//    					mDrive.reset();
//        				timer.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 5:
//    				if(timer.get() < 2.5){
//    					mDrive.brake();
//    				} else {
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 6:
//    				if(Math.abs(mDrive.getAverageDistance()) <= 24){
//    					mDrive.driveSpeedTurn(1.0, 0.0);
//    				} else{
//    					mDrive.brake();
//    					mDrive.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 7:
//    				if(Math.abs(mDrive.getGyroAngle()) <= 90){
//    					mDrive.driveSpeedTurn(0.0, 1.0);
//    				} else{
//    					mDrive.brake();
//    					mDrive.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 8:
//    				if(Math.abs(mDrive.getAverageDistance()) <= 50){
//    					mDrive.driveSpeedTurn(1.0, 0.0);
//    				} else{
//    					mDrive.brake();
//    					mDrive.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 9:
//    				if(Math.abs(mDrive.getGyroAngle()) <= 25){
//    					mDrive.driveSpeedTurn(0.0, -0.8);
//    				} else {
//    					mDrive.brake();
//    					mDrive.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 10:
//    				if(mVision.getXOffset() > 0 && !mVision.isTargetFound()){
//    					mDrive.driveSpeedTurn(0.0, -0.7);
//    				}
//    				else if(mVision.getXOffset() < 0 && !mVision.isTargetFound()){
//    					mDrive.driveSpeedTurn(0.0, 0.7);
//    				} else{
//    					mDrive.brake();
//    					mDrive.reset();
//    					timer.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 11:
//    				if(timer.get() <= 2.0){
//    					mShooter.setWheels(3600, 0.0, 1.0);
//    				}
//    				else {
//    					timer.reset();
//    					autoStepNumber++;
//    				}
//    				break;
//    			case 12:
//    				if(timer.get() < 4.0){
//    					mShooter.setCyclone(0.6);
//    					mShooter.setBallPath(1.0);
//    					mShooter.setTransport(-1.0);
//    				}
//    				else{
//    					mShooter.brake();
//    					mShooter.setWheels(0.0, 0.0, 0.0);
//    				}
//    				break;
//    			default:
//    				mDrive.brake();
//    				mShooter.brake();
//    			}
    	} else if(autonomousName.equals("Gear Right Side")) {
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
				default: 
					mDrive.brake();
    		}//end switch
    		
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
	    		autonomousName = "Gear Left Side";
	    		break;
	    	case 4:
	    		//mPropertyReader.parseAutonomousFile("home/lvuser/GearRightSide.txt");
	    		autonomousName = "Gear Right Side";
	    		break;
	    	case 5:
	    		autonomousName = "Hopper and Shoot Bloo";
	    		break;
    		default:
    			//mPropertyReader.parseAutonomousFile("/home/lvuser/DoNothing.txt");
    			autonomousName = "Do Nothing";
    			break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        mOI.processInputs();
        logToDashboard();
        //mVision.findTarget();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    public void loadAllProperties() {
    	
    }
    
    public void logToDashboard() {
//    	SmartDashboard.putNumber("X Offset from Target", mVision.getXOffset());
//    	SmartDashboard.putNumber("Y Offset from Target", mVision.getYOffset());
//    	SmartDashboard.putNumber("X Center:", mVision.getCenterX());
//    	SmartDashboard.putNumber("Y Center:", mVision.getCenterY());
//    	SmartDashboard.putBoolean("Target found?", mVision.isTargetFound());
    	SmartDashboard.putNumber("Left Encoder Distance: ", mDrive.getLeftDistance());
    	SmartDashboard.putNumber("Right Encoder Distance: ", mDrive.getRightDistance());
    	SmartDashboard.putNumber("Left Encoder Speed: ", mDrive.getLeftSpeed());
    	SmartDashboard.putNumber("Right Encoder Speed: ", mDrive.getRightSpeed());
    	SmartDashboard.putNumber("Gyro: ", mDrive.getGyroAngle());
    	SmartDashboard.putString("Auto mode: ", autonomousName);
    	SmartDashboard.putNumber("Speed", mShooter.getSpeed());
    	SmartDashboard.putBoolean("Is shooter ready?", mShooter.isTargetSpeed());
    	SmartDashboard.putBoolean("Is drive inverted?", mDrive.isInverted());
    	//SmartDashboard.putNumber("Yaw Angle From Target: ", mVision.getYawAngleTarget());
    	
    }
    
}
