package org.firstinspires.ftc.teamcode.OpModes.Auto.RED;

import com.pedropathing.geometry.Pose;
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
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED.Red9BallPath;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;


public class Red9BallAuto extends CommandOpMode{
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private Drivetrain drivetrain;
    private servoTransfer transfer;
    private Turret turret;

    private Red9BallPath Path;
    private Masquerade Robot;

    @Override
    public void initialize() {
        intake = new Intake(hardwareMap, telemetry);
        shooter = new Shooter(hardwareMap, telemetry, "RED");
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry, "RED");
        transfer = new servoTransfer(hardwareMap, telemetry);
        follower = drivetrain.follower;

        follower.setStartingPose(new Pose(125, 123.627, Math.toRadians(39)));
        follower.update();

        register(drivetrain, intake, transfer, shooter, turret);

        turret.relocalize();
        //turret.setAngle(-9.14);

        Path = new Red9BallPath(follower);

        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new RunCommand(follower::update),
                new SequentialCommandGroup(
                        new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
                        new FollowPathCommand(follower, Path.Path1, true, 1 ),
                        new WaitCommand(750),
                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
                        new InstantCommand(intake::stopIntake),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(500)),
                        new InstantCommand(shooter::disableFlyWheel),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(transfer::closeGate),
                        new InstantCommand(intake::intake),
                        new FollowPathCommand(follower, Path.Path2, false, .70),
                        new InstantCommand(intake::stopIntake),
                        //set default or zero shooter
                        new InstantCommand(shooter::enableFlyWheel),
                        new FollowPathCommand(follower, Path.Path3, true, 1).alongWith(new InstantCommand(shooter::shootClose)),
                        new WaitCommand(750),
                        new InstantCommand(transfer::openGate).andThen(new InstantCommand(intake::intake)),
                        new WaitCommand(1000),
                        new InstantCommand(intake::stopIntake),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(500)),
                        new InstantCommand(shooter::disableFlyWheel),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(transfer::closeGate)
                )
        );
    }
}
