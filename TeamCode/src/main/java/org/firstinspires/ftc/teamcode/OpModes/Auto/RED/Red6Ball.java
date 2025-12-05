package org.firstinspires.ftc.teamcode.OpModes.Auto.RED;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
import org.firstinspires.ftc.teamcode.Commands.ShootCommand;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;
import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED.Red6BallAuto;



//ts some AHHHHHuy
@Autonomous (name = "Red 6 Ball Auto", group = "RED AUTONOMOUS")
public class Red6Ball extends CommandOpMode {
    private Follower follower;
    private Intake intake;
    private Shooter shooter;

    private Drivetrain drivetrain;
    private servoTransfer transfer;
    private Turret turret;

    private Red6BallAuto path;
    private Masquerade Robot;

    public void reset() {
        shooter.disableFlyWheel();
        turret.stopTracking();
        intake.stopIntake();
        transfer.init();
        drivetrain.resetHeading();
        drivetrain.follower.setStartingPose(drivetrain.follower.getPose());
    }

    @Override
    public void initialize() {
        intake = new Intake(hardwareMap, telemetry);
        shooter = new Shooter(hardwareMap, telemetry, "RED");
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        transfer = new servoTransfer(hardwareMap, telemetry);
        turret = new Turret(hardwareMap, telemetry, "RED");
//        Robot = new Masquerade(hardwareMap, telemetry, gamepad1, gamepad2, "RED");

        follower = drivetrain.follower;
        follower.setStartingPose(new Pose(125, 123.627, Math.toRadians(39)));
        follower.update();

        register(drivetrain, intake, transfer, shooter, turret);

        //Turret Actions
        turret.relocalize();
        //turret.setAngle(-4); //change value

        path = new Red6BallAuto(follower);

        //shooter.shootMid();

        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new RunCommand(follower::update),
                new SequentialCommandGroup(
                        new InstantCommand(() -> shooter.enableFlyWheel()),
                        new InstantCommand(() -> shooter.shootMid()),

                        new InstantCommand(() -> turret.startLimelight()),
                        new InstantCommand(() -> turret.startTracking()),

                        //After init
                        new FollowPathCommand(follower, path.Path1, true, 1),
                        new WaitCommand(1500),
                        new InstantCommand(() -> intake.setPower(1)).andThen(new WaitCommand(1000)),
                        new InstantCommand(() -> transfer.extendPitch()).andThen(new WaitCommand(1000)),
                        new InstantCommand(() -> transfer.retractPitch()).andThen(new InstantCommand(() -> intake.setPower(0))),
                        new InstantCommand(() -> shooter.disableFlyWheel()),
                        new WaitCommand(500),
                        new InstantCommand(() -> intake.setPower(1)).andThen(new WaitCommand(1000)),
                        new FollowPathCommand(follower, path.Path2, false, .65),
                        new InstantCommand(() -> intake.stopIntake()),
                        new FollowPathCommand(follower, path.Path3, true, 1),
                        new WaitCommand(1000),
                        new InstantCommand(() -> shooter.enableFlyWheel()),
                        new WaitCommand(1500).alongWith(new InstantCommand(() -> shooter.shootMid())),

                        //new ShootCommand(shooter, transfer, intake).andThen(new InstantCommand(() -> shooter.stopMotor())),
                        new InstantCommand(() -> intake.setPower(1)).andThen(new WaitCommand(1000)),
                        new InstantCommand(() -> intake.setPower(0)).andThen(new InstantCommand(() -> transfer.extendPitch()),
                        new WaitCommand(500).andThen(new InstantCommand(() -> transfer.retractPitch()),
                        new InstantCommand(() -> shooter.disableFlyWheel()),

                        new FollowPathCommand(follower, path.Path4, true, 1)
                )
        )));
    }
}
