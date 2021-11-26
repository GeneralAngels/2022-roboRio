
package frc.robot;

import com.ctre.phoenix.sensors.PigeonIMU;

public abstract class Gyroscope {
    private static PigeonIMU pigeon = new PigeonIMU(30);

    public static double getAngle() {
        double[] ypr = new double[3];
        pigeon.getYawPitchRoll(ypr);
        return ypr[0];
    }

    public static void setAngle(double angle) {
        pigeon.setYaw(angle);
    }

    public static void reset() {
        pigeon.setYaw(0);
        pigeon.setFusedHeading(0);

    }

}
