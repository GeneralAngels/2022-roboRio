package frc.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Shooting {
    private PID hoodPid;
    private AnalogPotentiometer pot = new AnalogPotentiometer(0);
    private MotorGroup ShooterGroup;
    private CANSparkMax HoodMotor;
    private Vector shootingVector;
    private double shooting_wheel_diameter = 0.1016;
    private double potentiometerDelta = roboConsts.MaxPotentiometer - roboConsts.MinPotentiometer;
    private double angleDelta = roboConsts.HoodMaxAnglev - roboConsts.HoodMinAngle;
    private double hoodCoefficient = angleDelta / potentiometerDelta;

    public Shooting(int hoodPort, int shootPort1, int shootPort2){
        HoodMotor = new CANSparkMax(hoodPort, MotorType.kBrushless);
        ShooterGroup = new MotorGroup(shootPort1, shootPort2, shooting_wheel_diameter, "Shooters");
        ShooterGroup.setInverted(true, false);
        ShooterGroup.setSPID(0.2, 0.07, 0.07, 1);
        hoodPid = new PID(0.1, 0.07, 0.07, 0.5);
    }

    public double currentHoodAngle(){
        return roboConsts.HoodMaxAnglev - hoodCoefficient * (pot.get() - roboConsts.MaxPotentiometer);
    }

    public void setHoodAngle(){
        double angleError = shootingVector.getAngle() - currentHoodAngle();
        HoodMotor.set(hoodPid.Compute(angleError));
    }

    public void setShootingSpeed(){
        ShooterGroup.driveBySpeed(shootingVector.getVelocity());
    }

    public void setPower(double power){
        ShooterGroup.setPower(power);
    }

    public void disableHoodMotor(){
        HoodMotor.set(0);
    }

    public void disable(){
        setPower(0);
        disableHoodMotor();
    }

    public void setVector(Vector vector){
        this.shootingVector = vector;
    }

    public boolean ready(){
        return shootingVector.finishedAngle(currentHoodAngle(), 1) && 
        shootingVector.finishedSpeed(ShooterGroup.getSpeedMS(), 0.5);
    }
}
