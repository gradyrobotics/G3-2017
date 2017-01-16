package g3Robotics;

import g3Robotics.subsystems.*;
import g3Robotics.utilities.*;

public class OI 
{
	private static OI instance = null;
	private final Drive mDrive;
	private XboxController driverGamepad;
	private double speedCommand, turnCommand;
	
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
		driverGamepad = new XboxController(0);
		
	}

	public void processInputs()
	{
		speedCommand  = driverGamepad.getLeftYAxis();
		turnCommand = driverGamepad.getRightXAxis();
		
		mDrive.driveSpeedTurn(speedCommand, turnCommand);
	}
}
