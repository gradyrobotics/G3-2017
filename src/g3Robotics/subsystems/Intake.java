package g3Robotics.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import g3Robotics.Constants;

//author is peeps
public class Intake extends G3Subsystem {
	
	private VictorSP intakeMotor;
	private DoubleSolenoid intakePiston;
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
		intakePiston = new DoubleSolenoid(Constants.intakeSolenoid_1, Constants.intakeSolenoid_2);	
	}
	
	public void setSpeed(double inputSpeed)
	{
		intakeMotor.set(inputSpeed);
	}
	
	public void deploy()
	{
		intakePiston.set(DoubleSolenoid.Value.kForward);	
		isUp = false;
	}
	
	public void raise()
	{
		intakePiston.set(DoubleSolenoid.Value.kReverse);
		isUp = true;
	}
	
	public boolean getState()
	{
		return isUp;
	}
}
