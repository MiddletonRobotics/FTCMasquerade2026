package org.firstinspires.ftc.teamcode.OpModes.Auto.RED;

import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import org.firstinspires.ftc.teamcode.Commands.InitializeCommand;
import org.firstinspires.ftc.teamcode.Commands.resetCommand;
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED.RedSide6BallPath;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

@Autonomous(name = "Red 6 Ball Auto", group = "RED")

public class newRed6Ball extends CommandOpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private Drivetrain drivetrain;
    private servoTransfer transfer;
    private Turret turret;

    private RedSide6BallPath Path;
    private Masquerade Robot;

    @Override
    public void initialize() {
        intake = new Intake(hardwareMap, telemetry);
        shooter = new Shooter(hardwareMap, telemetry, "RED");
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry, "RED");
        transfer = new servoTransfer(hardwareMap, telemetry);
        follower = drivetrain.follower;

        follower.setStartingPose(new Pose(124.250, 122.25, Math.toRadians(39)));
        follower.update();

        register(drivetrain, intake, transfer, shooter, turret);

        turret.relocalize();

        //turret.setAngle(-9.14);

        Path = new RedSide6BallPath(follower);

        //Begins
        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new RunCommand(follower::update),
                new RunCommand(turret::periodic),
                new SequentialCommandGroup(
                        new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
                        new FollowPathCommand(follower, Path.Path1, true, 1 ),
                        new WaitCommand(750),
                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
                        //new InstantCommand(intake::stopIntake),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(500)),
                        new InstantCommand(shooter::idleRPM),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(transfer::closeGate),

                        new InstantCommand(() -> follower.setMaxPower(0.75)),
                        new FollowPathCommand(follower, Path.Path2, false, .75).alongWith(new InstantCommand(intake::intake)),
                        new InstantCommand(() -> follower.setMaxPower(1)),

                        //set default or zero shooter
                        new InstantCommand(shooter::enableFlyWheel),
                        new InstantCommand(intake::stopIntake),
                        new FollowPathCommand(follower, Path.Path3, true, 1).alongWith(new InstantCommand(intake::intake)),
                        new InstantCommand(shooter::shootMid),
                        new InstantCommand(() -> turret.setAngle(-2.5)),
                        new WaitCommand(500),
                        new InstantCommand(transfer::openGate).andThen(new WaitCommand(1000)),
                        new InstantCommand(intake::intake),
                        new WaitCommand(500),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(500)),
                        new InstantCommand(shooter::disableFlyWheel),
                        new InstantCommand(transfer::retractPitch),
                        new FollowPathCommand(follower, Path.Path4, true, 1),
                        new InstantCommand(intake::stopIntake),
                        new resetCommand(intake, shooter, drivetrain, turret, transfer)
                )
        );
    }
}
