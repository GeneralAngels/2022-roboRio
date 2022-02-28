package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Feeder {
    private CANSparkMax lowerMotor;
    private CANSparkMax neckMotor;

    private boolean isFeeding;

    private double feedTime;

    // change accordingly
    private double feedSpeed = 0.2;

    public Feeder(int neckPort){
        // lowerMotor = new CANSparkMax(lowerPort, MotorType.kBrushless);
        neckMotor = new CANSparkMax(neckPort, MotorType.kBrushless);

        isFeeding = false;
    }

    public void feed(){
        if(! isFeeding)
        {
            
        }
        // lowerMotor.set(feedSpeed);
        neckMotor.set(feedSpeed);

        isFeeding = true;
    }

    public void stopFeeding(){
        // lowerMotor.set(0);
        neckMotor.set(0);

        isFeeding = false;
    }

    public boolean isFeeding(){
        return isFeeding;
    }
}
