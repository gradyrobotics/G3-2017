package g3Robotics.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import g3Robotics.Constants;

public class Climber extends G3Subsystem {
	public VictorSP climberMotor;
	private static Climber instance;
	
	public static Climber getInstance()
	{
		if (instance == null)
		{
			return new Climber();
		}
		else
		{
			return instance;
		}
	}
	
	public Climber()
	{
		climberMotor = new VictorSP(Constants.climberMotorPWM);
	}
	
	public void setSpeed(double inputSpeed)
	{
		climberMotor.set(inputSpeed);
	}
}
