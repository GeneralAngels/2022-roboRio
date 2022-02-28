package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.ScheduleCommand;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.DigitalInput;

import org.opencv.video.TrackerGOTURN;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;


// import javax.xml.namespace.QName;

public class Drive_Train {
    private Joystick rightJoystick;
    private Joystick leftJoystick;
    private XboxController controller;
    
    // initializing the motors
    private final MotorGroup LeftGroup = new MotorGroup(3, 2, roboConsts.WHEEL_DIAMETER, "Left");
    private final MotorGroup RightGroup = new MotorGroup(0, 1,roboConsts.WHEEL_DIAMETER, "Right");
    // private final TalonFX shooting1 = new TalonFX(4);
    // private final TalonFX shooting2 = new TalonFX(5);

    private final Gyroscope pidgey = new Gyroscope(30);

    private final Limelight limey = new Limelight();

    DigitalInput rollerSwitch = new DigitalInput(0);


    // the distance it traveled since last stopped
    private double robo_dist = 0;

    private double travel_dist;

    private PID rotationPID = new PID(0.5, 0.15, 0.1, roboConsts.MAX_TURN_POWER);

    private final RoboIntake intake = new RoboIntake(11, 9, 8);

    private AutoSchedule driveSchedule;
    private AutoSchedule intakeSchedule;
    private AutoSchedule feederSchedule;
    private AutoSchedule shootingSchedule;

    private Feeder feeder = new Feeder(4);
    private Shooting shooter = new Shooting(10, 6, 7);
    private Climbing climbing = new Climbing(5);

    private BuiltActions builtActions = new BuiltActions();


    private TalonFX rightShooterFalcon = new TalonFX(6);
    private TalonFX leftShooterFalcon = new TalonFX(7);
    
    public Drive_Train(int leftJoystickPort, int rightJoystickPort, int xboxPort){
        rightJoystick = new Joystick(rightJoystickPort);
        leftJoystick = new Joystick(leftJoystickPort);
        controller = new XboxController(xboxPort);

    }

    public void setClimbing(double power){
        climbing.setPower(power);
    }

    public void resetClimbing(){
        climbing.resetEncoder();
    }

    public void intakeTeleOp(){
        if (controller.getBButtonPressed()){
            intakeSchedule.reset();
            if (intake.isOpened()){
                intakeSchedule.addAction(new Action("Close"));
            }
            else{
                intakeSchedule.addAction(new Action("Open"));
            }
        }
        else if (controller.getYButtonPressed()){
            if (intake.isTaking()){
                if(intakeSchedule.getAction().getActionName() == "Take")
                    intakeSchedule.removeAction();
                intakeSchedule.addAction(new Action("Stop Feeding"));                
            }
            else {
                intakeSchedule.removeAction();
                intakeSchedule.addAction(new Action("Take"));
            }
        }
        // System.out.println(controller.getBButtonPressed());
    }

    public void shootingTeleOp(){
        
        if(controller.getAButtonPressed()){
            addShootingAction(new Action("Set Power", 0.2));
            // driveDriveTrain.addShootingAction(new Action("Shoot", 0.2));
        }
        else if(controller.getAButtonPressed()){
            // driveDriveTrain.addShootingAction(new Action("Wait"));
            addShootingAction(new Action("Set Power", 0));
        }
    }

    public void feederTeleOp(){
        // System.out.println("intake");
        // System.out.println(intake.IsOpen());
        if (controller.getBButtonPressed()){
            intakeSchedule.reset();
            if (intake.isOpened()){
                intakeSchedule.addAction(new Action("Close"));
            }
            else{
                intakeSchedule.addAction(new Action("Open"));
            }
        }
        else if (controller.getYButtonPressed()){
            if (intake.isTaking()){
                if(intakeSchedule.getAction().getActionName() == "Take")
                    intakeSchedule.removeAction();
                intakeSchedule.addAction(new Action("Stop Feeding"));                
            }
            else {
                intakeSchedule.removeAction();
                intakeSchedule.addAction(new Action("Take"));
            }
        }
        // System.out.println(controller.getBButtonPressed());
    }

