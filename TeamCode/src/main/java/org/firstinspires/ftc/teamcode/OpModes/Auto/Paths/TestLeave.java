package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

public class TestLeave {
    public PathChain Path1;

    public TestLeave(Follower follower) {
        Path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(49.000, 8.000), new Pose(49.000, 102.000))
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(270))
                .build();
    }
}
