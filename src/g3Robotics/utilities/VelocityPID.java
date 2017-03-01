package g3Robotics.utilities;

import edu.wpi.first.wpilibj.util.BoundaryException;

public class VelocityPID{
    private double m_P;			// factor for "proportional" control
    private double m_I;			// factor for "integral" control
    private double m_D;			// factor for "derivative" control
    private double m_maximumOutput = 1.0;	// |maximum output|
    private double m_minimumOutput = -1.0;	// |minimum output|
    private double m_maximumInput = 0.0;		// maximum input - limit setpoint to this
    private double m_minimumInput = 0.0;		// minimum input - limit setpoint to this
    private boolean m_continuous = false;	// do the endpoints wrap around? eg. Absolute encoder
    private double vel_prevError = 0.0;	// the prior sensor input (used to compute velocity)
    private double vel_totalError = 0.0; //the sum of the errors for use in the integral calc
    private double vel_setpoint = 0.0;
    private double vel_error = 0.0;
    
    
    private double m_result = 0.0;
    private double m_prevResult = 0.0;
    private double m_last_input = Double.NaN;
    
    private double dt = 0.1;

    public VelocityPID(double Kp, double Ki, double Kd){
    	m_P = Kp;
    	m_I = Ki;
    	m_D = Kd;
    }
    
    public double calculate(double input)
    {
        m_last_input = input;
        vel_error = vel_setpoint - input;
        
        if (m_continuous)
        {
            if (Math.abs(vel_error) >
                    (m_maximumInput - m_minimumInput) / 2)
            {
                if (vel_error > 0)
                {
                    vel_error = vel_error - m_maximumInput + m_minimumInput;
                } else
                {
                    vel_error = vel_error +
                            m_maximumInput - m_minimumInput;
                }
            }
        }

        if ((vel_error * m_P < m_maximumOutput) &&
                (vel_error * m_P > m_minimumOutput))
        {
            vel_totalError += vel_error;
        }
        else
        {
            vel_totalError = 0;
        }

        m_result = (m_P * vel_error + m_I * vel_totalError + m_D * (vel_error - vel_prevError)) + m_prevResult;
        m_prevResult = m_result;
        vel_prevError = vel_error;

        if (m_result > m_maximumOutput)
        {
            m_result = m_maximumOutput;
        } else if (m_result < m_minimumOutput)
        {
            m_result = m_minimumOutput;
        }
        return m_result;
    }
    
    public void setSetpoint(double setpoint)
    {
       vel_setpoint = setpoint;
    }


}