package org.firstinspires.ftc.teamcode.OpModes.TeleOP.BLUE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandScheduler;

import org.firstinspires.ftc.teamcode.subsystems.Masquerade;

@TeleOp (name = "BLUE TeleOP Test", group = "BLUE Alliance")
public class blueTestOP extends OpMode {
    private Masquerade Robot;

    @Override
    public void init() {
        Robot = new Masquerade(hardwareMap, telemetry, gamepad1, gamepad2, "BLUE");
        Robot.reset();
        Robot.transfer.openGate();
    }

    @Override
    public void start() {
        Robot.start();
    }

    @Override
    public void loop() {
        Robot.controlMap();
        Robot.Periodic();
    }
}
