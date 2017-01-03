package g3Robotics;

import g3Robotics.subsystems.*;

public class OI 
{
	private static OI instance = null;
	private final Drive mDrive;
	
	public static OI getInstance()
    {
        if( instance == null )
        {
            instance = new OI();
        }
        return instance;
    }
	
	private OI()
	{
		//Initialize subsystems
		mDrive = Drive.getInstance();
		//Initialize gamepads
		
	}

	public void processInputs()
	{
		
	}
}
