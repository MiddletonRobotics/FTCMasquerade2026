//package org.firstinspires.ftc.teamcode.OpModes.TuningOP;
//
//import com.acmerobotics.dashboard.FtcDashboard;
//import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.teamcode.subsystems.Shooter;
//
//@TeleOp(name = "Shooter Test – Limelight + RPM", group = "Test")
//public class TuningShooter extends OpMode {
//
//    private Shooter shooter;
//    private FtcDashboard dashboard;
//
//    @Override
//    public void init() {
//        dashboard = FtcDashboard.getInstance();
//
//        // Send telemetry to both Driver Station AND Dashboard
//        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
//
//        shooter = new Shooter(hardwareMap, telemetry);
//
//        telemetry.addLine("Shooter Test Initialized");
//    }
//
//    @Override
//    public void start() {}
//
//    @Override
//    public void loop() {
//
//        // === SHOOTER CONTROLS ===
//        if (gamepad1.a) shooter.enableFlywheel();
//        if (gamepad1.b) shooter.disableFlywheel();
//        if (gamepad1.x) shooter.autoTuneRPM();
//        if (gamepad1.y) shooter.setTargetVelocity(3000);
//
//        // Hood controls
//        if (gamepad1.dpad_up)    shooter.setHoodMax();
//        if (gamepad1.dpad_down)  shooter.setHoodInit();
//        if (gamepad1.dpad_left)  shooter.setHoodMid();
//
//        // === PIPELINE SELECTION ===
//        if (gamepad1.dpad_right) shooter.setPipeline(20);   // Pipeline 0
//        if (gamepad1.dpad_left) shooter.setPipeline(24); // Pipeline 1// Pipeline 2
//
//        telemetry.addLine("\n=== Shooter Test ===");
//
//        // Limelight distance
//        double dist = shooter.getDistanceMeters();
//        telemetry.addData("Distance (m)", dist);
//
//        // Shooter telemetry
//        telemetry.addData("Current Velocity", shooterMotorVelocity());
//        telemetry.addData("Target Velocity", shooterTargetVelocity());
//
//        telemetry.addLine("--- Controls ---");
//        telemetry.addLine("A = Enable Flywheel");
//        telemetry.addLine("B = Disable Flywheel");
//        telemetry.addLine("X = Auto RPM (distance)");
//        telemetry.addLine("Y = 3000 RPM");
//        telemetry.addLine("DPAD Right = Pipeline 0");
//        telemetry.addLine("RB = Pipeline 1");
//        telemetry.addLine("LB = Pipeline 2");
//
//        telemetry.update();
//    }
//
//    // Helper methods
//    private double shooterMotorVelocity() {
//        return shooter != null ? shooter.getVelocity() : 0;
//    }
//
//    private double shooterTargetVelocity() {
//        return shooter != null ? shooter.getInterpolatedRPM(shooter.getDistanceMeters()) : 0;
//    }
//}