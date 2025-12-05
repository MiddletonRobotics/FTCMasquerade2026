package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

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
                            new BezierLine(new Pose(125.000, 123.637), new Pose(84.669, 83.882))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(0))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(84.669, 83.882), new Pose(125.975, 83.825))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(125.975, 83.825), new Pose(84.669, 83.825))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(39))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(84.669, 83.825),
                                    new Pose(85.770, 56.813),
                                    new Pose(125.975, 59.625)
                            )
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            Path5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(125.975, 59.625),
                                    new Pose(85.770, 56.813),
                                    new Pose(84.669, 83.882)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(39))
                    .build();

            Path6 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(84.669, 83.882), new Pose(92.380, 75.226))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(39))
                    .build();
        }
}
