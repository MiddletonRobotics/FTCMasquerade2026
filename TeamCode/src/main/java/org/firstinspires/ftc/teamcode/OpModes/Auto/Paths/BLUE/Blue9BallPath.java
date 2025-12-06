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

public class Blue9BallPath {

        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain Path6;

        public Blue9BallPath(Follower follower) {
            Path1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(19.000, 123.637), new Pose(59.230, 84.328))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(144))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(59.230, 84.328), new Pose(14.533, 83.887))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(14.533, 83.887), new Pose(59.230, 84.328))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(144))
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
                    .setConstantHeadingInterpolation(Math.toRadians(144))
                    .build();

            Path6 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(59.230, 84.328), new Pose(49.542, 74.199))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(144))
                    .build();
        }
}