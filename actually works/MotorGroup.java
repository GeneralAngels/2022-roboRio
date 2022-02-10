package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.fasterxml.jackson.core.sym.Name;

import java.sql.Time;
import java.text.BreakIterator;

import javax.xml.namespace.QName;

import com.ctre.phoenix.motorcontrol.ControlMode;

import java.time.Duration;
import java.time.Instant;

public class MotorGroup {
    private PID pid;
    private TalonFX backMotor;
    private TalonFX frontMotor;
    private double TravelDistance;
    private double MaxPower = 0.2;
    private double error;
    private PID sPID;
    private double currPow = 0;

    private String groupName;

    public MotorGroup(int backPort, int frontPort, String name){
        this.backMotor = new TalonFX(backPort);
        this.frontMotor = new TalonFX(frontPort);
        groupName = name;
    }

    /**
     * sets the distance pid of this group
     * @param kp kp of pid
     * @param ki ki of pid
     * @param kd kd of pid
     */
    public void SetPID(double kp, double ki, double kd){
        pid = new PID(kp, ki, kd, roboConsts.MAX_POWER);
    }

    /**
     * sets the speed pid of this group
     * @param kp kp of pid
     * @param ki ki of pid
     * @param kd kd of pid
     */
    public void SetSPID(double kp, double ki, double kd){
        sPID = new PID(kp, ki, kd, roboConsts.MAX_POWER);
    }

    /**
     * sets the power sent to both motors of the group
     * @param power power to send
     */
    public void SetPower(double power){
        this.frontMotor.set(ControlMode.PercentOutput, power);
        this.backMotor.set(ControlMode.PercentOutput, power);
    }

    public double GetBackEncoderValue(){
        return this.backMotor.getSelectedSensorPosition();
    }

    public double GetFrontEncoderValue(){
        return this.frontMotor.getSelectedSensorPosition();
    }

    public double GetEncoderValue(){
        return (GetFrontEncoderValue() + GetBackEncoderValue()) /2;
    }

    public void SetInverted(boolean inverted){
        this.frontMotor.setInverted(inverted);
        this.backMotor.setInverted(inverted);
    }

    public void resetEncoders(){
        this.frontMotor.setSelectedSensorPosition(0);
        this.backMotor.setSelectedSensorPosition(0);
    }

    /**
     * @return the current distance travelled in meters
     */
    public double getDistance(){
        return GetEncoderValue() * roboConsts.ENCODER_TO_M;
    }

    /**
     * sets the distance to travel
     * @param distance the distance to travel
     */
    public void SetTravelDistance(double distance){
        this.resetEncoders();
        this.TravelDistance = distance;
        this.error = distance;
    }

    /**
     *the distance to travel
     * @return the distance to travel
     */
    public double GetTravelDistance(){
        return this.TravelDistance;
    }

    /**
     *  drive distance with PID
     * @return input power
     */
    public double DriveByDistance(){
        this.error = this.GetDistamceError();
        double power = this.pid.Compute(error);
        this.SetPower(power * this.MaxPower);
        return power;
    }

    public double GetSpeedPS(){
        return (frontMotor.getSelectedSensorVelocity() + backMotor.getSelectedSensorVelocity()) / 2;
    }

    public double GetSpeedMS(){
        return GetSpeedPS() * roboConsts.ENCODER_TO_M;
    }

    public double GetDistamceError(){
        return this.TravelDistance - this.getDistance();
    }

    public void ResetPID(){
        this.pid.Reset();
    }

    public void driveBySpeed(double speed){
        double velocity_average = GetSpeedMS();
        double speedErr = speed - velocity_average;

        currPow += sPID.Compute(speedErr);

        if (currPow <= roboConsts.MAX_POWER && currPow >= -roboConsts.MAX_POWER)
            SetPower(currPow);        
        System.out.println("currPow:  " + currPow);

        // if (!(currPow > roboConsts.MAX_POWER || currPow < -roboConsts.MAX_POWER))    
        //     if (velocity_average > speed)
        //         currPow -= 0.001;
        //     else if (velocity_average < speed)
        //         currPow += 0.001;
        //     SetPower(currPow);
    }

    public void motorPeriodic(){
        ;
    }

    public void testEncoders(){
        System.out.println(groupName);
        System.out.println(GetEncoderValue());
        System.out.println("-");
    }

    public void testVelocity(){
        // System.out.println("pls work");
        // SetPower(1);
        // long startTime = Instant.now().toEpochMilli();
        // while (true){
        //     System.out.println(Instant.now().toEpochMilli() - startTime);
        //     System.out.println(GetEncoderValue());
        // }
        ;
        // System.out.println(groupName + ":");
        // System.out.println(frontMotor.getSelectedSensorVelocity() * roboConsts.ENCODER_TO_M);
        // System.out.println(backMotor.getSelectedSensorVelocity() * roboConsts.ENCODER_TO_M);
        // System.out.println(getDistance());
    }


}
