package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;
import g3Robotics.vision.Vision;
import g3Robotics.loops.DriveDistance;
import g3Robotics.utilities.SynchronousPID;

/**
 * Preliminary vision-testing autonomous.
 * 
 * Lines up the entire robot with the boiler high goal (no shooter adjustment)
 * 
 * @author AlexL
 */

public class LineUpGoal extends State
{
	private double mAngle, mSpeed;
	private double offsetX;
	private double offsetY;
	private Timer timer;
	private final Vision mVision;
	
	public LineUpGoal()
	{
		super("LineUpGoal");
		mVision = Vision.getInstance();
	}
	
	public void enter()
	{
		//mDrive.resetEncoders();
		mDrive.reset();
		mVision.findTarget();
		offsetX = mVision.getXOffset();
		offsetY = mVision.getYOffset();
		mDrive.lightsOn();
	}
	
	public void running() 
	{
		mDrive.setOpenLoop();
		//mVision.findTarget(); // update offset values. 

		if(offsetX < 0)
			mDrive.driveSpeedTurn(0, 0.3);
		else 
			mDrive.driveSpeedTurn(0, -0.3);
			
	}

	public boolean isDone()	 
	{
		//return mVision.isTargetFound();
		return (Math.abs(mVision.getYawAngleTarget()) > (Math.abs(mDrive.getGyroAngle()) + 21));
	}
	
	public void exit(){
		mDrive.brake();
	}
}