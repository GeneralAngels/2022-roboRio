// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.PID;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final VictorSP leftDownMotor = new VictorSP(1);
  private final VictorSP leftUpMotor = new VictorSP(0);
  private final VictorSP rightDownMotor = new VictorSP(5);
  private final VictorSP rightUpMotor = new VictorSP(4);

  private final SpeedControllerGroup leftControllerGroup = new SpeedControllerGroup(leftDownMotor, leftUpMotor);
  private final SpeedControllerGroup rightControllerGroup = new SpeedControllerGroup(rightDownMotor, rightUpMotor);
  private final Encoder leftEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
  private final Encoder rightEncoder = new Encoder(1, 0, false, Encoder.EncodingType.k4X);

  private static final double WHEEL_RADIUS = 7.62;
  private static final double TICKS_PER_REVOLUTION = 2048;
  private static final double ENCODER_TO_RADIAN = (2 * Math.PI) / TICKS_PER_REVOLUTION;
  private static final double ENCODER_TO_CM = ENCODER_TO_RADIAN * WHEEL_RADIUS;
  private PID pidLeft = new PID(0.004, 0, 0);
  private PID pidRight = new PID(0.008, 0, 0);

  private double leftError;
  private double rightError;
  private double distance;
  double rightDis;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    leftEncoder.setDistancePerPulse(ENCODER_TO_CM);
    rightEncoder.setDistancePerPulse(ENCODER_TO_CM);
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
    distance = 100;
    leftControllerGroup.setInverted(true);
    leftEncoder.reset();
    rightEncoder.reset();
    leftError = distance;
    rightError = distance;
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
      // Put default auto code here
      double leftDis = leftEncoder.getDistance();
      rightDis = rightEncoder.getDistance();

      if (leftError > 5) {
        leftError = distance - leftDis;
        double leftPow = pidLeft.Compute(leftError);
        System.out.println("LeftEr: " + leftDis);
        System.out.println("Left: " + leftPow);
        leftControllerGroup.set(leftPow);
      }

      if (rightError > 5) {
        rightError = distance - rightDis;
        double rightPow = pidRight.Compute(rightError);
        System.out.println("RighttEr: " + rightDis);
        System.out.println("Right: " + rightPow);
        rightControllerGroup.set(rightPow);
      }
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
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
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
