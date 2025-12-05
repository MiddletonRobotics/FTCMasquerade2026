package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.RED;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Red6BallPath {


        public PathChain line1;
        public PathChain Path2;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;

        public Red6BallPath(Follower follower) {
            line1 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(125.000, 123.637), new Pose(86.858, 85.980))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(39))
                    .build();

            Path2 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(86.858, 85.980), new Pose(102.925, 83.725))
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            Path3 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(102.925, 83.725), new Pose(123, 83.725)) //122.925
                    )
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            Path4 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(122.925, 83.725), new Pose(86.858, 85.980))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(39))
                    .build();

            Path5 = follower
                    .pathBuilder()
                    .addPath(
                            new BezierLine(new Pose(86.858, 85.980), new Pose(94.426, 76.957))
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(39), Math.toRadians(45))
                    .build();
        }
}
