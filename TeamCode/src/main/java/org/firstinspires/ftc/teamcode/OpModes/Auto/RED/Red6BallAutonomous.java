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
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED.Red6BallPath;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

import java.util.LinkedHashSet;

@Autonomous(name = "Revised Red 6 ball", group = "RED")
public class Red6BallAutonomous extends CommandOpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private Drivetrain drivetrain;
    private servoTransfer transfer;
    private Turret turret;

   private Red6BallPath Path;
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

        Path = new Red6BallPath(follower);

        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new RunCommand(follower::update),
                new SequentialCommandGroup(
                        new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
                        new FollowPathCommand(follower, Path.line1, true, .9).alongWith(new InstantCommand(shooter::shootClose)),
                        new InstantCommand(() -> intake.setPower(1)).andThen(new WaitCommand(2000)),
                        new InstantCommand(() -> transfer.extendPitch()),
                        new InstantCommand(() -> shooter.disableFlyWheel()),
                        new InstantCommand(() -> transfer.retractPitch()),
                        new InstantCommand(transfer::closeGate),
                        new FollowPathCommand(follower, Path.Path2, true, 1),
                        new WaitCommand(2000),
                        new FollowPathCommand(follower, Path.Path3, false, 1),
                        new InstantCommand(() -> intake.setPower(0)),
                        new InstantCommand(shooter::shootMid).andThen(new InstantCommand(transfer::openGate)),
                        new FollowPathCommand(follower, Path.Path4, true, 1).alongWith(new InstantCommand(shooter::enableFlyWheel)),
                        new WaitCommand(1500),
                        new InstantCommand(() -> intake.setPower(1)).andThen(new WaitCommand(1000)),
                        new InstantCommand(() -> transfer.extendPitch()),
                        new InstantCommand(() -> transfer.retractPitch()),
                        new InstantCommand(shooter::disableFlyWheel),
                        new FollowPathCommand(follower, Path.Path5, false, 1),
                        new InstantCommand(intake::stopIntake)
                )
        );
    }
}
