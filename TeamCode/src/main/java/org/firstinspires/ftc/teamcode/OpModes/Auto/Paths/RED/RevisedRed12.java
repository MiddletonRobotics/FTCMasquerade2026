package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class RevisedRed12 {

    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;
    public PathChain Path5;
    public PathChain Path6;
    public PathChain Path7;
    public PathChain Path8;

    public RevisedRed12(Follower follower) {
        Path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(20.15, 122.56).mirror(), new Pose(59.230, 84.328).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(45))
                .build();

        Path2 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.230, 84.328).mirror(), new Pose(20.833, 83.887).mirror())//14.535
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        Path3 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(20.533, 83.887).mirror(), new Pose(59.230, 84.328).mirror())
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();

        Path4 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(59.230, 84.328).mirror(),
                                new Pose(62.312, 55.043).mirror(), //helo
                                new Pose(30.606, 60.548).mirror(),
                                new Pose(18.689, 60.108).mirror() //60.108 13.658 x
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        Path5 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(18.689, 60.108).mirror(),
                                new Pose(62.312, 55.043).mirror(),
                                new Pose(59.230, 84.328).mirror()
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();

        Path6 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(59.230, 84.328).mirror(),
                                new Pose(55.711, 25.338).mirror(),
                                new Pose(33.364, 37.298).mirror(),
                                new Pose(18.689, 35.567).mirror()
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(0))
                .build();

        Path7 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(18.689, 35.567).mirror(),
                                new Pose(59.174, 31.948).mirror(),
                                new Pose(59.230, 84.328).mirror()
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(45))
                .build();

        Path8 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.230, 84.328).mirror(), new Pose(53.193, 76.170).mirror())
                )
                .setConstantHeadingInterpolation(Math.toRadians(45))
                .build();
    }
}