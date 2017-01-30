package g3Robotics;

import g3Robotics.subsystems.*;
import g3Robotics.utilities.*;

public class OI 
{
	private static OI instance = null;
	private final Drive mDrive;
	public XboxController driverGamepad;
	private double speedCommand, turnCommand;
	public boolean lastButtonState = false;
	public boolean currentButtonState = false;
	
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
		
		if (speedCommand > 0) {
			speedCommand = Math.pow(speedCommand,2);
		} else {
			speedCommand = (-1.) * Math.pow(speedCommand,2); 
		}
		
		if (turnCommand > 0) {
			turnCommand = Math.pow(turnCommand,2);
		} else {
			turnCommand = (-1.) * Math.pow(turnCommand,2); 
		}
		//speedCommand = Math.pow(speedCommand, 2);
		//turnCommand = Math.pow(turnCommand, 2);
		mDrive.driveSpeedTurn(speedCommand, turnCommand);
		
		if (driverGamepad.getLB())
		{
			mDrive.highGear();
		}
		else
		{
			mDrive.lowGear();
		}
		
		if(driverGamepad.getAButton())
		{
			currentButtonState = true;
		}
		else
		{
			currentButtonState = false;
		}
		
		if (currentButtonState && mDrive.getLightState() && !lastButtonState)
		{
			mDrive.lightsOff();
		}
		else if (currentButtonState && !mDrive.getLightState() && !lastButtonState)
		{
			mDrive.lightsOn();
		} 
		
		lastButtonState = currentButtonState;
		
		
		
	}
}
