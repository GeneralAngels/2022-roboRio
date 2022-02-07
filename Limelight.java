// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
//import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

//import edu.wpi.first.cscore.HttpCamera;


public class Limelight {

    private static final NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight"); // Network Table used to contact LL, can be used to get values from the LL
    
    public static final boolean POST_TO_SMART_DASHBOARD = true; // Toggle for posting to SmartDashboard

    // Uses network tables to check status of limelight
    private static NetworkTableEntry timingTestEntry = table.getEntry("TIMING_TEST_ENTRY");
    private static boolean timingTestEntryValue = false; // currently not conected to LL
    public static final long MAX_UPDATE_TIME = 200_000; // Micro Seconds = 0.2 Seconds


    public static final double IMAGE_CAPTURE_LATENCY = 11; // not accurate!!! (I think...), least 11ms for image capture latency.
    private static NetworkTableEntry latencyEntry = table.getEntry("tl"); // The pipeline’s latency contribution (ms)

    /* Commonly Used Contour Information */

    // Whether the limelight has any valid targets (0 or 1)
    private static NetworkTableEntry validTargetEntry = table.getEntry("tv");

    // Horizontal Offset From Crosshair To Target(-29.8 to 29.8 degrees)
    public static final double MIN_X_ANGLE = -29.8;
    public static final double MAX_X_ANGLE = 29.8;
    private static NetworkTableEntry xAngleEntry = table.getEntry("tx");

    // Vertical Offset From Crosshair To Target (-24.85 to 24.85 degrees)
    public static final double MIN_Y_ANGLE = -24.85;
    public static final double MAX_Y_ANGLE = 24.85;
    private static NetworkTableEntry yAngleEntry = table.getEntry("ty");

    // Targets area from (0% of the image to 100% of the image
    // private static NetworkTableEntry targetAreaEntry = table.getEntry("ta");


    /* gets and sets */
    
    public static boolean hasValidTarget(){
      return table.getEntry("tv").getDouble(0) == 1;
    }

    public double getXAngle(){
      return table.getEntry("tx").getDouble(0);
    }

    public double getYAngle(){
      return table.getEntry("ty").getDouble(0);
    }

    public double getAreaPercent(){
      return table.getEntry("ta").getDouble(0);
    }

    // mode sets what pipeline is used
    public void setPipeline(int mode) {
      table.getEntry("pipeline").setNumber(mode);
    }
    // a value setting the led mode of the limelight
    public static void setLEDMode(int mode) {
      table.getEntry("ledMode").setNumber(mode);
      /*
      0	use the LED Mode set in the current pipeline
      1	force off
      2	force blink
      3	force on
      */
    }
    // a value between 0 and 1, 0 being on, 1 being off 
    public static void setCamMode(int mode) { 
      table.getEntry("camMode").setNumber(mode);
    }

    // checks if LL is connected
    public static boolean isConnected() { 
        // Force an update and get current time
        timingTestEntryValue = !timingTestEntryValue; // flip test value
        timingTestEntry.forceSetBoolean(timingTestEntryValue);
        long currentTime = timingTestEntry.getLastChange();

        // Get most recent update from limelight
        long lastUpdate = latencyEntry.getLastChange();
        lastUpdate = Math.max(lastUpdate, validTargetEntry.getLastChange());
        lastUpdate = Math.max(lastUpdate, xAngleEntry.getLastChange());
        lastUpdate = Math.max(lastUpdate, yAngleEntry.getLastChange());

        // Calculate limelights last update
        long timeDifference = currentTime - lastUpdate;
        boolean connected = timeDifference < MAX_UPDATE_TIME;

        if (POST_TO_SMART_DASHBOARD) {
            SmartDashboard.putBoolean("Limelight Connected:", connected);
            SmartDashboard.putNumber("Limelight Time Difference:", timeDifference);
        }

        return connected;
    }

  /*public void Vision()
  {
    ShuffleboardTab mainTab = Shuffleboard.getTab("Dashboard");

    HttpCamera LLFeed = new HttpCamera("limelight", "http://10.22.30.26:5800");

    mainTab.add("LL", LLFeed).withPosition(0, 0).withSize(15, 8);
  } */
}
