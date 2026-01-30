package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE;

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

public class Blue12BallPath {

    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;
    public PathChain Path5;
    public PathChain Path6;
    public PathChain Path7;
    public PathChain Path8;

    public Blue12BallPath(Follower follower) {
        Path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(19.000, 123.637), new Pose(59.230, 84.328))
                )
                .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(138))
                .build();

        Path2 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.230, 84.328), new Pose(14.833, 83.887))//14.535
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Path3 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(14.533, 83.887), new Pose(59.230, 84.328))
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(137))
                .build();

        Path4 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(59.230, 84.328),
                                new Pose(62.312, 55.043),
                                new Pose(30.606, 60.548),
                                new Pose(9.689, 60.108)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Path5 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(9.689, 60.108),
                                new Pose(62.312, 55.043),
                                new Pose(59.230, 84.328)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(132))
                .build();

        Path6 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(59.230, 84.328),
                                new Pose(63.266, 27.541),
                                new Pose(33.049, 36.669),
                                new Pose(13.321, 35.567)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Path7 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(14.321, 35.567),
                                new Pose(65.469, 31.161),
                                new Pose(59.230, 84.328)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(132))
                .build();

        Path8 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.230, 84.328), new Pose(53.193, 76.170))
                )
                .setConstantHeadingInterpolation(Math.toRadians(132))
                .build();
    }
}