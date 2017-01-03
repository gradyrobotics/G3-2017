package g3Robotics.autonomous;

/**
 * State to wait for a specified number of milliseconds.
 * 
 * @author PratikKunapuli
 */
public class WaitForTime extends State
{
    long mTimeout;

    public WaitForTime(int aMilliseconds)
    {
        super("WaitForTime");

        mTimeout = aMilliseconds;
    }

    public void enter()
    {
        mTimeout += System.currentTimeMillis();
    }

    public void running()
    {
    	//mDrive.driveSpeedTurn(0.0, 0.0);
    }

    public void exit()
    {
    }

    public boolean isDone()
    {
        return mTimeout < System.currentTimeMillis();
    }

}
