package org.firstinspires.ftc.teamcode.OpModes.Auto.Paths.BLUE;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Blue6FarsidePath {
    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;

    public Blue6FarsidePath(Follower follower) {
        Path1 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(60.000, 8.000), new Pose(59.961, 27.564))
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(85))
                .build();

        Path2 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(59.961, 27.564),
                                new Pose(58.230, 37.770),
                                new Pose(34.938, 35.567),
                                new Pose(12.275, 35.725)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        Path3 = follower
                .pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Pose(12.275, 35.725),
                                new Pose(60.118, 36.197),
                                new Pose(59.961, 23.764)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(85))
                .build();

        Path4 = follower
                .pathBuilder()
                .addPath(
                        new BezierLine(new Pose(59.961, 23.764), new Pose(59.331, 34.466))
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();
    }
}
