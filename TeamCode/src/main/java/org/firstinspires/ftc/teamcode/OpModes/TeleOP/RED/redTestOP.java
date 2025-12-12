package org.firstinspires.ftc.teamcode.OpModes.TeleOP.RED;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

@TeleOp(name = "RED TeleOP Test", group = "RED Alliance")
public class redTestOP extends OpMode {
    private Masquerade Robot;
    private Telemetry telemetryB;
    private Shooter shooter;
    private FtcDashboard dashboard;   // <-- Store dashboard instance

    @Override
    public void init() {
        Robot = new Masquerade(hardwareMap, telemetry, gamepad1, gamepad2, "RED");
        Robot.reset();

        dashboard = FtcDashboard.getInstance();
        telemetryB = dashboard.getTelemetry();
    }

    @Override
    public void start() {
        Robot.dt.follower.setMaxPower(0.85);
        Robot.start();
        Robot.turret.setAngle(-3);
        Robot.turret.startTracking();
    }

    @Override
    public void loop() {
        Robot.controlMap();
        Robot.Periodic();

        double targetVel = Robot.shooter.getTargetVelocity();
        double currentVel = Robot.shooter.getMotorVelocity();

        // Normal dashboard text telemetry
        telemetryB.addData("Target Velocity", targetVel);
        telemetryB.addData("Current Velocity", currentVel);
        telemetryB.update();

        // **GRAPHING PACKET**
        TelemetryPacket packet = new TelemetryPacket();
        packet.put("TargetVelocity", targetVel);
        packet.put("CurrentVelocity", currentVel);


        Robot.shooter.periodic();
        dashboard.sendTelemetryPacket(packet);
    }
}
