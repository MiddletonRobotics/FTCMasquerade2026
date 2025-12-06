package org.firstinspires.ftc.teamcode.constants;

import java.util.TreeMap;
import com.acmerobotics.dashboard.config.Config;

@Config
public class ShooterConstants {

    // IDs
    public static final String hoodServoID = "hoodServo";
    public static final String shooterMotorID = "shooterMotor";

    public static  double kp = 0.003;
    public static  double ki = 0.0;
    public static  double kd = 0.00005;
    public static  double kf = 0.00055;

    public static final double hoodInit = 0.00;
    public static final double hoodMid  = 0.25;
    public static final double hoodMax  = 0.55;

    public static double closeShot = 1400;
    public static double midShot = 1750;
    public static double farShot = 1950;
}
