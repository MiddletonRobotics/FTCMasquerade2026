package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathBuilder;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;

public class Red9BallPath {

    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;
    public PathChain Path5;
    public PathChain Path6;

    public Red9BallPath(Follower follower) {
        Path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(19.000, 123.637).mirror(), new Pose(59.230, 84.328).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(50))
                .build();

        Path2 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.230, 84.328).mirror(), new Pose(14.533, 83.887).mirror())
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        Path3 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(14.533, 83.887).mirror(), new Pose(59.230, 84.328).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(50))
                .build();

        Path4 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(59.230, 84.328).mirror(),
                                new Pose(62.312, 55.043).mirror(),
                                new Pose(30.606, 60.548).mirror(),
                                new Pose(9.689, 60.108).mirror()
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        Path5 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(9.689, 60.108).mirror(),
                                new Pose(62.312, 55.043).mirror(),
                                new Pose(59.230, 84.328).mirror()
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(50))
                .build();

        Path6 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.230, 84.328).mirror(), new Pose(49.542, 74.199).mirror())
                )
                .setConstantHeadingInterpolation(Math.toRadians(50))
                .build();
    }
}