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
		mDrive.resetEncoders();
		mDrive.reset();
		mVision.VisionInit();
		mVision.findTarget();
		offsetX = mVision.getXOffset();
		offsetY = mVision.getYOffset();
	}
	
	public void running() 
	{
		mDrive.setOpenLoop();
		mVision.findTarget(); // update offset values. 
		offsetX = mVision.getXOffset();
		offsetY = mVision.getYOffset();

		if(offsetX < 0)
			mDrive.driveSpeedTurn(0, 0.3 * offsetX);
		else if (offsetX > 0)
			mDrive.driveSpeedTurn(0, -0.3 * offsetX);
	}

	public boolean isDone()	 
	{
		return (Math.abs(mVision.getYawAngleTarget()) > (Math.abs(mDrive.getGyroAngle()) - 4));
	}
}