    /**
     * excecutes the actions in the intake schedule
     * after an action is done excecuting it will delete it from the schedule
     */
    public void makeIntakeAction(){
        System.out.println(intakeSchedule.toString());
        
        if (intakeSchedule.getAction() == null){
            // System.out.println("no intake actions");
        }
        else
        {
            switch (intakeSchedule.currentActionName()){
                case "Open":
                    intake.open();

                    if (intake.isOpened()){
                        intakeSchedule.removeAction();
                    }
                    break;
                case "Close":
                    intake.close();

                    if (intake.isClosed()){
                        intakeSchedule.removeAction();
                    }
                    break;
                case "Take":
                    intake.take();
                    // feeder.feed();
                    // shootingSchedule.reset();
                    // shootingSchedule.addAction(new Action("Set Power", -0.1));
                    intakeSchedule.removeAction();
                    
                    break;
                case "Stop Taking":
                    intake.stopTaking();
                    intakeSchedule.removeAction();

                    break;
                default:
                    System.out.println("not an implemeted action");
            }
        }
    }

    public void makeFeedingAction(){
        System.out.println(intakeSchedule.toString());
        
        if (feederSchedule.getAction() == null){
            // System.out.println("no intake actions");
        }
        else
        {
            switch (feederSchedule.currentActionName()){
                case "Feed":
                    feeder.feed();
                    break;
                case "Stop Feeding":
                    feeder.stopFeeding();
                    break;
                default:
                    System.out.println("not an implemeted action");
            }
        }
    }

    /**
     * excecutes the actions in the drive schedule
     * after an action is done excecuting it will delete it from the schedule
     */
    public void makeDriveAction(){
        
        if (driveSchedule.getAction() == null){
            disableGroupMotors();
            resetMovement();
        }
        else
        {
            if (driveSchedule.currentActionName() == "Drive Distance"){
                
                if (robo_dist == 0){
                    resetDistancePID();
                    setTravelDist(driveSchedule.getAction().getTarget());
                }

                if (!driveSchedule.currentActionFinished(robo_dist, 0.02)){
                    driveByDist();
                }

                else {
                    disableGroupMotors();
                    resetMovement();
                    driveSchedule.removeAction();
                }
            }

            else if(driveSchedule.currentActionName() == "Turn")
            {
                if(!driveSchedule.currentActionFinished(pidgey.getAngle(), 3)){
                    turn(driveSchedule.getAction().getTarget());
                }

                else {
                    disableGroupMotors();
                    resetMovement();
                    driveSchedule.removeAction();
                }
            }
            
            else if (driveSchedule.currentActionName() == "Search Tape"){
                new Print(targetOnScreen());
                if (!targetOnScreen()){
                    LeftGroup.setPower(0.1);
                    RightGroup.setPower(-0.1);
                }
                else {
                    disableGroupMotors();
                    resetMovement();
                    driveSchedule.removeAction();
                }
            }

            else if (driveSchedule.currentActionName() == "Turn To Tape"){
                if(!driveSchedule.currentActionFinished(limey.getXAngle(), 1)){
                    turnToTarget(-limey.getXAngle());
                }

                else {
                    double distance = 1; // limey.getDistance():
                    shooter.setVector(calcShootingVector(distance));
                    shootingSchedule.addAction(builtActions.shoot());
                    disableGroupMotors();
                    resetMovement();
                    driveSchedule.removeAction();
                }
            }

            
        }
    }

    /**
     * excecutes the actions in the shooting schedule
     * after an action is done excecuting it will delete it from the schedule
     */
    public void makeShootingAction()
    {
        System.out.println(shootingSchedule.toString());
        if(shootingSchedule.getAction() != null)
        {
            switch(shootingSchedule.currentActionName())
            {
                case("Wait"):
                    shooter.disable();
                    shootingSchedule.removeAction();
                    break;
                case("Prepare For Shooting"):
                    shooter.setShootingSpeed();
                    shooter.setHoodAngle();
                    if(shooter.ready())
                    {
                        shooter.disableHoodMotor();
                        shootingSchedule.removeAction();
                    }
                    break;
                case("Shoot"):
                    feeder.feed();
                    shootingSchedule.removeAction();
                    break;
                case("Set Power"):
                    shooter.setPower(shootingSchedule.getAction().getTarget());
                    shootingSchedule.removeAction();
                    break;
            }
        }
    }

