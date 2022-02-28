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
    private TalonFX firstMotor;
    private double distanceToTravel;
    private double MaxPower = 0.2;
    private double error;
    private PID sPID;
    private double currPow = 0;

    private String groupName;

    private double encoder_to_meter;

    public MotorGroup(int backPort, int frontPort, double wheel_diameter, String name){
        this.backMotor = new TalonFX(backPort);
        this.firstMotor = new TalonFX(frontPort);
        this.encoder_to_meter = roboConsts.ENCODER_TO_RADIAN * wheel_diameter;
        groupName = name;
    }

    /**
     * sets the distance pid of this group
     * @param kp kp of pid
     * @param ki ki of pid
     * @param kd kd of pid
     */
    public void setDistancePID(double kp, double ki, double kd, double max_in){
        distancePID = new PID(kp, ki, kd, max_in);
    }

    /**
     * sets the speed pid of this group
     * @param kp kp of pid
     * @param ki ki of pid
     * @param kd kd of pid
     */
    public void setSPID(double kp, double ki, double kd, double maxOut){
        sPID = new PID(kp, ki, kd, maxOut);
    }

    /**
     * sets the power sent to both motors of the group
     * @param power power to send
     */
    public void setPower(double power){
        this.firstMotor.set(ControlMode.PercentOutput, power);
        this.backMotor.set(ControlMode.PercentOutput, power);
    }

    public double getBackEncoderValue(){
        return this.backMotor.getSelectedSensorPosition();
    }

    public double getFrontEncoderValue(){
        return this.firstMotor.getSelectedSensorPosition();
    }

    public double getEncoderValue(){
        return (getFrontEncoderValue() + getBackEncoderValue()) /2;
    }

    public void setInverted(boolean inverted){
        this.firstMotor.setInverted(inverted);
        this.backMotor.setInverted(inverted);
    }

    public void setInverted(boolean firstInvert, boolean secondInvert){
        this.firstMotor.setInverted(firstInvert);
        this.backMotor.setInverted(secondInvert);
    }

    public void resetEncoders(){
        this.firstMotor.setSelectedSensorPosition(0);
        this.backMotor.setSelectedSensorPosition(0);
    }

    /**
     * @return the current distance travelled in meters
     */
    public double getDistance(){
        // return GetEncoderValue() / roboConsts.METER_DRIVE;
        return getEncoderValue() * this.encoder_to_meter;
    }

    /**
     * sets the distance to travel
     * @param distance the distance to travel
     */
    public void setTravelDistance(double distance){
        distanceToTravel = distance;
        error = distance;
    }

    /**
     *the distance to travel
     * @return the distance to travel
     */
    public double getTravelDistance(){
        return this.distanceToTravel;
    }

    /**
     *  drive distance with PID
     * @return input power
     */
    public double driveByDistance(){
        error = getDistanceError();
        // new Print(GetEncoderValue());
        new Print(groupName);
        new Print("distance:  "+ getDistance());
        double power = distancePID.Compute(error);
        new Print("power:  " + power);
        setPower(power);
        return power;
    }

    /***
     * gets each motors velocity in pulses/100msec, multiplies by 10 to get pulses/sec
     * and doens an average
     * @return the average speed of the two motors in pulses per second
     */
    public double getSpeedPS(){
        return (firstMotor.getSelectedSensorVelocity() + backMotor.getSelectedSensorVelocity()) / 2 * 10;
    }

    public double getSpeedMS(){
        return getSpeedPS() * this.encoder_to_meter;
    }

    public double getDistanceError(){
        return distanceToTravel - getDistance();
    }

    public void  resetDistancePID(){
        distancePID.Reset();
    }

    public void resetSPID(){
        sPID.Reset();
        currPow = 0;
    }

    public double driveBySpeed(double speed){
        double velocity_average = getSpeedMS();
        double speedErr = speed - velocity_average;
        double toAdd = sPID.Compute(speedErr) / 10; 
        currPow += toAdd;
        // currPow = Math.max(-roboConsts.MAX_POWER, Math.min(currPow, roboConsts.MAX_POWER));
        if (currPow < 0) currPow = Math.max(-roboConsts.MAX_POWER, currPow);
        else currPow = Math.min(roboConsts.MAX_POWER, currPow);
        setPower(currPow);  
        return velocity_average;
    }

    public void motorPeriodic(){
        ;
    }

    public void testEncoders(){
        System.out.println(groupName);
        System.out.println(getEncoderValue() + " - " + getDistance());
        System.out.println("-");
    }

    public void testVelocity(){
        System.out.println(groupName);
        System.out.println("front: " + firstMotor.getSelectedSensorVelocity() * 10 * this.encoder_to_meter);
        System.out.println("back: " + backMotor.getSelectedSensorVelocity() * 10 * this.encoder_to_meter);
        System.out.println("avg: " + getSpeedMS());
    }


}
