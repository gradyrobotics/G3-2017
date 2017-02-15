package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;

public class RevUpShooter extends State{
	
	private double mTimeout;
	public RevUpShooter(double aSeconds) {
		super("RevUpShooter");
		mTimeout = aSeconds *  1000;
	}
	
	public void enter(){
		mTimeout += System.currentTimeMillis();
	}
	
	public void exit(){
		mShooter.setWheels(0.0, 0.0, 0.0);
	}

	public void running() {
		mShooter.setWheels(1300, 0.5, 0.7);
	}
	

	public boolean isDone() {
		return mTimeout < System.currentTimeMillis();
	}
	
}