package g3Robotics.subsystems;

import g3Robotics.fileio.PropertySet;
import g3Robotics.loops.Controller;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Base class for a subsystem.
 * 
 * A subsystem is a mechanism or cohesive function of a robot.  Examples could
 * be: Drive, Arm, Shooter, Intake, etc.
 * 
 * @author PratikKunapuli
 */
public abstract class G3Subsystem extends Subsystem
{
    protected Controller m_currentController;
    protected PropertySet m_propertySet = PropertySet.getInstance();
    
    public void initDefaultCommand()
    {
        
    }

    public Controller getCurrentController()
    {
        return m_currentController;
    }

    public void setCurrentController(Controller controller)
    {
        m_currentController = controller;
    }

    public void setOpenLoop()
    {
        m_currentController = null;
    }

    public void runCurrentController()
    {
        if( m_currentController != null )
        {
            m_currentController.run();
        }
    }

    public void runOutputFilters()
    {
        
    }

    public void runInputFilters()
    {

    }

    public void reset()
    {
        
    }
    
    public void loadProperties()
    {
        
    }
}
