package g3Robotics;

import g3Robotics.subsystems.*;
import g3Robotics.utilities.*;

public class OI 
{
	private static OI instance = null;
	private final Drive mDrive;
	public XboxController driverGamepad;
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
		speedCommand = G3Math.applyDeadband(driverGamepad.getLeftYAxis(), 0.2);
		turnCommand = G3Math.applyDeadband(driverGamepad.getRightXAxis(), 0.2);
		
		
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
