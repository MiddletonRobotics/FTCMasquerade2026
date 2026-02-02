package org.firstinspires.ftc.teamcode.constants;

import com.acmerobotics.dashboard.config.Config;

@Config
public class TurretConstants {
    public static final String turretMotorID = "turretMotor";

    public static double kTurretRatio = 1;

    //PIDF Constants - Tunable via FTC Dashboard
    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0;

    public static double kS = 0.0;
    public static double kV = 0.0;
    public static double kA = 0.0;
}
