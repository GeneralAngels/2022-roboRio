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


    // the distance it traveled since last stopped
    private double robo_dist = 0;

    private double travel_dist;

    private PID rotationPID = new PID(0.5, 0.15, 0.1, roboConsts.MAX_TURN_POWER);

    private AutoSchedule driveSchedule;

    // currently only sets the joysticks
    public Drive_Train(int leftJoystickPort, int rightJoystickPort, int xboxPort){
        rightJoystick = new Joystick(rightJoystickPort);
        leftJoystick = new Joystick(leftJoystickPort);
        controller = new XboxController(xboxPort);
    }

    public void AddAction(String name, double target){
        
        // my funky one ;)
        driveSchedule.addAction(new Action(name, target));
    }

    public AutoSchedule getSchedule(){
        return driveSchedule;
    }
    
    public void MakeAction(){
        
        if (driveSchedule.getAction() == null){
            disableMotors();
            ResetAll();
        }
        else
        {
            if (driveSchedule.currentActionName() == "Drive Distance"){
                
                if (robo_dist == 0){
                    ResetPID();
                    SetTravelDist(driveSchedule.getAction().getTarget());
                }

                if (!driveSchedule.currentActionFinished(robo_dist, 0.02)){
                    driveByDist();
                }

                else {
                    ResetAll();
                    driveSchedule.removeAction();
                }
            }

            else if(driveSchedule.currentActionName() == "Turn")
            {
                if(!driveSchedule.currentActionFinished(pidgey.getAngle(), 3)){
                    turn(driveSchedule.getAction().getTarget());
                }

                else {
                    ResetAll();
                    driveSchedule = driveSchedule.getNext();
                }
            }
            
        }
    }

    public void Init(){
        LeftGroup.SetInverted(false);
        RightGroup.SetInverted(true);

        shooting1.setInverted(false);
        shooting2.setInverted(true);
        
        RightGroup.SetDistancePID(0.4, 0.13, 0.14);
        RightGroup.SetSPID(0.07, 0.01, 0.01);
        
        LeftGroup.SetDistancePID(0.4, 0.13, 0.14);
        LeftGroup.SetSPID(0.07, 0.01, 0.01);

        rotationPID.Reset();
        driveSchedule = new AutoSchedule();
        resetEncoders();
    }

    public void testVelocity(){
        RightGroup.testVelocity();
        LeftGroup.testVelocity();
    }

    public void AutonomusInit(){
        LeftGroup.ResetDistancePID();
        RightGroup.ResetDistancePID();
        ResetSPID();
        resetEncoders();

        driveSchedule = new AutoSchedule();
    }

    public void SetTravelDist(double distance){
        robo_dist = 0;
        travel_dist = distance;
        RightGroup.SetTravelDistance(distance);
        LeftGroup.SetTravelDistance(distance);
    }

    public void JoyStickDrive(){

        // setting the power to drive at
        double leftPow = -1 * roboConsts.MAX_POWER * leftJoystick.getY();
        double rightPow = -1 * roboConsts.MAX_POWER * rightJoystick.getY();

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
        LeftGroup.DriveByDistance();
        RightGroup.DriveByDistance();
        robo_dist = (LeftGroup.getDistance() + RightGroup.getDistance())/2;
    }

    public void driveBySpeed(double speed){
        RightGroup.driveBySpeed(speed);
        LeftGroup.driveBySpeed(speed);
    }

    public void testEncoders(){
        RightGroup.testEncoders();
        LeftGroup.testEncoders();
    }

    public void resetEncoders(){
        RightGroup.resetEncoders();
        LeftGroup.resetEncoders();
    }

    public void Disabled(){
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
            
            RightGroup.SetPower(powerCalc);
            LeftGroup.SetPower(-powerCalc);
            System.out.println(powerCalc);
            System.out.println("Degrees: " + pidgey.getAngle());
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

    public void ResetSPID(){
        LeftGroup.ResetSPID();
        RightGroup.ResetSPID();
    }

    public void ResetPID(){
        LeftGroup.ResetDistancePID();
        RightGroup.ResetDistancePID();
    }

    public void ResetAll(){
        resetEncoders();
        ResetSPID();
        ResetPID();
        resetRotation();
        resetDistance();
    }

    public void resetDistance(){
        robo_dist = 0;
        LeftGroup.SetTravelDistance(0);
        RightGroup.SetTravelDistance(0);
    }

}
