package g3Robotics.autonomous;

public class MixedTurn extends State {

	private int targetHeading;
	
	public MixedTurn(int target) {
		super("MixedTurn");
		targetHeading = target;
	}
	public void enter() {
		mDrive.reset();
		
	}

	@Override
	public void running() {

	}

	@Override
	public boolean isDone() {
		if (targetHeading > 0)
		{
			return mDrive.getGyroAngle() >= targetHeading;
		}
		else
		{
			return mDrive.getGyroAngle() <= targetHeading;
		}
	}
	
	public void exit() {
		
	}
}
