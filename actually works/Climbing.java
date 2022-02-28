// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;


/** Add your docs here. */
public class Climbing {
    private TalonFX arms;
    private int pulses;

    public Climbing(int armsPort){
        arms = new TalonFX(armsPort);

        pulses = 0;
    }

    public void setPower(double power){
        arms.set(TalonFXControlMode.PercentOutput, power);
        System.out.println(arms.getSelectedSensorPosition());
    }

    public void extend(){
        
    }

    public void resetEncoder(){
        arms.setSelectedSensorPosition(0);
    }
}
