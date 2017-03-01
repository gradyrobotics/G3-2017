package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;

public class ShootFuel extends State{
	
	private double mTimeout;
	private Timer timer;
	public ShootFuel(double aSeconds) {
		
		super("ShootFuel");
		
		mTimeout = aSeconds;
		timer = new Timer();
	}
	
	public void enter(){
		timer.reset();
		timer.start();
	}

	public void exit(){
		timer.stop();
		mShooter.brake();
	}

	public void running() {
		mShooter.setTransport(1.0);
		mShooter.setBallPath(0.6);
		mShooter.setCyclone(1.0);
	}
	

	public boolean isDone() {
		return timer.get() > mTimeout;
	}
	
}