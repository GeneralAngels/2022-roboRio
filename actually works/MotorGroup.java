package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.fasterxml.jackson.core.sym.Name;

import java.sql.Time;
import java.text.BreakIterator;

import javax.xml.namespace.QName;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class MotorGroup {
    private PID distancePID;
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
    public void SetDistancePID(double kp, double ki, double kd){
        distancePID = new PID(kp, ki, kd, roboConsts.MAX_POWER);
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
        return GetEncoderValue() / roboConsts.METER_DRIVE;
    }

    /**
     * sets the distance to travel
     * @param distance the distance to travel
     */
    public void SetTravelDistance(double distance){
        resetEncoders();
        TravelDistance = distance;
        error = distance;
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
        error = GetDistanceError();
        double power = distancePID.Compute(error);
        SetPower(power);
        return power;
    }

    public double GetSpeedPS(){
        return (frontMotor.getSelectedSensorVelocity() + backMotor.getSelectedSensorVelocity()) / 2;
    }

    public double GetSpeedMS(){
        return GetSpeedPS() * roboConsts.ENCODER_TO_M;
    }

    public double GetDistanceError(){
        return TravelDistance - getDistance();
    }

    public void ResetDistancePID(){
        distancePID.Reset();
    }

    public void ResetSPID(){
        sPID.Reset();
        currPow = 0;
    }

    public void driveBySpeed(double speed){
        double velocity_average = GetSpeedMS();
        double speedErr = speed - velocity_average;
        System.out.println("speed error" + this.groupName + "   :" + speedErr);
        currPow += sPID.Compute(speedErr);
        currPow = Math.max(-roboConsts.MAX_POWER, Math.min(currPow, roboConsts.MAX_POWER));
        SetPower(currPow);        
        System.out.println("currPow " + this.groupName + "  :"+ currPow);
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
        ;
    }


}
