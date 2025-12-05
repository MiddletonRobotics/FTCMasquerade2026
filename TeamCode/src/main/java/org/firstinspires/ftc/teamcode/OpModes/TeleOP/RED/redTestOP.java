package org.firstinspires.ftc.teamcode.OpModes.TeleOP.RED;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;

@TeleOp (name = "RED TeleOP Test", group = "RED Alliance")
public class redTestOP extends OpMode {

    private Masquerade Robot;

    @Override
    public void init() {
        Robot = new Masquerade(hardwareMap, telemetry, gamepad1, gamepad2, "RED");
        Robot.reset();
    }

    @Override
    public void start() {
        Robot.dt.follower.setMaxPower(0.80);
        Robot.dt.enableTeleop();
        Robot.start();
    }

    @Override
    public void loop() {
        Robot.controlMap();
        Robot.Periodic();
    }
}
