package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Blue6BallPath {
    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;

    public Blue6BallPath(Follower follower) {
        Path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(125.000, 123.637).mirror(), new Pose(84.669, 83.882).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(144))
                .build();

        Path2 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(84.669, 85.882).mirror(), new Pose(125.975, 83.825).mirror())
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Path3 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(133.975, 83.825).mirror(), new Pose(84.669, 83.882).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(144))//
                .build();

        Path4 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(84.669, 83.882).mirror(), new Pose(93.482, 75.698).mirror())
                )
                .setConstantHeadingInterpolation(Math.toRadians(144))
                .build();
    }
}
