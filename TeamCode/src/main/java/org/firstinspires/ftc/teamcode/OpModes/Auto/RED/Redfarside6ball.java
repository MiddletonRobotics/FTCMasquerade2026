package org.firstinspires.ftc.teamcode.OpModes.Auto.RED;
import com.seattlesolvers.solverslib.command.CommandOpMode;
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
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE.Blue6FarsidePath;
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE.Blue9BallPath;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

@Autonomous(group = "RED", name = "RED 6 Ball Far Auto")
public class Redfarside6ball extends CommandOpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private Drivetrain drivetrain;
    private servoTransfer transfer;
    private Turret turret;

    private Blue6FarsidePath Path;
    private Masquerade Robot;

    @Override
    public void initialize() {
        intake = new Intake(hardwareMap, telemetry);
        shooter = new Shooter(hardwareMap, telemetry, "BlUE");
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry);
        transfer = new servoTransfer(hardwareMap, telemetry);
        follower = drivetrain.follower;

        follower.setStartingPose(new Pose(56, 9, Math.toRadians(180)));
        follower.update();
        follower.setMaxPower(0.85);

        register(drivetrain, intake, transfer, shooter, turret);

        //turret.setAngle(-9.14);

        Path = new Blue6FarsidePath(follower);

        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new RunCommand(follower::update),
                new RunCommand(turret::periodic),
                new SequentialCommandGroup(
                        new InstantCommand(shooter::enableFlyWheel),
                        new InstantCommand(shooter::shootMid),
                        new InstantCommand(() -> shooter.setHoodServoPos(0.52)),
                        new InstantCommand((transfer::init)),
                        new FollowPathCommand(follower, Path.Path1, true, 1),
                        new WaitCommand(1000),
                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(750)),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(intake::stopIntake),
                        new InstantCommand(shooter::idleRPM),
                        //Intake -> shoot
                        new InstantCommand(transfer::closeGate),
                        new FollowPathCommand(follower, Path.Path2, false, 1).alongWith(new InstantCommand(intake::intake)),
                        new WaitCommand(500),
                        new InstantCommand(intake::stopIntake),
                        new FollowPathCommand(follower, Path.Path3, true, 1).alongWith(new InstantCommand(shooter::shootMid)),
                        new InstantCommand(transfer::openGate),
                        new WaitCommand(500),
                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(750)),
                        new InstantCommand(transfer::retractPitch),
                        new InstantCommand(intake::stopIntake),
                        new InstantCommand(shooter::disableFlyWheel),
                        new FollowPathCommand(follower, Path.Path4, true, 1),
                        //new InstantCommand(() -> turret.setAngle(-3)),
                        new resetCommand(intake, shooter, drivetrain, turret, transfer)
                )
        );

    }
}
