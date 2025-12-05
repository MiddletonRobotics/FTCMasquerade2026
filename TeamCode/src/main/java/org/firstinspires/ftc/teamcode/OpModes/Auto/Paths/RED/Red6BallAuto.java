package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Red6BallAuto {


        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;

        public Red6BallAuto (Follower follower) {
            Path1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(125.000, 123.637), new Pose(86.858, 85.980))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(39))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(86.858, 85.980),
                                    new Pose(104.404, 82.909),
                                    new Pose(128.485, 83.232)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(128.485, 83.232),
                                    new Pose(104.404, 82.909),
                                    new Pose(86.858, 85.980)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(39))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(86.858, 85.980), new Pose(103.434, 85.333))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(39))
                    .build();
        }
}
