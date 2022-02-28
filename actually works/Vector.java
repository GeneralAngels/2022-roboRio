package frc.robot;

public class Vector {
    private double angle;
    private double velocity;


    public Vector(double angle, double velocity){
        this.angle = angle;
        this.velocity = velocity;
    }

    public double getVelocity(){
        return this.velocity;
    }

    public double getAngle(){
        return this.angle;
    }

    public boolean finishedAngle(double value, double rangeOfError){
        return value < this.angle + rangeOfError &&
         value > this.angle - rangeOfError;
    }
    public boolean finishedSpeed(double value, double rangeOfError){
        return value < this.velocity + rangeOfError &&
         value > this.velocity - rangeOfError;
    }
}
