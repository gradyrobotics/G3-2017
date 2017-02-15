package g3Robotics.vision;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import g3Robotics.vision.GripPipeline;

import g3Robotics.*;

public class Vision {
	
    private static final int Image_Width = 320;
    private static final int Image_Height = 240;
    private double centerX = 0.0;
    private double centerY = 0.0;
    private double xOffset = 0.0;
    private double yOffset = 0.0;
    
    private final double horizontalFieldOfView = 43.5;
    private final double verticleFieldOfView = 40.0;
    private double focalLength = 401.03;
    private double viewingAngle; //arccos(apparent height in pixels / actual height in pixels)
    						     //actual height in pixels = apparentWidth * (physical height/physical width), only works
    							 //if there is little distortion
    
    private double distanceFromGoal; 
    
    private VisionThread visionThread;
    
	private static Vision instance = null;
	public final Object imgLock = new Object();

    public static Vision getInstance()
    {
        if( instance == null )
        {
            instance = new Vision();
        }
        return instance;
    }
    	
    public void VisionInit()
    {
		AxisCamera camera = CameraServer.getInstance().addAxisCamera("Axis Camera","10.16.48.30");
    	camera.setResolution(Image_Width, Image_Height);

    	visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
    		
            if (!pipeline.filterContoursOutput().isEmpty()) {
            	Rect boundingBox = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
            	synchronized (imgLock) {
            		centerX = boundingBox.x + (boundingBox.width / 2);
            		centerY = boundingBox.y + (boundingBox.height / 2);
            		findTarget();
            	}
            }	
    	});
    		
    	visionThread.start();	
    }
    
    public synchronized void findTarget() {
    		xOffset = centerX - Image_Width/2;
    		yOffset = centerY - Image_Height/2;
    }
    
    public double getDistanceFromTarget(){
    	return distanceFromGoal;
    }
    
    public double getYawAngleTarget(){
    	return (Math.atan((centerX - (Image_Width / 2))/focalLength));
    }
    
    public boolean isTargetFound(){
    	if ((Math.abs(xOffset) < 2) && (Math.abs(yOffset) < 2)){
    		return true;
    	}
    	else
    		return false;
    }
    	
    public double getCenterX(){
    	return centerX;
    }
    public double getCenterY(){
    	return centerY;
    }
    
    public double getXOffset(){
    	return xOffset;
    }
    public double getYOffset(){
    	return yOffset;
    }
}