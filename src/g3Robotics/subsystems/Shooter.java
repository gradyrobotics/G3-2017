package g3Robotics.subsystems;

import g3Robotics.Constants;
import edu.wpi.first.wpilibj.*;

public class Shooter extends G3Subsystem {
	private final VictorSP shooterMotor1;
	private final VictorSP shooterMotor2;
	
	private Counter counter;
	
	private static Shooter instance = null;
	
	private Shooter()
	{
		shooterMotor1 = new VictorSP(Constants.shooterMotorPWM_1);
		shooterMotor2 = new VictorSP(Constants.shooterMotorPWM_2);
		counter = new Counter(Constants.bannerSensorPWM);
	}
	
	private static Shooter getInstance(){
		if( instance == null )
        {
        	instance = new Shooter();
        }
        return instance;
	}

    public double getSpeed(){
    	return 60.0/counter.getPeriod(); //60 sounded like a correct number?
    }
}