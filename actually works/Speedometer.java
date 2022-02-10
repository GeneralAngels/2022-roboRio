// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.time.Duration;
import java.time.Instant;

/** Add your docs here. */
public class Speedometer {
    // private double last_time;
    private Instant last_time;
    private double last_dist;
    private double curr_dist;
    private double curr_speed;

    private String name;

    public Speedometer(String name){
        // last_time = System.currentTimeMillis()  % 1000000;
        last_time = Instant.now();
        last_dist = 0;
        curr_dist = 0;
        curr_speed = 0;

        this.name = name;
    }

    /**
     * calculates the current speed of the object in m/s
     * @param curr_dist the current distance of the object
     */
    public void calc_speed(double curr_dist){
        // System.out.println("time:  " + getTime());
        // System.out.println("last dist:  " + last_dist);
        // System.out.println("curr dist:  " + curr_dist);
        last_dist = this.curr_dist;
        this.curr_dist = curr_dist;

        System.out.println(name + ":");
        double dist_diff = curr_dist - last_dist;
        // double time_diff = getTimeDifference();
        double time_diff = newUpdateTime();
        curr_speed = dist_diff / time_diff;

        System.out.println("differences:");
        // System.out.println("dist diff:  " + dist_diff);
        // System.out.println("time diff:  " + time_diff);
        System.out.println("curr speed:  " + curr_speed);
    }

    /**
     * @return returns% the speed of the object
     */
    public double getSpeed() {
        return this.curr_speed; 
    }

    /**
     * @return the time passed between last update
     */
    public double getTimeDifference(){
        // double timeDiff = (double)((System.currentTimeMillis() % 1000000 - last_time) / 100.0);
        Instant curr_time = Instant.now();
        Duration timeDiff = Duration.between(last_time, curr_time);
        
        System.out.println("times:");
        System.out.println(curr_time);
        System.out.println(last_time);
        System.out.println("timeDiff:  " + timeDiff.toNanos()/(Math.pow(10.0, 9)));

        // if (timeDiff == 0.0)
        //     return 0.1;
        // return timeDiff;
        return (double)timeDiff.toNanos()/(Math.pow(10.0, 9));
    }

    /**
     * updates the time on the speedometer
     */
    public void updateTime(){
        // if(getTime() !=)
        // last_time = System.currentTimeMillis() % 1000000;
        // System.out.println("time before:  " + last_time);
        last_time = Instant.now();
        // System.out.println("time now:  " + Duration.between(last_time));
    }

    public double newUpdateTime(){
        Instant curr_time = Instant.now();
        Duration timeDiff = Duration.between(last_time, curr_time);
        last_time = curr_time;
        
        // System.out.println("times:");
        // System.out.println(curr_time);
        // System.out.println(last_time);
        // System.out.println("timeDiff:  " + timeDiff.toNanos()/(Math.pow(10.0, 9)));

        // if (timeDiff == 0.0)
        //     return 0.1;
        // return timeDiff;
        return (double)timeDiff.toNanos()/(Math.pow(10.0, 9));

    }
    
}
