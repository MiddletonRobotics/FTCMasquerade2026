package org.firstinspires.ftc.teamcode.pedropathing;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.constants.DrivetrainConstants;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(14.56)
            .forwardZeroPowerAcceleration(-42.072)
            .lateralZeroPowerAcceleration(-67.58)

            .secondaryHeadingPIDFCoefficients(new PIDFCoefficients(3, 0, .04, 0))
            .headingPIDFCoefficients(new PIDFCoefficients(1, 0, 0.01, 0))

            .translationalPIDFCoefficients(new PIDFCoefficients(0.1,0,0.01,0.003))
            .secondaryTranslationalPIDFCoefficients(new PIDFCoefficients(0.07, 0, 0.019, 0.012))

            .useSecondaryDrivePIDF(true)
            .useSecondaryHeadingPIDF(true)
            .useSecondaryTranslationalPIDF(true)
            .forwardZeroPowerAcceleration(-31.427929263)
            .lateralZeroPowerAcceleration(-78.8335152);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .useBrakeModeInTeleOp(true)
            .rightFrontMotorName(DrivetrainConstants.fRMotorID)
            .rightRearMotorName(DrivetrainConstants.bRMotorID)
            .leftRearMotorName(DrivetrainConstants.bLMotorID)
            .leftFrontMotorName(DrivetrainConstants.fLMotorID)
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(76.239723)
            .yVelocity(58.2611430);

    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(5.984)
            .strafePodX(-5.039)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}
