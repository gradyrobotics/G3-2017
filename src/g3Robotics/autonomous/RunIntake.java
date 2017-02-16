package g3Robotics.autonomous;

import edu.wpi.first.wpilibj.Timer;
import g3Robotics.subsystems.*;

public class RunIntake extends State{
	
	private double mTimeout;
	private Timer timer;
	public RunIntake(double aSeconds) {
		super("RunIntake");
		mTimeout = aSeconds *  1000;
		timer = new Timer();
	}
	
	public void enter(){
		timer.reset();
		timer.start();
	}
	
	public void exit(){
		timer.stop();
		//mIntake.setSpeed(0.0);
	}

	public void running() {
		mIntake.setSpeed(0.7);
	}
	

	public boolean isDone() {
		return timer.get() > mTimeout;
	}
	
}