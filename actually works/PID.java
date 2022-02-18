package frc.robot;

public class PID {
    private double lastTime = 0;
    private double startTime = System.currentTimeMillis()  % 1000000;
    
    private double maxOutput;

    private double errSum, lastErr;
    private double kp, ki, kd;
    private double MAXIMUM_INTEGRAL = 0.7;

    public PID(double kp, double ki, double kd, double maxOutput) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.maxOutput = maxOutput;
    }

    public double Compute(double error) {
        double dErr = 0;
        /* How long since we last calculated */
        double now = System.currentTimeMillis() % 3000000 / 1000.0;
        if (this.lastTime != 0) {
            double timeChange = (double) (now - this.lastTime);
            /* Compute all the working error variables */
            
            this.errSum += ((error + this.lastErr) * timeChange) / 2;
            this.errSum = Math.max(-MAXIMUM_INTEGRAL,Math.min(this.errSum, MAXIMUM_INTEGRAL));
            dErr = (error - lastErr) * 1000 / timeChange / 1000;
            // System.out.println("D   :" + dErr);
            // System.out.println("Error:    " + error);
        }
        /* Compute PID Output */
        double Output = this.kp * error + this.ki * this.errSum + this.kd * dErr;
        Output = Math.max(-1, Math.min(Output, 1));// insuring the range of the output
        /* Remember some variables for next time */
        this.lastErr = error;
        this.lastTime = now;

        if (Output < 0) return Math.max(Output, -maxOutput); 
        return Math.min(Output, maxOutput);
    }

    public double getTime(){

        return (double)((System.currentTimeMillis() % 1000000 - startTime) / 1000.0);
    }

    public void Reset(){
        this.lastTime = 0;
        this.errSum = 0;
        this.lastErr = 0;
        
    }

}
