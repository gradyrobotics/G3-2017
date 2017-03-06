//package g3Robotics.autonomous;
//
//import edu.wpi.first.wpilibj.Timer;
//import g3Robotics.subsystems.*;
//
//public class DropIntake extends State{
//	
//	private double mTimeout;
//	private Timer timer;
//	
//	public DropIntake(double aSeconds) {
//		super("DropIntake");
//		mTimeout = aSeconds *  1000;
//		timer = new Timer();
//	}
//	
//	public void enter(){
//		timer.reset();
//		timer.start();
//	}
//	
//	public void exit(){
//		timer.stop();
//	}
//
//	public void running() {
//		mIntake.deploy();
//	}
//
//	public boolean isDone() {
//		return timer.get() > mTimeout;
//	}
//	
//}