    /**
     * initializes things such as inversion, PIDs and the schedules
     */
    public void init(){
        leftShooterFalcon.setInverted(true);
        rightShooterFalcon.setInverted(false);

        LeftGroup.setInverted(false);
        RightGroup.setInverted(true);

        RightGroup.setDistancePID(0.4, 0.13, 0.14, roboConsts.MAX_POWER);
        // RightGroup.SetSPID(0.2, 0.01, 0.01, 0.1);
        RightGroup.setSPID(0.2, 0.07, 0.1, 0.1);

        LeftGroup.setDistancePID(0.4, 0.13, 0.14, roboConsts.MAX_POWER);
        // LeftGroup.SetSPID(0.2, 0.01, 0.01, 0.1);
        LeftGroup.setSPID(0.2, 0.07, 0.1, 0.1);

        rotationPID.Reset();
        driveSchedule = new AutoSchedule();
        intakeSchedule = new AutoSchedule();
        feederSchedule = new AutoSchedule();
        shootingSchedule = new AutoSchedule();
        resetGroupEncoders();
    }
    
    /**
     * prints the velocities of each motor group
     */
    public void testVelocity(){
        RightGroup.testVelocity();
        LeftGroup.testVelocity();
    }

    /**
     * initializes the autonamous process,
     * mostly resets values that were initializes initially
     */
    public void autonomusInit(){
        LeftGroup.resetDistancePID();
        RightGroup.resetDistancePID();
        resetSPID();
        resetGroupEncoders();

        driveSchedule = new AutoSchedule();
    }

    /**
     * sets the distance to travel
     * the distance is used in the driveByDistance function
     * @param distance the distance to travel
     */
    public void setTravelDist(double distance){
        robo_dist = 0;
        travel_dist = distance;
        RightGroup.setTravelDistance(distance);
        LeftGroup.setTravelDistance(distance);
    }

    /**
     * reads joystick values and drives using them as power values
     * only reaches the max power from the roboConsts class
     */
    public void joyStickDriveByPower(){
        // setting the power to drive at
        double leftPow = -1 * roboConsts.MAX_POWER * leftJoystick.getY();
        double rightPow = -1 * roboConsts.MAX_POWER * rightJoystick.getY();

        LeftGroup.setPower(leftPow);
        RightGroup.setPower(rightPow);  
    }

    /**
     * reads joystick values and drives using them as meter per sec
     * only reaches the max velocity from the roboConsts class
     */
    public void joyStickDriveBySpeed(){
        // setting the power to drive at
        double leftIn = -1 * leftJoystick.getY() * roboConsts.MAX_SPEED;
        double rightIn = -1 * rightJoystick.getY() * roboConsts.MAX_SPEED;

        LeftGroup.driveBySpeed(leftIn);
        RightGroup.driveBySpeed(rightIn);  
    }

    /**
     * drives the given power
     * @param power the power to drive in
     */
    public void driveByPower(double power){
        RightGroup.setPower(power);
        LeftGroup.setPower(power);
    }

    /** 
     * this function uses the distance that was set to drive that distance
     */
    public void driveByDist(){
        LeftGroup.driveByDistance();
        RightGroup.driveByDistance();
        robo_dist = (LeftGroup.getDistance() + RightGroup.getDistance())/2;
    }

    /**
     * drives the given velocity
     * @param speed the velocity to drive in (meter per sec)
     */
    public void driveBySpeed(double speed){
        RightGroup.driveBySpeed(speed);
        LeftGroup.driveBySpeed(speed);
    }

    /**
     * prints the encoder values from each group
     */
    public void testEncoders(){
        RightGroup.testEncoders();
        LeftGroup.testEncoders();
    }

    /**
     * resets the encoders of each group
     */
    public void resetGroupEncoders(){
        RightGroup.resetEncoders();
        LeftGroup.resetEncoders();
    }

    /**
     * disables the motors of each group
     */
    public void disableGroupMotors(){
        RightGroup.setPower(0);
        LeftGroup.setPower(0);
    }

    /**
     * this function should be called when the robot is disables
     * will reset movement values and disable all motors
     */
    public void disabled(){
        disableGroupMotors();
        resetMovement();
    }

    /**
     * will turn the robot according to the given degrees
     * @param degrees the degrees to turn (positive = right, negative = left)
     */
    public void turn(double degrees){         
        double degreeError = (degrees - pidgey.getAngle()) / 180;
        double powerCalc = rotationPID.Compute(degreeError);
        
        RightGroup.setPower(powerCalc);
        LeftGroup.setPower(-powerCalc);
        System.out.println(powerCalc);
        System.out.println("Degrees: " + pidgey.getAngle());
    }

