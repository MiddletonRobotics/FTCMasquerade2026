package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class RedSide6BallPath {
        public PathChain Path1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;

        public RedSide6BallPath(Follower follower) {
            Path1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(125.000, 123.637), new Pose(84.669, 83.882)) //87.187, 86.557 //84.669, 83.882 Old
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(39)
                    )
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
                            new BezierLine(new Pose(133.975, 83.825), new Pose(84.669, 83.882))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(39))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(84.669, 83.882), new Pose(93.482, 75.698))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(39))
                    .build();
        }
}
