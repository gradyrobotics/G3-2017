package g3Robotics.subsystems;

import g3Robotics.Constants;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import g3Robotics.vision.Vision;

/**
 * The robot drive base.
 * 
 * This includes motors and sensors related to the drive and chassis base.
 * 
 * @author PratikKunapuli
 */
public class Drive extends G3Subsystem
{
    // Actuators
    private final VictorSP leftMotor1;
    private final VictorSP rightMotor1;
    private final DoubleSolenoid shifter;
    private final Relay lights;
    
    // Sensors
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    //private Gyro gyro;
    private PowerDistributionPanel pdp;
    private ADXRS450_Gyro gyro;
	public DoubleSolenoid platePiston;
	private Timer lineUpTimer;

    //Variables
    private double leftDistance = 0;
    private double rightDistance = 0;
    private double lastLeftDistance = 0;
    private double lastRightDistance = 0;
    private double leftRaw = 0;
    private double rightRaw =0;
    private boolean areLightsOn = false;
    private boolean isPlateUp = false;
    private boolean isInverted = false;
    private boolean openLoop = true;
    private double gyroZero = 0.0;
    private boolean isLineUpTimeSet = false;
    private double lineUpTime = 0.0;
    private boolean lineUpState = false;
    
    private static Drive instance = null;
    Vision mVision;

    private Drive()
    {
    	mVision = Vision.getInstance();
    	
        leftMotor1 = new VictorSP(Constants.leftDrivePWM);
        rightMotor1 = new VictorSP(Constants.rightDrivePWM);
        shifter = new DoubleSolenoid(Constants.shifterSolenoid_1, Constants.shifterSolenoid_2);
        lights = new Relay(Constants.lightRelay);
        
        pdp = new PowerDistributionPanel();
        
        double encoderScalingFactor = (Constants.WheelSizeIn*Math.PI)/128; //CHECK THIS
        leftEncoder = new Encoder(Constants.leftDriveDI2, Constants.leftDriveDI1);
        leftEncoder.setDistancePerPulse(encoderScalingFactor);
        rightEncoder = new Encoder(Constants.rightDriveDI2, Constants.rightDriveDI1);
        rightEncoder.setDistancePerPulse(-encoderScalingFactor);

        gyro = new ADXRS450_Gyro();
        lineUpTimer = new Timer();
        lineUpTimer.start();
		platePiston = new DoubleSolenoid(Constants.humanPlayerPlateSolenoid_1, Constants.humanPlayerPlateSolenoid_2);
		
		lowGear();
        
    }
    
    public static Drive getInstance()
    {
        if( instance == null )
        {
        	instance = new Drive();
        }
        return instance;
    }
    
    
    private void set(double left, double right)
    {
        leftMotor1.set(left);
        rightMotor1.set(-right);
        
    }
    
    public double getTotalCurrent()
    {
    	return pdp.getTotalCurrent();
    }
    
    public void driveSpeedTurn(double speed, double turn)
    {
    	double left;
    	double right;
    	if(isInverted()) {
    		speed = -speed;
    	}
    	
    	left = -speed+turn;
    	right = -speed - turn;
    	
        set(left, right);
    }
    
    public void lineUpGoal(){
    	if(mVision.getXOffset() > 4){
    		lineUpState = true;
    		driveSpeedTurn(0.0, 0.8 * mVision.getXOffset());
    	}
    	else if(mVision.getXOffset() < -4){
    		lineUpState = true;
    		driveSpeedTurn(0.0, -0.8 * mVision.getXOffset());
    	}
    	else{
    		lineUpState = false;
    	}
    }

    public void driveLeftRight(double left, double right)
    {
        set(left, right);
    }
    
    public void brake()
    {
        driveLeftRight(0.0, 0.0);
    }
    
