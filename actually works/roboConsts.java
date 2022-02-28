// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class roboConsts { // 8/62
    public final static double EndAngle = -45; // degrees
    public final static double TowerHeight = 2.64;
    public final static double WheelHeight =  1.1; // 1.061;
    public final static double HieghtDifference = TowerHeight - WheelHeight;

    public final static double HoodHalfLength = 0.261;
    public final static double GloryHoleRadius = 0.62;

    public final static double HoodMinAngle = 22;
    public final static double HoodMaxAnglev =  57;

    public final static double MinPotentiometer = 0.3;
    public final static double MaxPotentiometer = 0.4; //need to check

    public final static double RATIO_BETWEEN_WHEELS_AND_MOTOR = 62/8.0;
    public final static double PULSES_PER_REVOLUTION = 2048;
    public final static double PULSES_PER_WHEEL_ROTATION = PULSES_PER_REVOLUTION * RATIO_BETWEEN_WHEELS_AND_MOTOR;
    
    public final static double WHEEL_DIAMETER = 0.1524;
    public final static double ENCODER_TO_RADIAN = (Math.PI) / PULSES_PER_WHEEL_ROTATION;
    public final static double ENCODER_TO_M = ENCODER_TO_RADIAN * WHEEL_DIAMETER; // 0.0004
    public final static double WHEEL_SCOPE = (WHEEL_DIAMETER) * Math.PI; // 0.00006 // ~166666 for meter
    public final static double METER_DRIVE = (1 / WHEEL_SCOPE) * PULSES_PER_WHEEL_ROTATION;

    public final static double MAX_POWER = 0.5;
    public final static double MAX_TURN_POWER = 0.15;
    public final static double MAX_SPEED = 1; // 1.4

    public final static double HUB_HEIGHT = 2.64;
    public final static double CAM_ANGLE = 63.36;
    public final static double CAM_HEIGHT = 1; // very close approximation

}
