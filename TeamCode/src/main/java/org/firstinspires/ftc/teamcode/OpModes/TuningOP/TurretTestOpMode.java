//package org.firstinspires.ftc.teamcode.OpModes.TuningOP;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.seattlesolvers.solverslib.gamepad.GamepadEx;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.subsystems.Turret;
//
//import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
//
///**
// * OpMode to test Turret PID controller with Limelight AprilTag tracking
// *
// * Usage:
// * 1. Run this OpMode on your robot
// * 2. Connect to FTC Dashboard at http://192.168.43.1:8080/dash
// * 3. Tune PID values (kp, ki, kd, kf) in real-time via the dashboard
// * 4. The turret will automatically track AprilTags detected by Limelight
// */
//
//@TeleOp(group = "Tuning", name = "Turret PID Test")
//public class TurretTestOpMode extends OpMode {
//
//    private Turret turret;
//    private Telemetry dashboardTelemetry;
//    private Drivetrain dt;
//    private GamepadEx Driver1;
//
//    @Override
//    public void init() {
//        // Initialize FTC Dashboard
//        FtcDashboard dashboard = FtcDashboard.getInstance();
//        dashboardTelemetry = dashboard.getTelemetry();
//
//        // Initialize Turret subsystem (includes Limelight initialization)
//        turret = new Turret(hardwareMap, telemetry);
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.addData("Instructions", "Connect to FTC Dashboard to tune PID values");
//        telemetry.update();
//
//        dt = new Drivetrain(hardwareMap, telemetry);
//        Driver1 = new GamepadEx(gamepad1);
//    }
//
//    @Override
//    public void start() {
//        // Start Limelight polling
//        dt.follower.startTeleopDrive();
//        dt.follower.setMaxPower(.8);
//
//        telemetry.addData("Status", "Started");
//        telemetry.addData("Turret", "Tracking AprilTags");
//        telemetry.update();
//    }
//
//    @Override
//    public void loop() {
//        // Update turret (reads Limelight and applies PID control)
//        turret.periodic();
//
//        dt.setMovementVectors(Driver1.getLeftX(),Driver1.getLeftY(), Driver1.getRightX(),false);
//        dt.periodic();
//
//        // Display telemetry
//        telemetry.addData("Turret Encoder", turret.getCurrentPosition());
//        telemetry.addData("---", "---");
//        telemetry.addData("FTC Dashboard", "http://192.168.43.1:8080/dash");
//        telemetry.addData("Tune PID", "Adjust kp, ki, kd, kf in TurretConstants");
//        telemetry.update();
//
//        // Also send to dashboard
//        dashboardTelemetry.addData("Turret Angle", String.format("%.2f", turret.getCurrentPosition()));
//        dashboardTelemetry.update();
//    }
//
//    @Override
//    public void stop() {
//        // Stop Limelight polling
//
//        telemetry.addData("Status", "Stopped");
//        telemetry.update();
//    }
//}
//
