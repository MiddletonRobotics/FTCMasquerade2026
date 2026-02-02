package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import java.util.List;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.controller.wpilibcontroller.SimpleMotorFeedforward;

import org.firstinspires.ftc.library.geometry.Pose2d;
import org.firstinspires.ftc.library.geometry.Units;
import org.firstinspires.ftc.library.math.MathUtility;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.constants.GlobalConstants;
import org.firstinspires.ftc.teamcode.constants.TurretConstants;

public class Turret extends SubsystemBase {
    private DcMotorEx turretMotor;

    private PIDFController pidfController;
    private SimpleMotorFeedforward ffController;

    private Telemetry telemetry;

    public Turret(HardwareMap hMap, Telemetry telemetry) {
        turretMotor = hMap.get(DcMotorEx.class, TurretConstants.turretMotorID);
        turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        pidfController = new PIDFController(TurretConstants.kP, TurretConstants.kI, TurretConstants.kD, TurretConstants.kF);
        ffController = new SimpleMotorFeedforward(TurretConstants.kS, TurretConstants.kV, TurretConstants.kA);
    }

    @Override
    public void periodic() {
        telemetry.addData("Turret Current Open Loop", turretMotor.getPower());
        telemetry.addData("Turret Current Position", getCurrentPosition());
    }

    public static double normalizeAngle(double angle) {
        angle %= 360;
        if (angle > 180) angle -= 360;
        if (angle < -180) angle += 360;
        return angle;
    }

    public double getCurrentPosition() {
        return Math.toRadians(normalizeAngle(((turretMotor.getCurrentPosition() / 537.7) / TurretConstants.kTurretRatio) * 360));
    }

    public double getCurrentVelocity() {
        return Math.toRadians(normalizeAngle(((turretMotor.getVelocity() / 537.7) / TurretConstants.kTurretRatio) * 360));
    }

    public void setManualPower(double speed) {
        telemetry.addData("Turret Setpoint Open Loop", speed);
        turretMotor.setPower(speed);
    }

    public void resetPosition() {
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPosition(double radians) {
        telemetry.addData("Turret Setpoint Position", radians);
        telemetry.addData("Turret Primary Position Error", pidfController.getPositionError());
        telemetry.addData("Turret Primary At Setpoint?", pidfController.atSetPoint());

        if(GlobalConstants.kTuningMode) {
            pidfController.setPIDF(TurretConstants.kP, TurretConstants.kI, TurretConstants.kD, TurretConstants.kF);
        }

        pidfController.setSetPoint(radians);
        turretMotor.setPower(MathUtility.clamp(pidfController.calculate(getCurrentPosition(), radians), -0.65, 0.45) + ffController.calculate(Math.toDegrees(200)));
    }

    public double computeAngle(Pose2d robotPose, Pose targetPose, double turretOffsetX, double turretOffsetY) {
        double robotX = robotPose.getX();
        double robotY = robotPose.getY();
        double robotHeading = robotPose.getRotation().getRadians();

        double turretX = robotX + turretOffsetX * Math.cos(robotHeading) - turretOffsetY * Math.sin(robotHeading);
        double turretY = robotY + turretOffsetX * Math.sin(robotHeading) + turretOffsetY * Math.cos(robotHeading);

        double dx = targetPose.getX() - turretX;
        double dy = targetPose.getY() - turretY;

        double targetAngleGlobal = Math.atan2(dy, dx);
        double desiredTurretAngle = targetAngleGlobal - robotHeading;
        double normalizedAngle = AngleUnit.normalizeRadians(desiredTurretAngle);
        return MathUtility.clamp(normalizedAngle, -((37 * Math.PI) / 64), ((3 * Math.PI) / 4));
    }

    public Pose getTargetPose(GlobalConstants.AllianceColor allianceColor) {
        return allianceColor == GlobalConstants.AllianceColor.BLUE ? new Pose(144, 0, Units.degreesToRadians(135)) : new Pose(144, 144, Units.degreesToRadians(45));
    }
}