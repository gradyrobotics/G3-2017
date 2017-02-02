package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.loops.DriveDistance;
import g3Robotics.utilities.SynchronousPID;
import g3Robotics.utilities.Trajectory;
import g3Robotics.subsystems.*;

/**
 *
 * @author Pratik341
 */
public class MixedDrive extends State
{
    private double targetDistance;
    public MixedDrive(double distance)
    {
    	super("MixedDrive");
       targetDistance = distance;
    }

    public void enter()
    {
      mDrive.reset(); 
    }
    
    public void exit()
    {
      mDrive.brake();  
    }
    
    public void running() 
    {
    	if(targetDistance >= 0)
    	{
    		 mDrive.driveSpeedTurn(1.0,0.0);
    	}
    	else
    	{
    		mDrive.driveSpeedTurn(-1.0,0.0);
    	}
    	
    }

    public boolean isDone() 
    {
    	if(targetDistance >= 0)
    	{
    		return mDrive.getAverageDistance() >= targetDistance;
    	}
    	else
    	{
    		return mDrive.getAverageDistance() <= targetDistance;
    	}
    }
}

