package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

// import javax.xml.namespace.QName;

public class Drive_Train {
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    private XboxController controller;
    
    // initializing the motors
    private final MotorGroup LeftGroup = new MotorGroup(3, 2, "Left");
    private final MotorGroup RightGroup = new MotorGroup(0, 1, "Right");
    private final TalonFX shooting1 = new TalonFX(4);
    private final TalonFX shooting2 = new TalonFX(5);

    private final Gyroscope pidgey = new Gyroscope(30);

    private final Limelight limey = new Limelight();

    // max speed of the motors (out of 1)
    private final double maxSpeed = 0.25;

    // the distance it traveled since last stopped
    private double robo_dist = 0;

    // making PID controllers for each side of the robot
    private PID add_sPID_right = new PID(0.5,0,0.3, roboConsts.MAX_POWER);
    private PID add_sPID_left = new PID(0.5,0,0.3, roboConsts.MAX_POWER);

    private PID rotationPID = new PID(0.4, 0.11, 0.15, roboConsts.MAX_TURN_POWER);

    // currently only sets the joysticks
    public Drive_Train(int leftJoystickPort, int rightJoystickPort, int xboxPort){
        rightJoystick = new Joystick(rightJoystickPort);
        leftJoystick = new Joystick(leftJoystickPort);
        controller = new XboxController(xboxPort);
    }

    public void Init(){
        LeftGroup.SetInverted(false);
        RightGroup.SetInverted(true);

        shooting1.setInverted(false);
        shooting2.setInverted(true);
        
        RightGroup.SetPID(0.9, 0.4, 0.2);
        RightGroup.SetSPID(0.4, 0.15, 0.1);
        
        LeftGroup.SetPID(0.9, 0.4, 0.2);
        LeftGroup.SetSPID(0.4, 0.15, 0.1);

        rotationPID.Reset();
        
        resetEncoders();
    }

    public void testVelocity(){
        RightGroup.testVelocity();
        LeftGroup.testVelocity();
    }

    public void AutonomusInit(){
        LeftGroup.ResetPID();
        RightGroup.ResetPID();
        add_sPID_left.Reset();
        add_sPID_right.Reset();
        resetEncoders();
    }

    public void JoyStickDrive(){

        // setting the power to drive at
        double leftPow = -1 * maxSpeed * leftJoystick.getY();
        double rightPow = -1 * maxSpeed * rightJoystick.getY();

        LeftGroup.SetPower(leftPow);
        RightGroup.SetPower(rightPow);  
    }

    public void driveByPower(double power){
        RightGroup.SetPower(power);
        LeftGroup.SetPower(power);
    }

    public void driveTrainPeriodic(){
        RightGroup.motorPeriodic();
        LeftGroup.motorPeriodic();
    }

    /** this function uses the distance that was set to drive that distance*/
    public void driveByDist(){
        //new code:
        System.out.println("lef power:   " + LeftGroup.DriveByDistance());
        System.out.println("right power:   " + RightGroup.DriveByDistance());
        robo_dist = (LeftGroup.getDistance() + RightGroup.getDistance()) /2;
        System.out.println(robo_dist);
    }

    public void driveBySpeed(double speed){
        RightGroup.driveBySpeed(speed);
        LeftGroup.driveBySpeed(speed);
    }

    public void testEncoders(){
        RightGroup.testEncoders();
        LeftGroup.testEncoders();
    }

    public void resetEncoders()
    {
        RightGroup.resetEncoders();
        LeftGroup.resetEncoders();
    }

    public void Disabled(){
        // stop the motors when the code is disabled
        disableMotors();
    }

    public void disableMotors(){
        RightGroup.SetPower(0);
        LeftGroup.SetPower(0);
    }

    public void testPidgey(){
        System.out.println(pidgey.getAngle());
    }

    public void turn(double degrees){
        // pidgey.reset();
        // rotationPID.Reset();

        // while (!(pidgey.inRange(degrees-1, degrees+1))){            
            double degreeError = (degrees - pidgey.getAngle()) / 180;
            double powerCalc = rotationPID.Compute(degreeError);
            
            RightGroup.SetPower(-powerCalc);
            LeftGroup.SetPower(powerCalc);
            System.out.println(powerCalc);
            System.out.println(degrees);
            // printDegrees();

        // }
        
        // Disabled();

    }
    public void resetRotation(){
        pidgey.reset();
        rotationPID.Reset();
        disableMotors();
    }

    public void printDegrees() {System.out.println(pidgey.getAngle());}

    public void printDegreesFromTarget(){
        // System.out.println("lol");
        System.out.println(limey.getXAngle());
        // ;
    }

    public double getDegreesFromTarget(){
        return limey.getXAngle();
    }

    public boolean TargetOnScreen(){
        return limey.hasValidTarget();

    }

}
