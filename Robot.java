// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // i have no idea
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // initializing the motors
  private final TalonFX leftDownMotor = new TalonFX(15);
  private final TalonFX leftUpMotor = new TalonFX(14);
  private final TalonFX rightDownMotor = new TalonFX(0);
  private final TalonFX rightUpMotor = new TalonFX(1);

  // initializing the encoders
  private final Encoder leftEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
  private final Encoder rightEncoder = new Encoder(1, 0, false, Encoder.EncodingType.k4X);

  // setting the wheels
  private static final double WHEEL_RADIUS = 7.62;
  private static final double TICKS_PER_REVOLUTION = 2048;
  private static final double ENCODER_TO_RADIAN = (2 * Math.PI) / TICKS_PER_REVOLUTION;
  private static final double ENCODER_TO_CM = ENCODER_TO_RADIAN * WHEEL_RADIUS;

  // making PID controllers for each side of the robot
  private PID pidLeft = new PID(0.004, 0, 0);
  private PID pidRight = new PID(0.008, 0, 0);
  private double leftError;
  private double rightError;

  // distance from target location
  private double distance;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // setting how many cm the robot moves each pulse
    // will get the distance travelled when calling encoder.getDistance
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

    // resetting the encoders
    leftEncoder.reset();
    rightEncoder.reset();

    // setting the distance the robot will move (in cm)
    distance = 100;
    leftError = distance;
    rightError = distance;

    // turning off the motors
    leftDownMotor.set(ControlMode.PercentOutput, 0);
    leftUpMotor.set(ControlMode.PercentOutput, 0);
    rightDownMotor.set(ControlMode.PercentOutput, 0);
    rightUpMotor.set(ControlMode.PercentOutput, 0);
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

        // moving the motors in 20% power
        leftDownMotor.set(TalonFXControlMode.PercentOutput, 0.2);
        leftUpMotor.set(TalonFXControlMode.PercentOutput, 0.2);
        rightDownMotor.set(TalonFXControlMode.PercentOutput, 0.2);
        rightUpMotor.set(TalonFXControlMode.PercentOutput, 0.2);

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

    // stop the motors when the code is disabled
    leftDownMotor.set(ControlMode.PercentOutput, 0);
    leftUpMotor.set(ControlMode.PercentOutput, 0);
    rightDownMotor.set(ControlMode.PercentOutput, 0);
    rightUpMotor.set(ControlMode.PercentOutput, 0);
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