    /**
     * will turn the robot according to where the limelight target is
     * should be called periodically
     * @param degrees idk lol
     */
    public void turnToTarget(double degrees){         
        double degreeError = degrees;
        double powerCalc = rotationPID.Compute(degreeError);
        
        RightGroup.setPower(powerCalc);
        LeftGroup.setPower(-powerCalc);
        System.out.println(powerCalc);
        System.out.println("Degrees: " + limey.getXAngle());
    }

    /**
     * calculates the vector of the ball that will be shot
     * @param distance the distance from the hub
     * @return the vector of the ball
     */
    public Vector calcShootingVector(double distance)
    {
        double angle = Math.atan( 2 * roboConsts.HieghtDifference / distance - Math.tan(roboConsts.EndAngle));
        double velocity = Math.sqrt(9.8 * distance 
        / (Math.cos( angle) * Math.sin(angle) - 
        Math.tan(roboConsts.EndAngle) * Math.pow(Math.cos(angle), 2)));
        return new Vector(angle, velocity);
    }

    /**
     * resets all values that are connected to rotation
     */
    public void resetRotation(){
        pidgey.reset();
        rotationPID.Reset();
        disableGroupMotors();
    }

    /**
     * @return the x degrees from the target
     */
    public double getXDegreesFromTarget(){
        return limey.getXAngle();
    }

    /**
     * @return whether or not the target is on screen
     */
    public boolean targetOnScreen(){
        return limey.hasValidTarget();

    }

    /**
     * resets the speed PIDs (michael was very proud of that one)
     */
    public void resetSPID(){
        LeftGroup.resetSPID();
        RightGroup.resetSPID();
    }

    /**
     * resets the distance PIDs
     */
    public void resetDistancePID(){
        LeftGroup.resetDistancePID();
        RightGroup.resetDistancePID();
    }

    /**
     * resets all values and PIDs that are connected to the movement
     */
    public void resetMovement(){
        resetGroupEncoders();
        resetSPID();
        resetDistancePID();
        resetRotation();
        resetDistance();
    }

    /**
     * resets all things that are connected to the distance
     */
    public void resetDistance(){
        robo_dist = 0;
        LeftGroup.setTravelDistance(0);
        RightGroup.setTravelDistance(0);
    }

    /**
     * adds an action to the drive schedule
     * @param act the action to add
     */
    public void addDriveAction(Action act){
        driveSchedule.addAction(act);
    }

    /**
     * adds actions to the drive schedule
     * @param actBunch the action to add
     */
    public void addDriveAction(AutoSchedule actBunch){
        driveSchedule.addAction(actBunch);
    }

    /**
     * adds an action to the feeder schedule
     * @param act the action to add
     */
    public void addFeederAction(Action act){
        feederSchedule.addAction(act);
    }

    /**
     * adds an actions to the feeder schedule
     * @param actBunch the actions to add
     */
    public void addFeederAction(AutoSchedule actBunch){
        feederSchedule.addAction(actBunch);
    }

    /**
     * adds an action to the intake schedule
     * @param act the action to add
     */
    public void addIntakeAction(Action act){
        intakeSchedule.addAction(act);
    }

    /**
     * adds an actions to the intake schedule
     * @param actBunch the actions to add
     */
    public void addIntakeAction(AutoSchedule actBunch){
        intakeSchedule.addAction(actBunch);
    }

    /**
     * adds an action to the shooting schedule
     * @param act the action to add
     */
    public void addShootingAction(Action act){
        shootingSchedule.addAction(act);
    }

    /**
     * adds a actions to the shooting schedule
     * @param actBunch the actions to add
     */
    public void addShootingAction(AutoSchedule actBunch){
        shootingSchedule.addAction(actBunch);
    }

    public void controllerDriveBySpeed(){
        // setting the power to drive at
        double leftIn = -1 * controller.getLeftY() * roboConsts.MAX_SPEED;
        double rightIn = -1 * controller.getRightY() * roboConsts.MAX_SPEED;

        LeftGroup.driveBySpeed(leftIn);
        RightGroup.driveBySpeed(rightIn); 
    }

    /**
     * @return the distance from the tape target
     */
    public double getXDistFromTape(){
        double h = roboConsts.HUB_HEIGHT - roboConsts.CAM_HEIGHT;
        double a = roboConsts.CAM_ANGLE + limey.getYAngle();
        return h / Math.tan(a);
    }

    public boolean intakeOpen(){ return intake.isOpened(); }
}
