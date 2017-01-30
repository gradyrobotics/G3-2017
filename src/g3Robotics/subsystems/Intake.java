package g3Robotics.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import g3Robotics.Constants;

//author is peeps
public class Intake extends G3Subsystem{
	private VictorSP intakeMotor;
	private Solenoid intakePiston;
	private static Intake instance;
	private boolean isUp = true;
	
	public static Intake getInstance()
	{
		if (instance == null)
		{
			return new Intake();
		}
		else
		{
			return instance;
		}
	}
	
	public Intake()
	{
		intakeMotor = new VictorSP(Constants.intakeMotorPWM);
		intakePiston = new Solenoid(Constants.intakeSolenoid);
		
	}
	
	public void setSpeed(double inputSpeed)
	{
		intakeMotor.set(inputSpeed);
	}
	
	public void deploy()
	{
		intakePiston.set(true);
		isUp = false;
	}
	
	public void raise()
	{
		intakePiston.set(false);
		isUp = true;
	}
	
	public boolean getState()
	{
		return isUp;
	}
}
