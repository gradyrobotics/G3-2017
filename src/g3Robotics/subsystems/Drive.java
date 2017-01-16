package g3Robotics.subsystems;

import g3Robotics.Constants;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

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
   
    // Sensors
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private Gyro gyro;
    private PowerDistributionPanel pdp;
    
    //Variables
    private double leftDistance = 0;
    private double rightDistance = 0;
    private double lastLeftDistance = 0;
    private double lastRightDistance = 0;
    private double leftRaw = 0;
    private double rightRaw =0;

    private static Drive instance = null;

    private Drive()
    {
        leftMotor1 = new VictorSP(Constants.leftDrivePWM);
        rightMotor1 = new VictorSP(Constants.rightDrivePWM);
        
        pdp = new PowerDistributionPanel();
        

        double encoderScalingFactor = (Constants.WheelSizeIn*Math.PI)/128; //CHECK THIS
        leftEncoder = new Encoder(Constants.leftDriveDI1, Constants.leftDriveDI2);
        leftEncoder.setDistancePerPulse(encoderScalingFactor);
        rightEncoder = new Encoder(Constants.rightDriveDI1, Constants.rightDriveDI2);
        rightEncoder.setDistancePerPulse(-encoderScalingFactor);
        
        
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
    	double adjustedLeft;
    	double adjustedRight;
    	
    	left = -speed+turn;
    	right = -speed - turn;
    	
        set(left, right);
    }

    public void driveLeftRight(double left, double right)
    {
        set(left, right);
    }
    
    public void brake()
    {
        driveLeftRight(0.0, 0.0);
    }
    
    public double getLeftDistance()
    {
        return -leftEncoder.getDistance();
    }
    
    public double getRightDistance()
    {
        return -rightEncoder.getDistance();
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
        return gyro.getAngle();
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
        gyro.reset();
        resetEncoders();
    }
}