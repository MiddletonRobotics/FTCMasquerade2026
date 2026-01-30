//package org.firstinspires.ftc.teamcode.OpModes.Auto.RED.;
//
//import com.pedropathing.geometry.Pose;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.seattlesolvers.solverslib.command.CommandOpMode;
//import com.pedropathing.follower.Follower;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.RunCommand;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//import com.seattlesolvers.solverslib.command.WaitCommand;
//import com.seattlesolvers.solverslib.command.WaitUntilCommand;
//import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;
//
//import org.firstinspires.ftc.teamcode.Commands.InitializeCommand;
//import org.firstinspires.ftc.teamcode.Commands.resetCommand;
//import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE.newBlue12th;
//import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
//import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
//import org.firstinspires.ftc.teamcode.subsystems.Intake;
//import org.firstinspires.ftc.teamcode.subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.subsystems.Turret;
//import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;
//
//@Autonomous(name = "Revised 12 Ball Blue", group = "Blue")
//public class Red12BallAuto extends CommandOpMode {
//    private Follower follower;
//    private Intake intake;
//    private Shooter shooter;
//
//    private Drivetrain drivetrain;
//    private servoTransfer transfer;
//    private Turret turret;
//
//    private newBlue12th Path;
//    private Masquerade Robot;
//
//    @Override
//    public void initialize() {
//        intake = new Intake(hardwareMap, telemetry);
//        shooter = new Shooter(hardwareMap, telemetry, "BLUE");
//        drivetrain = new Drivetrain(hardwareMap, telemetry);
//        turret = new Turret(hardwareMap, telemetry, "BLUE");
//        transfer = new servoTransfer(hardwareMap, telemetry);
//        follower = drivetrain.follower;
//
//        follower.setStartingPose(new Pose(144-20.15, 122.56, Math.toRadians(39)));
//        follower.update();
//
//        register(drivetrain, intake, transfer, shooter, turret);
//
//        follower.setMaxPower(0.85);
//
//        turret.stopTracking();
//
//        turret.relocalize();
//
//        //turret.setAngle(-9.14);
//
//        Path = new newBlue12th(follower);
//
//        //Begins
//        schedule(
//                new WaitUntilCommand(this::opModeIsActive),
//                new RunCommand(follower::update),
//                new RunCommand(turret::periodic),
//                new SequentialCommandGroup(
//                        //new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
//                        new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
//                        //new InitializeCommand(intake, shooter, drivetrain, turret, transfer),
//                        new InstantCommand(() -> turret.setAngle(-3)),
//                        new InstantCommand(shooter::enableFlyWheel),
//                        new InstantCommand(() -> shooter.setRPM(1250)),
//                        //new InstantCommand(() -> turret.setAngle(-10)),
//                        new InstantCommand((transfer::init)),
//                        new FollowPathCommand(follower, Path.Path1, true, 1),
//                        new WaitCommand(1000),
//                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
//                        new InstantCommand(transfer::extendPitch),
//                        new WaitCommand(1000),
//                        new InstantCommand(intake::stopIntake),
//                        new InstantCommand(transfer::retractPitch),
//                        new InstantCommand(shooter::idleRPM),
//                        new InstantCommand(transfer::closeGate),
//                        new InstantCommand(() -> follower.setMaxPower(0.75)),
//                        new FollowPathCommand(follower, Path.Path2, false, 0.75).alongWith(new InstantCommand(intake::intake)),
//                        new WaitCommand(250),
//                        new InstantCommand(intake::stopIntake),
//                        new InstantCommand(() -> follower.setMaxPower(1)),
//                        new InstantCommand(() -> shooter.setRPM(1250)),
//                        new FollowPathCommand(follower, Path.Path3, true, 1),
//                        new WaitCommand(500),
//                        new InstantCommand(() -> follower.setMaxPower(0.85)),
//                        new InstantCommand(transfer::openGate),
//                        new InstantCommand(intake::intake),
//                        new WaitCommand(1000),
//                        new InstantCommand(transfer::extendPitch),
//                        new WaitCommand(750),
//                        new InstantCommand(transfer::retractPitch),
//                        new InstantCommand(shooter::idleRPM),
//                        new InstantCommand(transfer::closeGate),
//                        new InstantCommand(() -> follower.setMaxPower(0.85)),
//                        new FollowPathCommand(follower, Path.Path4, false, .85).alongWith(new InstantCommand(intake::intake)),
//                        new WaitCommand(250),
//                        new InstantCommand(intake::stopIntake),
//                        new InstantCommand(() -> shooter.setRPM(1250)),
//                        new InstantCommand(() -> follower.setMaxPower(1)),
//                        new FollowPathCommand(follower, Path.Path5, true, 1 ),
//                        new WaitCommand(500),
//                        new InstantCommand(transfer::openGate),
//                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
//                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(750)),
//                        new InstantCommand(transfer::retractPitch),
//                        new InstantCommand(transfer::closeGate),
//                        new InstantCommand(() -> follower.setMaxPower(.85)),
//                        new FollowPathCommand(follower, Path.Path6, true, .85).alongWith(new InstantCommand(intake::intake)),
//                        new WaitCommand(250),
//                        new InstantCommand(intake::stopIntake),
//                        new InstantCommand(() -> shooter.setRPM(1250)),
//                        new InstantCommand(() -> follower.setMaxPower(1)),
//                        new FollowPathCommand(follower, Path.Path7, true, 1),
//                        new WaitCommand(500),
//                        new InstantCommand(transfer::openGate),
//                        new InstantCommand(intake::intake).andThen(new WaitCommand(1000)),
//                        new InstantCommand(transfer::extendPitch).andThen(new WaitCommand(750)),
//                        new InstantCommand(transfer::retractPitch),
//                        new InstantCommand(transfer::closeGate),
//                        new InstantCommand(() -> follower.setMaxPower(1)),
//                        new FollowPathCommand(follower, Path.Path8, true, 1)
//                )
//        );
//    }
//}
