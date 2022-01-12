package frc.robot;

public class PID {
    private long lastTime = 0;
    private double errSum, lastErr;
    private double kp, ki, kd;

    public PID(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public double Compute(double error) {
        double dErr = 0;
        /* How long since we last calculated */
        long now = System.currentTimeMillis() % 3000000;
        if (this.lastTime != 0) {
            double timeChange = (double) (now - this.lastTime);

            /* Compute all the working error variables */
            this.errSum += ((error + this.lastErr) * timeChange) / 2;
            dErr = (error - lastErr) / timeChange;
        }
        /* Compute PID Output */
        double Output = this.kp * error + this.ki * this.errSum + this.kd * dErr;
        Output = Math.max(-1, Math.min(Output, 1));// insuring the the range of the output
        /* Remember some variables for next time */
        this.lastErr = error;
        this.lastTime = now;
        return Output;
    }

}