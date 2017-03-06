package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;


/**
 * State to wait for a specified number of milliseconds.
 * 
 * @author PratikKunapuli
 */
public class WaitForTime extends State
{
    long mTimeout;
    Timer timer;

    public WaitForTime(int timeout)
    {
        super("WaitForTime");

        mTimeout = timeout;
    }

    public void enter()
    {
    	timer.start();
    }

    public void running()
    {
    	//mDrive.driveSpeedTurn(0.0, 0.0);
    }

    public void exit()
    {
    	timer.stop();
    }

    public boolean isDone()
    {
        return timer.get() > mTimeout;
    }

}
