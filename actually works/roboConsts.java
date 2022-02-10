// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/** Add your docs here. */
public class roboConsts { // 8/62
    public final static double RATIO_BETWEEN_WHEELS_AND_MOTOR = 62/8;
    public final static double PULSES_PER_REVOLUTION = 2048;
    public final static double PULSES_PER_WHEEL_ROTATION = PULSES_PER_REVOLUTION * RATIO_BETWEEN_WHEELS_AND_MOTOR;
    
    public final static double WHEEL_DIAMETER = 0.1524;
    public final static double ENCODER_TO_RADIAN = 2 * (Math.PI) / PULSES_PER_WHEEL_ROTATION;
    public final static double ENCODER_TO_M = ENCODER_TO_RADIAN * WHEEL_DIAMETER;
    public final static double WHEEL_SCOPE = (WHEEL_DIAMETER) * Math.PI;
    public final static double METER_DRIVE = (1 / WHEEL_SCOPE) * PULSES_PER_WHEEL_ROTATION;

    public final static double MAX_POWER = 0.3;
    public final static double MAX_TURN_POWER = 0.15;
}
