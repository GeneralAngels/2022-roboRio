package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;

public class Drive_Train {
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    
    // initializing the motors
    private final TalonFX leftDownMotor = new TalonFX(3);
    private final TalonFX leftUpMotor = new TalonFX(2);
    private final TalonFX rightDownMotor = new TalonFX(0);
    private final TalonFX rightUpMotor = new TalonFX(1);

    // initializing the encoders
    private final Encoder leftEncoder = new Encoder(2, 3, true, Encoder.EncodingType.k4X);
    private final Encoder rightEncoder = new Encoder(1, 0, false, Encoder.EncodingType.k4X);

    // defining wheel diameters and settings
    private final double WHEEL_RADIUS = 7.62;
    private final double TICKS_PER_REVOLUTION = 2048;
    private final double ENCODER_TO_RADIAN = (2 * Math.PI) / TICKS_PER_REVOLUTION;
    private final double ENCODER_TO_CM = ENCODER_TO_RADIAN * WHEEL_RADIUS;
  
    // max speed of the motors (out of 1)
    private final double maxSpeed = 0.3;

    // making PID controllers for each side of the robot
    private PID pidLeft = new PID(-0.008, 0, 0);
    private PID pidRight = new PID(0.008, 0, 0);
    private double leftError;
    private double rightError;

    // currently only sets the joysticks
    public Drive_Train(int leftJoystickPort, int rightJoystickPort){
        rightJoystick = new Joystick(rightJoystickPort);
        leftJoystick = new Joystick(leftJoystickPort);
    }

    public void Init(){
        leftDownMotor.setInverted(true);
        leftUpMotor.setInverted(true);

        // setting how many cm the robot moves each pulse
        // will get the distance travelled when calling encoder.getDistance
        leftEncoder.setDistancePerPulse(ENCODER_TO_CM);
        rightEncoder.setDistancePerPulse(ENCODER_TO_CM);
    }

    public void JoyStickDrive(){
        double leftPow = leftJoystick.getY();
        double rightPow = rightJoystick.getY();

        leftDownMotor.set(TalonFXControlMode.PercentOutput, maxSpeed * leftPow);
        leftUpMotor.set(TalonFXControlMode.PercentOutput,  maxSpeed * leftPow);
        rightDownMotor.set(TalonFXControlMode.PercentOutput, maxSpeed * rightPow);
        rightUpMotor.set(TalonFXControlMode.PercentOutput, maxSpeed * rightPow);
    }

    public void Disabled(){
        // stop the motors when the code is disabled
        leftDownMotor.set(ControlMode.PercentOutput, 0);
        leftUpMotor.set(ControlMode.PercentOutput, 0);
        rightDownMotor.set(ControlMode.PercentOutput, 0);
        rightUpMotor.set(ControlMode.PercentOutput, 0);
    }
    
    
}
