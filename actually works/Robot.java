// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// https://gist.github.com/jcorcoran/e56d985fd1ee290b075d13c9f9aa3595
// our god and savior

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // i have no idea
  private static final XboxController controller = new XboxController(0); 
  // private CANSparkMax saprky = new CANSparkMax(4 , MotorType.kBrushless);
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  // creating a drivetrain object used for driving the robot using joysticks
  //parameters: joysticks ports
  public Drive_Train driveDriveTrain = new Drive_Train(8, 9, 0);// left, right, controller

  boolean shooting;
  boolean opening; 
  boolean feeding; 
  boolean rolling;


  public BuiltActions actions; 
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    // initializing drive trains
    driveDriveTrain.init();
    driveDriveTrain.resetRotation();

    actions = new BuiltActions();   
    
    shooting = false;
    opening = false;
    feeding = false;
    rolling = false;


  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    driveDriveTrain.autonomusInit();

    driveDriveTrain.resetMovement();
  
    // driveDriveTrain.addDriveAction(actions.findTape());
    driveDriveTrain.addShootingAction(new Action("Set Power", 0.1));

    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      // calling shooter    
    
      default:
        // driveDriveTrain.testVelocity();
        // driveDriveTrain.testEncoders();
        // driveDriveTrain.driveBySpeed(1);
        if(controller.getAButtonPressed())

        driveDriveTrain.makeShootingAction();
        driveDriveTrain.makeIntakeAction();
        driveDriveTrain.makeDriveAction();
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // driveDriveTrain.intakeTeleOp();
    // driveDriveTrain.feederTeleOp();
    // driveDriveTrain.shootingTeleOp();

    // driveDriveTrain.MakeIntakeAction();
    // if(cont.getRightBumperPressed())
    // driveDriveTrain.JoyStickDriveBySpeed();
    // driveDriveTrain.ControllerDriveBySpeed();
    // driveDriveTrain.testController();
    
    if(controller.getAButtonPressed() && !shooting){
      driveDriveTrain.addShootingAction(new Action("Set Power", 0.7));
      // driveDriveTrain.addShootingAction(new Action("Shoot", 0.2));
      shooting = true;
    }
    else if(controller.getAButtonPressed()){
      // driveDriveTrain.addShootingAction(new Action("Wait"));
      driveDriveTrain.addShootingAction(new Action("Set Power", 0));
      shooting = false;
    }

    if(controller.getBButtonPressed() && !opening){
      driveDriveTrain.addIntakeAction(new Action("Open"));
      opening = true;
    }
    else if(controller.getBButtonPressed()){
      // driveDriveTrain.addShootingAction(new Action("Wait"));
      driveDriveTrain.addIntakeAction(new Action("Close"));
      opening = false;
    }

    if(controller.getYButtonPressed() && !feeding){
      driveDriveTrain.addFeederAction(new Action("Feed"));
      feeding = true;
    }
    else if(controller.getYButtonPressed()){
      // driveDriveTrain.addShootingAction(new Action("Wait"));
      driveDriveTrain.addFeederAction(new Action("Stop Feeding"));
      feeding = false;
    }

    if(controller.getXButtonPressed() && !rolling){
      driveDriveTrain.addIntakeAction(new Action("Take"));
      rolling = true;
    }
    else if(controller.getXButtonPressed()){
      // driveDriveTrain.addShootingAction(new Action("Wait"));
      driveDriveTrain.addIntakeAction(new Action("Stop Taking"));
      rolling = false;
    }


      // driveDriveTrain.setClimbing(controller.getLeftY() * 0.8);
      // if (controller.getAButtonPressed()) driveDriveTrain.resetClimbing();
    // if (controller.getBButtonPressed() && driveDriveTrain.intakeOpen()){
    //   driveDriveTrain.addIntakeAction(act);
    // }
    
    driveDriveTrain.makeShootingAction();
    driveDriveTrain.makeIntakeAction();
    driveDriveTrain.makeDriveAction();
    driveDriveTrain.makeFeedingAction();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    driveDriveTrain.disabled();    
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {
  }

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
  }
}
