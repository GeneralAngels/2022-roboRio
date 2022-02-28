// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;

/** Add your docs here. */
public class RoboIntake {

    private final CANSparkMax liftMotor;

    private final CANSparkMax lowerIntakeMotor;

    private final CANSparkMax upperIntakeMotor;

    // private final DigitalInput openLimitSwitch = new DigitalInput(99);
    // private final DigitalInput closeLimitSwitch = new DigitalInput(98);



    // private final double StuckCurrent = 8;
    private final double liftSpeed = 0.1;
    private final double intakeSpeed = 0.35;

    private boolean isTaking;

    public RoboIntake(int liftPort, int lowerIntakePort, int upperIntakePort){
        liftMotor = new CANSparkMax(liftPort, MotorType.kBrushless);
        lowerIntakeMotor = new CANSparkMax(lowerIntakePort, MotorType.kBrushless);
        upperIntakeMotor = new CANSparkMax(upperIntakePort, MotorType.kBrushless);
    }

    public boolean isOpened(){
        // return openLimitSwitch.get();
        return true;
    }

    public boolean isClosed(){
        // return closeLimitSwitch.get();
        return true;
    }

    public void close(){
        if (!isClosed()) liftMotor.set(-liftSpeed);
    }

    public void open(){
        if (!isClosed()) liftMotor.set(liftSpeed);
    }

    public void take(){
        lowerIntakeMotor.set(-intakeSpeed);
        // upperIntakeMotor.set(-intakeSpeed);
    }

    public void stopTaking(){
        lowerIntakeMotor.set(0);
        // upperIntakeMotor.set(0);
    }

    public boolean isTaking(){
        return isTaking;
    }

    public void disableMotors(){
        liftMotor.set(0);
        lowerIntakeMotor.set(0);
    }
}
