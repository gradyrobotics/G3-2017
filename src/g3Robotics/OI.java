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
		if(Math.abs(driverGamepad.getLeftYAxis()) > 0.1)
		{
			speedCommand  = 0;//driverGamepad.getLeftYAxis();
		}
		else
		{
			speedCommand = 0.;
		}
		
		if(Math.abs(driverGamepad.getRightXAxis()) > 0.1) 
		{
			turnCommand = 0;//driverGamepad.getRightXAxis();
		}
		else
		{
			turnCommand = 0.;
		}
		
		
		
		mDrive.driveSpeedTurn(speedCommand, turnCommand);
		
		if (driverGamepad.getLB())
		{
			mDrive.lowGear();
		}
		else if (driverGamepad.getRB())
		{
			mDrive.highGear();
		}
	}
}
