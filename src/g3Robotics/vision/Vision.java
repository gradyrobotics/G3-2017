package g3Robotics.vision;

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
    	UsbCamera camera =  CameraServer.getInstance().startAutomaticCapture(0);
    	camera.setResolution(Image_Width, Image_Height);
    	//camera.setExposureManual(30);
    	//camera.setExposureHoldCurrent();
    	
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