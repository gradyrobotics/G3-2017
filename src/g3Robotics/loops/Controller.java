package g3Robotics.loops;

/**
 * Interface for a generic Controller.
 * 
 * A controller is something that attempts to control a subsystem to get to a 
 * specified goal.
 * 
 * @author PratikKunapuli
 */
public interface Controller extends Runnable
{
    public void reset();
    public boolean onTarget();
    public void loadProperties();
}
