package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;

public class RevUpShooter extends State{
	
	private double mTimeout;
	private Timer timer;
	
	public RevUpShooter(double aSeconds) {
		super("RevUpShooter");
		mTimeout = aSeconds;
		
		timer = new Timer();
	}
	
	public void enter(){
		timer.reset();
		timer.start();
	}
	
	public void exit(){
		timer.stop();
		mShooter.setWheels(0.0, 0.0, 0.0);
	}

	public void running() {
		mShooter.setWheels(3000, 0.5, 0.7);
	}
	

	public boolean isDone() {
		return timer.get() > mTimeout;
	}
	
}