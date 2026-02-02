package org.firstinspires.ftc.teamcode.OpModes.TuningOP;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.TurretConstants;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

/**
 * OpMode specifically for PID tuning the turret's AprilTag tracking
 *
 * Features:
 * - Real-time PID tuning via FTC Dashboard
 * - Displays Limelight data and turret status
 * - Automatically tracks AprilTags when detected
 * - Shows PID error and output values
 */
@TeleOp(group = "Tuning", name = "Turret PID Tuning")
public class TurretPIDTuningOpMode extends OpMode {

    private Turret turret;
    private Drivetrain drivetrain;
    private FtcDashboard dashboard;

    @Override
    public void init() {
        // Initialize FTC Dashboard for PID tuning
        dashboard = FtcDashboard.getInstance();

        // Attach dashboard telemetry to normal telemetry
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        // Initialize turret
        turret = new Turret(hardwareMap, telemetry);

        // Initialize drivetrain
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        drivetrain.follower.startTeleopDrive();
        drivetrain.follower.setMaxPower(0.8);

        telemetry.addLine("Initialized — Ready to Start");
        telemetry.update();
    }

    @Override
    public void start() {
        telemetry.addData("Status", "Started - Turret tracking enabled");
        telemetry.update();
    }

    @Override
    public void loop() {

        // ---------------------------
        // UPDATE TURRET
        // ---------------------------
        turret.periodic();

        // ---------------------------
        // UPDATE DRIVE
        // ---------------------------
        double x = gamepad1.left_stick_x;     // strafe
        double y = gamepad1.left_stick_y;     // forward/back
        double turn = gamepad1.right_stick_x; // rotation

        drivetrain.setMovementVectors(x, y, turn, false);
        drivetrain.periodic();

        // ---------------------------
        // TELEMETRY
        // ---------------------------

        // PID Constants
        telemetry.addLine("=== PID Constants (Tune via Dashboard) ===");
        telemetry.addData("Kp", TurretConstants.kP);
        telemetry.addData("Ki", TurretConstants.kI);
        telemetry.addData("Kd", TurretConstants.kD);
        telemetry.addData("Kf", TurretConstants.kF);
        telemetry.addLine("");

        // Turret Status
        telemetry.addLine("=== Turret Status ===");
        telemetry.addData("Current Angle", String.format("%.2f°", turret.getCurrentPosition()));
        telemetry.addLine("");

        // PID Tracking Info
        telemetry.addLine("=== PID Control ===");
        telemetry.addData("Error (TX)", String.format("%.2f°", turret.getPositionError()));
        telemetry.addData("Target Angle", "0° (centered)");
        telemetry.addLine("");

        // Drive Info
        telemetry.addLine("=== Drive Control ===");
        telemetry.addData("Drive X", x);
        telemetry.addData("Drive Y", y);
        telemetry.addData("Turn", turn);
        telemetry.addLine("");

        // Gamepad Controls
        telemetry.addLine("=== Controls ===");
        telemetry.addData("A", "Start Tracking");
        telemetry.addData("B", "Stop Tracking");
        telemetry.addData("X", "Return to Zero");
        telemetry.addData("Y", "Manual Control (Left Stick X)");

        // ---------------------------
        // GAMEPAD TURRRET CONTROLS
        // ---------------------------
        if (gamepad1.a) turret.setManualPower(0.3);
        if (gamepad1.b) turret.setManualPower(-0.3);

        telemetry.update();
    }

    @Override
    public void stop() {
        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }
}
