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
    public void robotInit() {
    	mDrive = Drive.getInstance();
    	mOI = OI.getInstance();
    	//mVision = Vision.getInstance();
        //mVision.VisionInit();
        //mVision.findTarget();
    }
    
    /**
     * Function that is called periodically while robot is disabled. 
     * Publishing to smart dashboard here will be a live status of the 
     * robot in disabled mode. 
     */
    public void disabledPeriodic() {
    	logToDashboard();
    	//mVision.findTarget();
    }
    
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
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
    
    public void logToDashboard() {
//    	//SmartDashboard.putNumber("X Offset from Target", mVision.getXOffset());
//    	//SmartDashboard.putNumber("Y Offset from Target", mVision.getYOffset());
//    	SmartDashboard.putNumber("X Center:", mVision.getCenterX());
//    	SmartDashboard.putNumber("Y Center:", mVision.getCenterY());
//    	SmartDashboard.putBoolean("Target found?", mVision.isTargetFound());
    	SmartDashboard.putNumber("Left Encoder Distance: ", mDrive.getLeftDistance());
    	SmartDashboard.putNumber("Right Encoder Distance: ", mDrive.getRightDistance());
    	SmartDashboard.putNumber("Left Encoder Speed: ", mDrive.getLeftSpeed());
    	SmartDashboard.putNumber("Right Encoder Speed: ", mDrive.getRightSpeed());
    }
    
}
