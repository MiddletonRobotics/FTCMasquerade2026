package org.firstinspires.ftc.teamcode.OpModes.Auto;

import com.pedropathing.geometry.Pose;
import com.pedropathing.localization.PoseTracker;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.DanceDemo;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;


@Autonomous(name="DemoAuto")
public class PedroTestAuto extends CommandOpMode
{
    private DanceDemo Chain;
    private Drivetrain dt;

    @Override
    public void initialize()
    {
        Follower follower = Constants.createFollower(hardwareMap);
        dt = new Drivetrain(hardwareMap, telemetry);

        dt.follower.setStartingPose(new Pose(36, 33, Math.toRadians(90)));

        Chain = new DanceDemo(dt.follower);

        schedule(
                new WaitUntilCommand(this::opModeIsActive),
                new SequentialCommandGroup(
                        new FollowPathCommand(dt.follower, Chain.Path1),
                        new WaitCommand(250),
                        new FollowPathCommand(dt.follower, Chain.Path2),
                        new InstantCommand(() -> dt.resetHeading())
                ));
    }
}