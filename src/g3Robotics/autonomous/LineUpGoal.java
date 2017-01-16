package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;
import g3Robotics.vision.Vision;
import g3Robotics.loops.DriveDistance;
import g3Robotics.utilities.SynchronousPID;

/**
 * Preliminary vision-testing autonomous for the mulebot.
 * 
 * Lines up the entire robot with the boiler high goal.
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
		offsetX = mVision.getCenterX() - 160;
		offsetY = mVision.getCenterY() - 120;
	}
	
	public void running() 
	{
		mDrive.setOpenLoop();
		mDrive.driveSpeedTurn(0, 0.7);
		offsetX = mVision.getCenterX() - 160;
		offsetY = mVision.getCenterY() - 120;
	}

	public boolean isDone()	 
	{
		return ((offsetX > 0.1) && (offsetY > 0.1));
	}
}