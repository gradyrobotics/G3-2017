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
	
	public LineUpGoal()
	{
		super("LineUpGoal");
	}
	
	public void enter()
	{
		mDrive.resetEncoders();
		mDrive.reset();
		//Somehow grab center x and y, and do calculations
	}
	
	public void running() 
	{
		mDrive.setOpenLoop();
		mDrive.driveSpeedTurn(0, 0.7);
		//Refer to the comment above for this space
	}

	public boolean isDone()	 
	{
		return ((offsetX > 0.1) && (offsetY > 0.1));
	}
}