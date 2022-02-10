package frc.robot;

import com.ctre.phoenix.sensors.PigeonIMU;

public class Gyroscope {
    private static PigeonIMU pidgey;

    public Gyroscope(int port){
        pidgey = new PigeonIMU(port);
    }

    public double getAngle() {
        double[] ypr = new double[3];
        pidgey.getYawPitchRoll(ypr);
        return ypr[0];
    }

    public void setAngle(double angle) {
        pidgey.setYaw(angle);
    }

    public void reset() {
        pidgey.setYaw(0);
        pidgey.setFusedHeading(0);

    }

    public boolean inRange(double minVal, double maxVal){
        return minVal <= getAngle() && getAngle() <= maxVal;
    }

}