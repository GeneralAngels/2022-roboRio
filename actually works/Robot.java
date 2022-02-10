// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// https://gist.github.com/jcorcoran/e56d985fd1ee290b075d13c9f9aa3595
// our god and savior

package frc.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // i have no idea
  private static final XboxController cont = new XboxController(0); 
  private CANSparkMax saprky = new CANSparkMax(4 , MotorType.kBrushless);
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  // creating a drivetrain object used for driving the robot using joysticks
  //parameters: joysticks ports
  private Drive_Train driveDriveTrain = new Drive_Train(1, 0, 2);

  private boolean doTurn = true;

  private int grace;

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
    driveDriveTrain.Init();

    grace = 0;

    // driveDriveTrain.testVelocity();

    // checkPID();

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

    driveDriveTrain.AutonomusInit();
    
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // driveDriveTrain.motorCheck();
        // driveDriveTrain.driveBySpeed(0.2);
        // driveDriveTrain.driveByPower(0.1);
        // driveDriveTrain.driveTrainPeriodic();
        // driveDriveTrain.testEncoders();
        // driveDriveTrain.testVelocity();

        // if (doTurn){
        //   driveDriveTrain.turn(90);
        //   driveDriveTrain.Disabled();
        //   doTurn = false;
        // }

        // driveDriveTrain.printDegrees();
        // driveDriveTrain.printDegreesFromTarget();

        // if (driveDriveTrain.TargetOnScreen()){
        //   driveDriveTrain.turn(driveDriveTrain.getDegreesFromTarget());
        //   grace = 0;
        // }
        // else
        //   grace++;
        //   if (grace == 2000){
        //     driveDriveTrain.resetRotation();
            
        //   }
        
          
        break;
    }
  }

  // public void checkPID(){
  //   PID pidTest = new PID(0.4, 1, 0.05);

  //   double target = 10;
  //   double thing = 0;
  //   double error = target;

  //   while (thing != 0){
  //     thing += pidTest.Compute(error);
  //     System.out.println(thing);
  //     error = target - thing;
  //   }
  // }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    if(cont.getRightBumper())
      saprky.set(0.3);
    // driving using joysticks
    // driveDriveTrain.JoyStickDrive();
    // driveDriveTrain.testPidgey();

  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    driveDriveTrain.Disabled();
    
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
