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
    SendableChooser chooser;
	
    Vision mVision;
    Drive mDrive;
    OI mOI;
    
    PropertyReader mPropertyReader;
    PropertySet mProperties;
    
    String autonomousName = "";
    
    StateMachine mAutonomousStateMachine;
    boolean mLastIterationButtonState = false;
    int buttonCounter;
    
    public void robotInit() {
    	
    	mDrive = Drive.getInstance();
    	mOI = OI.getInstance();
    	mVision = Vision.getInstance();
        mVision.VisionInit();
        mVision.findTarget();
        
        mPropertyReader = new PropertyReader();
        mProperties = PropertySet.getInstance();
        
    }
    
    /**
     * Function that is called periodically while robot is disabled. 
     * Publishing to smart dashboard here will be a live status of the 
     * robot in disabled mode. 
     */
    public void disabledPeriodic() {
    	logToDashboard();
    	mVision.findTarget();
    	
    	/**
    	 * if driver presses this button{
    	 * 		buttonCounter++;
    	 * 		readStartingPosition(buttonCounter);
    	 * autonomousStartMachine = new StartMachine;
    	 * 
    	 * if driver presses this other button {
    	 * 		buttonCounter++;
    	 * 		
    	 * }
    	 */
    	
		if(mOI.driverGamepad.getAButton() && !mLastIterationButtonState)
    	{
    		buttonCounter++;
    		readAutoMode(buttonCounter);
    		
	    	if(buttonCounter > 3)
	    	{
	    		buttonCounter = 0;
	    	}

    	mAutonomousStateMachine = new StateMachine(new AutonomousParser().parseStates());
    	}
		
		if(mOI.driverGamepad.getBButton())
		{
	   		 mDrive.reset();
	   		 mPropertyReader.parseFile("/home/lvuser/properties.txt");
	   		 //loadAllProperties();
	   	}
   	 	mLastIterationButtonState = mOI.driverGamepad.getAButton();
    }
    
    public void autonomousInit() {
    	
    }

    /** 
     *This function is called periodically during autonomous  
     */
    
    public void autonomousPeriodic() {
    	mAutonomousStateMachine.run();
    	logToDashboard();
    }
    
    private void readAutoMode(int lWhich)
    {
    	switch(lWhich)
    	{
    		//Add autonomous modes as they are made
	    	case 1:
	    		mPropertyReader.parseAutonomousFile("/home/lvuser/DriveForward.txt");
	    		autonomousName = "Drive Forward";
	    		break;
	    	case 2:
	    		mPropertyReader.parseAutonomousFile("/home/lvuser/AimAtGoal.txt");
	    		autonomousName = "Aim At Goal Test";
	    		break;
    		default:
    			mPropertyReader.parseAutonomousFile("/home/lvuser/DoNothing.txt");
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
        mVision.findTarget();
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
    }
    
}
