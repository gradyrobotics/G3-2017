package g3Robotics.autonomous;

import g3Robotics.fileio.*;

/**
 * Parses autonomous modes from an auto mode text file.
 * 
 * Because there is no reflection in J2ME, you MUST add a new else clause to
 * read in any new states that are developed.
 * 
 * @author PratikKunapuli
 */
public class AutonomousParser
{
    private final PropertySet mProperties;

    public AutonomousParser()
    {
        mProperties = PropertySet.getInstance();
    }

    public State[] parseStates()
    {

        State[] lStates;
        int lNumStates = mProperties.getIntValue("AutonomousNumStates", -1);
        System.out.println("Found " + Integer.toString(lNumStates) + " auto states");
        if( lNumStates < 1 )
        {
            lStates = new State[1];
            lStates[0] = new WaitForTime(0);
        }
        else
        {
            lStates = new State[lNumStates];
            for( int i = 0; i < lNumStates; i++ )
            {
                String lStateName = mProperties.getStringValue("AutonomousState" + Integer.toString(i+1), "");
                if( lStateName.equals("") )
                {
                    lStates[i] = new WaitForTime(0);
                }
                else
                {
                    if( lStateName.equals("MixedDrive") )
                    {
                        double lParam1 = mProperties.getDoubleValue("AutonomousFirstParam" + Integer.toString(i+1), 0);
                        //double lParam2 = mProperties.getDoubleValue("AutonomousSecondParam" + Integer.toString(i+1), 0);
                        //double lParam3 = mProperties.getDoubleValue("AutonomousThirdParam" + Integer.toString(i+1), 0);
                        lStates[i] = new MixedDrive(lParam1);
                    }
                    else if (lStateName.equals("LineUpGoal"))
                    {
                    	//double lParam1 = mProperties.getDoubleValue("AutonomousFirstParam" + Integer.toString(i+1), 0);
                    	
                    	lStates[i] = new LineUpGoal();
                    }
                    //TODO: Add additional States as they are developed
                    /*
                    // Example:
                    else if( lStateName.equals("MyNewState") )
                    {
                        int lParam1 = mProperties.getIntValue("AutonomousFirstParam" + Integer.toString(i+1), 0);
                        double lParam2 = mProperties.getDoubleValue("AutonomousSecondParam" + Integer.toString(i+1), 0.0);
                        // Etc...
                        lStates[i] = new MyNewState(lParam1, lParam2);
                    }
                    */
                    
                    System.out.println(lStates[i].toString());
                }
            }
        }

        return lStates;
    }

}
