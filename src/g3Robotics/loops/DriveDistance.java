package g3Robotics.loops;

//import missdaisy.fileio.PropertySet;
import g3Robotics.subsystems.Drive;
import g3Robotics.utilities.G3Math;
import g3Robotics.utilities.Trajectory;
import g3Robotics.utilities.TrajectoryFollower;

/**
 *
 * @author PratikKunapuli
 */
public class DriveDistance implements Controller
{
    //private PropertySet mProperties = PropertySet.getInstance();
    private Drive mDrive = Drive.getInstance();
    private TrajectoryFollower mFollower;
    private double kTurn;
    private double distanceThreshold;
    private double heading;
    private double direction;
    
    private static DriveDistance instance = null;
    
    public static DriveDistance getInstance()
    {
        if( instance == null )
        {
            instance = new DriveDistance();
        }
        return instance;
    }
    
    private DriveDistance()
    {
        mFollower = new TrajectoryFollower();
        loadProperties();
    }
    
    public void loadProfile(Trajectory profile, double direction, double heading)
    {
        reset();
        //mFollower.setTrajectory(profile);        
        this.direction = direction;
        this.heading = heading;
    }

    public void reset() 
    {
        loadProperties();
       // mFollower.reset();
        mDrive.resetEncoders();
    }

    public boolean onTarget() 
    {
        return mFollower.isFinishedTrajectory();// && mFollower.onTarget(distanceThreshold);
    }


    public void run() 
    {
        //System.out.println(this.onTarget() + " " + mFollower.isFinishedTrajectory() + " " + mFollower.onTarget(1.0));
        if( onTarget() )
        {
            mDrive.driveLeftRight(0.0, 0.0);
        }
        else
        {
            double distance = direction*mDrive.getAverageDistance();
            double angleDiff = G3Math.boundAngleNeg180to180Degrees(heading-mDrive.getGyroAngle());
            
            //double speed = direction*mFollower.calculate(distance);
            double turn = kTurn*angleDiff;
            //mDrive.driveSpeedTurn(speed, turn);
        }
    }

	@Override
	public void loadProperties() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}


    
}
