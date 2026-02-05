package org.firstinspires.ftc.teamcode.constants;

import java.util.TreeMap;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.configurables.annotations.Configurable;


@Config
@Configurable
public class ShooterConstants {
    // IDs
    public static final String hoodServoID = "hoodServo";
    public static final String shooterMotorID = "shooterMotor";

    public static double kp = 0.01;
    public static double ki = 0;
    public static double kd = 0.000;
    public static double kf = 0.0003;

    public static final double hoodInit = 0.00;
    public static final double hoodMid  = 0.25;
    public static final double hoodMax  = 0.55;

    public static double closeShot = 1125;
    public static double midShot = 1375; //1725 //1350
    public static double farShot = 1725; //Far
}
