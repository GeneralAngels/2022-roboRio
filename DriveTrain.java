// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
// import frc.robot.PID;

public class DriveTrain {
    public static final String kDefaultAuto = "Default";
    public static final String kCustomAuto = "My Auto";
    public String m_autoSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();

    private final TalonFX leftDownMotor = new TalonFX(15);
    private final TalonFX leftUpMotor = new TalonFX(14);
    private final TalonFX rightDownMotor = new TalonFX(0);
    private final TalonFX rightUpMotor = new TalonFX(1);

    private final Encoder leftEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
    private final Encoder rightEncoder = new Encoder(1, 0, false, Encoder.EncodingType.k4X);

    private static final double WHEEL_RADIUS = 7.62;
    private static final double TICKS_PER_REVOLUTION = 2048;
    private static final double ENCODER_TO_RADIAN = (2 * Math.PI) / TICKS_PER_REVOLUTION;
    private static final double ENCODER_TO_CM = ENCODER_TO_RADIAN * WHEEL_RADIUS;

    private PID pidLeft = new PID(0.004, 0, 0);
    private PID pidRight = new PID(0.004, 0, 0);

    private double leftError;
    private double rightError;
    private double distance;
    private double rightDist;
    private double leftDist;

    public void roboInit() {
        m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
        m_chooser.addOption("My Auto", kCustomAuto);
        SmartDashboard.putData("Auto choices", m_chooser);
        leftEncoder.setDistancePerPulse(ENCODER_TO_CM);
        rightEncoder.setDistancePerPulse(ENCODER_TO_CM);
    }

    public void autoInit() {
        m_autoSelected = m_chooser.getSelected();
        // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
        System.out.println("Auto selected: " + m_autoSelected);
        distance = 100;
        leftEncoder.reset();
        rightEncoder.reset();
        leftError = distance;
        rightError = distance;
    }

    public void customAutoPeriodic() {
        return;
    }

    public void roboDrive() {
        leftDist = leftEncoder.getDistance();
        rightDist = rightEncoder.getDistance();

        if (leftError > 5) {
            leftError = distance - leftDist;
            double leftPow = pidLeft.Compute(leftError);
            System.out.println("LeftEr: " + leftDist);
            System.out.println("Left: " + leftPow);
        }

        if (rightError > 5) {
            rightError = distance - rightDist;
            double rightPow = pidRight.Compute(rightError);
            System.out.println("RighttEr: " + rightDist);
            System.out.println("Right: " + rightPow);
        }
    }
}
