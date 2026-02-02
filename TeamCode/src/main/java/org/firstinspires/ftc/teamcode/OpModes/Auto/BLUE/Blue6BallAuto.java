package org.firstinspires.ftc.teamcode.OpModes.Auto.BLUE;
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
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE.Blue6BallPath;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

@Autonomous(name = "BLUE 6 Ball Auto", group = "Blue")
public class Blue6BallAuto extends CommandOpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private Drivetrain drivetrain;
    private servoTransfer transfer;
    private Turret turret;

    private Blue6BallPath Path;
    private Masquerade Robot;

    @Override
    public void initialize() {
        intake = new Intake(hardwareMap, telemetry);
        shooter = new Shooter(hardwareMap, telemetry, "RED");
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry);
        transfer = new servoTransfer(hardwareMap, telemetry);
        follower = drivetrain.follower;

        follower.setStartingPose(new Pose(125, 123.627, Math.toRadians(39)).mirror());
        follower.update();

        register(drivetrain, intake, transfer, shooter, turret);

        //turret.setAngle(-9.14);

        Path = new Blue6BallPath(follower);

        //Begins
        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new RunCommand(follower::update),
                new RunCommand(turret::periodic),
                new SequentialCommandGroup(
                        //new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
                        new InstantCommand(shooter::enableFlyWheel),
                        new InstantCommand(() -> shooter.setRPM(1680)),
                        //new InstantCommand(() -> turret.setAngle(-10)),
                        new InstantCommand((transfer::init)),
                        new FollowPathCommand(follower, Path.Path1, true, 1 ),
                        new WaitCommand(1000),
                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
                        new InstantCommand(intake::stopIntake),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(500)),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(shooter::idleRPM),
                        new InstantCommand(transfer::closeGate),
                        new InstantCommand(() -> follower.setMaxPower(0.70)),
                        new FollowPathCommand(follower, Path.Path2, false, .7).alongWith(new InstantCommand(intake::intake)),
                        new InstantCommand(intake::stopIntake),
                        new InstantCommand(() -> follower.setMaxPower(1)),
                        new InstantCommand(shooter::enableFlyWheel),
                        new FollowPathCommand(follower, Path.Path3, true, 1).alongWith( new InstantCommand(() -> shooter.setRPM(1680))),
                        //new InstantCommand(() -> turret.setAngle(-6)),
                        new WaitCommand(500),
                        new InstantCommand(transfer::openGate).andThen(new WaitCommand(1000)),
                        new InstantCommand(intake::intake),
                        new WaitCommand(500),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(500)),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(shooter::disableFlyWheel),
                        new FollowPathCommand(follower, Path.Path4, true, 1),
                        new InstantCommand(intake::stopIntake),
                        new resetCommand(intake, shooter, drivetrain, turret, transfer)
                )
        );
    }
}
