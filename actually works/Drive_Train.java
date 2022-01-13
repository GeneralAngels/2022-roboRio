package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import javax.xml.namespace.QName;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class Drive_Train {
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    
    // initializing the motors
    private final TalonFX leftDownMotor = new TalonFX(3);
    private final TalonFX leftUpMotor = new TalonFX(2);
    private final TalonFX rightDownMotor = new TalonFX(0);
    private final TalonFX rightUpMotor = new TalonFX(1);

    // defining wheel diameters and settings
    // private final double WHEEL_RADIUS = 7.62;
    // private final double TICKS_PER_REVOLUTION = 2048;
    // private final double ENCODER_TO_RADIAN = (2 * Math.PI) / TICKS_PER_REVOLUTION;
    // private final double ENCODER_TO_CM = ENCODER_TO_RADIAN * WHEEL_RADIUS;

    private final double WHEEL_RADIUS = 0.0762;
    private final double PULSES_PER_REVOLUTION = 15000;
    private final double ENCODER_TO_RADIAN = (2 * Math.PI) / PULSES_PER_REVOLUTION;
    private final double ENCODER_TO_M = ENCODER_TO_RADIAN * WHEEL_RADIUS;
  
    // max speed of the motors (out of 1)
    private final double maxSpeed = 0.3;

    // tells the robot what distance to travel
    private double travel_dist = 0;

    // the distance it raveled since last stopped
    private double robo_dist = 0;

    // making PID controllers for each side of the robot
    private PID pidLeft = new PID(0.003, 0.002, 0.001);
    private PID pidRight = new PID(0.003, 0.002, 0.001);
    private double leftError;
    private double rightError;

    // currently only sets the joysticks
    public Drive_Train(int leftJoystickPort, int rightJoystickPort){
        rightJoystick = new Joystick(rightJoystickPort);
        leftJoystick = new Joystick(leftJoystickPort);
    }

    public void Init(){
        leftDownMotor.setInverted(false);
        leftUpMotor.setInverted(false);
        rightDownMotor.setInverted(true);
        rightUpMotor.setInverted(true);

        rightDownMotor.setSelectedSensorPosition(0);
        leftDownMotor.setSelectedSensorPosition(0);

        
        // setting how many cm the robot moves each pulse
        // will get the distance travelled when calling encoder.getDistance
    }

    public void JoyStickDrive(){

        // setting the power to drive at
        // 
        double leftPow = -1 * maxSpeed * leftJoystick.getY();
        double rightPow = -1 * maxSpeed * rightJoystick.getY();

        setLeftGroup(leftPow);
        setRightGroup(rightPow);
        

    }

    /** sets the distance to drive in cm */
    public void setDist(double distance){
        robo_dist = 0;
        travel_dist = distance;
    }

    /** this function uses the distance that was set to drive that distance*/
    public void driveByDist(){

        double right_dist = rightDownMotor.getSelectedSensorPosition() * ENCODER_TO_M; 
        double left_dist = leftDownMotor.getSelectedSensorPosition() * ENCODER_TO_M;

        rightError = travel_dist - right_dist;
        leftError = travel_dist - left_dist;
        // System.out.println("right:  " + rightDownMotor.getSelectedSensorPosition());
        System.out.println("right:  " + right_dist);
        // System.out.println("left:   " + leftDownMotor.getSelectedSensorPosition());
        System.out.println("left:  " + left_dist);
        // System.out.println(travel_dist);
        // if (right_dist < travel_dist){
        //     setRightGroup(maxSpeed * pidRight.Compute(rightError));
        // }
        // else
        //     setRightGroup(0);
        // if (left_dist < travel_dist){
        //     setRightGroup(maxSpeed * pidRight.Compute(rightError));
        // }
        // else
        //     setLeftGroup(0);
        setRightGroup(maxSpeed * pidRight.Compute(rightError));
        setLeftGroup(maxSpeed * pidLeft.Compute(leftError));
    }

    public boolean canDrive(){
        if (robo_dist > travel_dist) return false;
        return true;
    }

    public void Disabled(){
        // stop the motors when the code is disabled
        disableMotors();
    }

    public void setRightGroup(double power){
        rightDownMotor.set(ControlMode.PercentOutput, power);
        rightUpMotor.set(ControlMode.PercentOutput, power);   
    }

    public void setLeftGroup(double power){
        leftDownMotor.set(ControlMode.PercentOutput, power);
        leftUpMotor.set(ControlMode.PercentOutput, power);   
    }

    public void disableMotors(){
        setLeftGroup(0);
        setRightGroup(0);
    }
    
    
}
