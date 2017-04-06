package g3Robotics.loops;

import g3Robotics.loops.Controller;
import g3Robotics.trajectorylib.Trajectory;
import g3Robotics.trajectorylib.TrajectoryFollower;
import g3Robotics.utilities.G3Math;
import g3Robotics.subsystems.*;
/**
 * TrajectoryDriveController.java
 * This controller drives the robot along a specified trajectory.
 */
public class TrajectoryDriveController implements Controller {

  Drive mDrive;
  private boolean enabled;

  public TrajectoryDriveController() {
    init();
    mDrive = Drive.getInstance();
  }
  
  Trajectory trajectory;
  TrajectoryFollower followerLeft = new TrajectoryFollower("left");
  TrajectoryFollower followerRight = new TrajectoryFollower("right");
  double direction;
  double heading;
  double kTurn = -3.0/80.0;

  public boolean onTarget() {
    return followerLeft.isFinishedTrajectory();
  }	

  private void init() {
    followerLeft.configure(1.5, 0, 0, 1.0/15.0, 1.0/34.0);
    followerRight.configure(1.5, 0, 0, 1.0/15.0, 1.0/34.0);
  }
  public void enable() {
	  enabled = true;
  }


  public void disable() {
	  enabled = false;
  }

  public void loadProfile(Trajectory leftProfile, Trajectory rightProfile, double direction, double heading) {
    reset();
    followerLeft.setTrajectory(leftProfile);
    followerRight.setTrajectory(rightProfile);
    this.direction = direction;
    this.heading = heading;
  }
  
  public void loadProfileNoReset(Trajectory leftProfile, Trajectory rightProfile) {
    followerLeft.setTrajectory(leftProfile);
    followerRight.setTrajectory(rightProfile);
  }

  public void reset() {
    followerLeft.reset();
    followerRight.reset();
    mDrive.resetEncoders();
  }
  
  public int getFollowerCurrentSegment() {
    return followerLeft.getCurrentSegment();
  }
  
  public int getNumSegments() {
    return followerLeft.getNumSegments();
  }
  
  public void run(){ 
    if (!enabled){ 	
      return;
    }

    if (onTarget()) {
      //mDrive.driveLeftRight(0.0, 0.0);
      disable();
    } else  {
      double distanceL = direction * mDrive.getLeftDistance();
      double distanceR = direction * mDrive.getRightDistance();

      double speedLeft = direction * followerLeft.calculate(distanceL);
      double speedRight = direction * followerRight.calculate(distanceR);
      
      double goalHeading = followerLeft.getHeading();
      double observedHeading = mDrive.getGyroAngle(); //actually not in radians so 

      double angleDiffRads = G3Math.getDifferenceInAngleRadians(observedHeading, goalHeading);
      double angleDiff = Math.toDegrees(angleDiffRads);

      double turn = kTurn * angleDiff;
      mDrive.driveLeftRight(speedLeft + turn, speedRight - turn);
    }
  }

  public void setTrajectory(Trajectory t) {
    this.trajectory = t;
  }

  public double getGoal() {
    return 0;
  }
  
  public boolean isEnabled(){
	return enabled;
  }

  public void loadProperties() {
		
  }

}