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
    private double mDistance;
    
    public MixedDrive(double distance)
    {
        super("MixedDrive");
       
        mDistance = distance;
    }

    public void enter()
    {
        mDrive.resetEncoders();
        mDrive.reset();
    }
    
    public void exit()
    {
        mDrive.setOpenLoop();
        mDrive.driveLeftRight(0.0,0.0);
    }
    
    public void running() 
    {
    	mDrive.driveSpeedTurn(1.0, 0.0);
    	
    }

    public boolean isDone() 
    {
        return mDrive.getAverageDistance() == mDistance;
    }
}