    public void lowGear()
    {
    	shifter.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void highGear()
    {
    	shifter.set(DoubleSolenoid.Value.kForward);
    }
    
    
    public double getLeftDistance()
    {
        return leftEncoder.getDistance();
    }
    
    public double getRightDistance()
    {
        return rightEncoder.getDistance();
    }
    
    public synchronized double getAverageDistance()
    {
        return (getLeftDistance() + getRightDistance())/2.0;
    }
    
    public synchronized double getLargestDistance()
    {
        if( Math.abs(leftDistance) > Math.abs(rightDistance))
        {
            return leftDistance;
        }
        else
        {
            return rightDistance;
        }
    }
    
    public double getLeftSpeed()
    {
        //return (leftDistance-lastLeftDistance)/Constants.LoopPeriodS;
        return leftEncoder.getRate();
    }
    
    public double getRightSpeed()
    {
        //return (rightDistance-lastRightDistance)/Constants.LoopPeriodS;
        return leftEncoder.getRate();
    }
    
    public synchronized double getAverageSpeed()
    {
        return (getLeftSpeed()+getRightSpeed())/2.0;
    }
    
    
    public double getGyroAngle()
    {

        return gyro.getAngle() - gyroZero;

    }
    
     public void driveArc(double speed, double arc)
    {
        double theta = (arc * (Math.PI/2));
        
        double ySpeed =  (speed*Math.cos(theta));
        double xSpeed = (speed*Math.sin(theta));
        
        double rPlusL = (1-Math.abs(xSpeed)) * (ySpeed) + ySpeed;
        double rMinusL = (1-Math.abs(ySpeed)) * (xSpeed) + xSpeed;
        
        set((rPlusL + rMinusL)/2, (rPlusL - rMinusL)/2);
    }
    
    public synchronized void runInputFilters()
    {
        lastLeftDistance = leftDistance;
        lastRightDistance = rightDistance;
        leftDistance = leftEncoder.getDistance();
        rightDistance = rightEncoder.getDistance();
        leftRaw = leftEncoder.get();
        rightRaw = rightEncoder.get();
    }
    
    public double getLeftRaw()
    {
        return leftRaw;
    }
    
    public double getRightRaw()
    {
        return rightRaw;
    }
    
    public void resetEncoders()
    {
        leftEncoder.reset();
        rightEncoder.reset();
        leftDistance =  0;
        rightDistance = 0;
        lastLeftDistance = 0; 
        lastRightDistance = 0;	
    }
    
    public synchronized void runOutputFilters()
    {
    }

   
    public synchronized void reset()
    {
        this.driveSpeedTurn(0.0,0.0);
        this.zeroGyro();
        resetEncoders();
    }
    
    public void lightsOn()
    { 
    	lights.set(Relay.Value.kForward);
    	areLightsOn = true;
    }
    
    public void lightsOff()
    {
    	lights.set(Relay.Value.kReverse);
    	areLightsOn = false; 
    }
//    
//    public void calibrate(){
//    	gyro.reset();
//    }
//    

	public void raisePlate(){
		platePiston.set(DoubleSolenoid.Value.kForward);
		isPlateUp = true;
	}
	public void lowerPlate(){
		platePiston.set(DoubleSolenoid.Value.kReverse);
		isPlateUp = false;
	}
	
	public boolean getPlateState(){
		return isPlateUp;
	}
    
    public boolean getLightState()
    {
    	return areLightsOn;
    }
    
    public void calibrate() {
    	gyro.calibrate();
    }
    
    public void zeroGyro() {
    	gyroZero = gyro.getAngle();
    }
    
    public boolean isInverted() {
    	return isInverted;
    }
    
    public boolean isLinedUp(){
    	return lineUpState;
    }
    
    public void setLiningUp(boolean input){
    	lineUpState = input;
    }
    
    
    public void invert() {
    	isInverted = !isInverted;
    }
    
    public void setOpenLoop(boolean input){
    	openLoop = input;
    }
    
    public boolean isOpenLoop(){
    	return openLoop;
    }

}
