package org.firstinspires.ftc.teamcode.subsystems;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.kf;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.controller.wpilibcontroller.SimpleMotorFeedforward;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;

import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.kp;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.ki;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.kd;
import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.shooterMotorID;

import java.lang.Math;
import java.util.List;

public class Shooter extends SubsystemBase {
    private MotorEx shooterMotor;
    private ServoEx hoodServo;
    private ServoEx hoodServo2;
    private Limelight3A limelight;
    private Motor.Encoder shooterEncoder;

    private double currentMotorVelocity;
    private double currentEncoderVelocity;
    private double targetVelocity;
    private double currentDistance;
    private double Distance;
    private String Alliance;

    private double hoodPosition;
    private boolean flyWheelEnabled;
    private boolean trackingEnabled;

    private List<LLResultTypes.FiducialResult> tagList;

    LLResult result;

    LLResultTypes.FiducialResult currentTag;

    PIDFController motorController;
    SimpleMotorFeedforward FFController;

    private Telemetry telemetry;

    public Shooter(HardwareMap hmap, Telemetry telemetry, String Alliance) {
        shooterMotor = new MotorEx(hmap, shooterMotorID, Motor.GoBILDA.BARE);
        shooterMotor.setRunMode(Motor.RunMode.RawPower);
        shooterMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        shooterMotor.motor.setDirection(DcMotorSimple.Direction.FORWARD);

        shooterEncoder = shooterMotor.encoder;

        shooterEncoder.reset();

        limelight = hmap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
        limelight.setPollRateHz(50);

        hoodServo = new ServoEx(hmap, "hoodServo");

        motorController = new PIDFController(kp, ki, kd, kf);

        FFController = new SimpleMotorFeedforward(0.01, 0.0, 0.0);

        hoodServo.set(0);

        flyWheelEnabled = false;

        // ***** FIXED ALLIANCE STORAGE *****
        this.Alliance = Alliance.toUpperCase();

        flyWheelEnabled = true;

        targetVelocity = 100; //Default value

        tagList = limelight.getLatestResult().getFiducialResults();

        this.telemetry = telemetry;
    }

    @Override
    public void periodic() {
        currentMotorVelocity = getMotorVelocity();
        currentEncoderVelocity = getVelocity();

        trackingEnabled = limelight.getLatestResult().isValid();
        result = limelight.getLatestResult();

        currentDistance = getCamDistance();

        currentTag = getTag();

        motorController.setPIDF(kp, ki, kd, kf);

        if(flyWheelEnabled) {
            setShooterVelocity();
        } else {
            stopMotor();
        }

        telemetry.addData(("Encoder Velocity: "), currentEncoderVelocity);
        telemetry.addData(("Motor Velocity: "), currentMotorVelocity);
        telemetry.addData("Target RPM: ", targetVelocity);
        telemetry.addData("Fly Wheel Enabled: ", flyWheelEnabled);
        telemetry.addData(("Servo Position: "), hoodServo.getRawPosition());

        telemetry.addLine("");
        telemetry.addLine("Limelight Values");

        telemetry.addData(("Camera Distance: "), currentDistance);
        telemetry.addData(("Limelight tracking: "), trackingEnabled);
        telemetry.addData(("Tag ID: "), result.getFiducialResults());
        telemetry.addData(("Alliance Color: "), Alliance);

        if (currentTag != null) {
            telemetry.addData("Detected Tag ID", currentTag.getFiducialId());
        } else {
            telemetry.addData("Detected Tag ID", "None");
        }

        telemetry.update();
    }

    public double getVelocity() {
        return shooterEncoder.getCorrectedVelocity();
    }

    public double getMotorVelocity() {return shooterMotor.getVelocity();}

    public void setMotorVelocity(Double RPM) {
        shooterMotor.setVelocity(RPM);
    }

    public void setMotorPower(Double POWER) {
        shooterMotor.motor.setPower(POWER);
    }

    public void setMax() {
        shooterMotor.set(1);
    }

    public void reverseMotor() {
        shooterMotor.motor.setPower(-0.20);
    }

    public void stopMotor() {
        shooterMotor.motor.setPower(0);
    }

    public void disableFlyWheel() {
        flyWheelEnabled = false;
    }

    public void enableFlyWheel() {
        flyWheelEnabled = true;
        targetVelocity = 100;
    }

    public void setShooterVelocity() {
        motorController.setPIDF(kp, ki, kd, kf);
        shooterMotor.motor.setPower((motorController.calculate(currentMotorVelocity, targetVelocity)));
    }

    public void setServoPos() {

        hoodServo.set(0);

        if(currentTag != null) {
            if (currentDistance > 0 && currentDistance < 48) {
                hoodServo.set(0);
            }

            else if (currentDistance >= 48 && currentDistance < 84) {
                hoodServo.set(0.25);
            }

            else if (currentDistance > 100) {
                hoodServo.set(.70);
            }

            else {
                hoodServo.set(0);
            }
        }
    }

    public void setHoodServoPos(Double POS) {
        hoodServo.set(POS);
    }

    public double getCamDistance() {
        double LLAngle = 15;
        double LLHeight = 13.5;
        double aprilTagHeight = 29.5;

        double angleToGoalDegrees = LLAngle + limelight.getLatestResult().getTy();
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);

        return (aprilTagHeight - LLHeight) / Math.tan(angleToGoalRadians);
    }

    // ***** FULLY UPDATED getTag() WITH LOGGING INTEGRATED *****
    public LLResultTypes.FiducialResult getTag()
    {
        LLResult latest = limelight.getLatestResult();
        if (latest == null) {
            telemetry.addLine("[TAG] Latest Limelight result NULL");
            return null;
        }

        if (!latest.isValid()) {
            telemetry.addLine("[TAG] Latest Limelight result INVALID");
            return null;
        }

        tagList = latest.getFiducialResults();

        if (tagList == null || tagList.isEmpty()) {
            telemetry.addLine("[TAG] No fiducials detected");
            return null;
        }

        int targetID = Alliance.equals("BLUE") ? 20 : 24;
        telemetry.addData("[TAG] Alliance", Alliance);
        telemetry.addData("[TAG] Looking for ID", targetID);

        LLResultTypes.FiducialResult bestTag = null;
        double bestScore = Double.MAX_VALUE;

        for (LLResultTypes.FiducialResult tag : tagList) {

            if (tag == null) continue;

            telemetry.addData("[TAG] Seen ID", tag.getFiducialId());
            telemetry.addData("[TAG] Tx", tag.getTargetXDegrees());
            telemetry.addData("[TAG] Ty", tag.getTargetYDegrees());

            if (tag.getFiducialId() == targetID) {

                double score = Math.abs(tag.getTargetXDegrees());

                if (score < bestScore) {
                    bestScore = score;
                    bestTag = tag;
                }
            }
        }

        if (bestTag == null) {
            telemetry.addLine("[TAG] Correct alliance tag NOT FOUND");
        } else {
            telemetry.addData("[TAG] Selected Tag", bestTag.getFiducialId());
            telemetry.addData("[TAG] Final Tx", bestTag.getTargetXDegrees());
        }

        return bestTag;
    }

    public void shootClose() {
        targetVelocity = ShooterConstants.closeShot;
    }

    public void shootMid() {
        targetVelocity = ShooterConstants.midShot;
    }

    public void shootFar() {
        targetVelocity = ShooterConstants.farShot;
    }

    public void idleRPM() {
        targetVelocity = 200;
    }

    public void setRPM(double RPM) {
        targetVelocity = RPM;
    }

    public boolean isAtSpeed() {
        double error = Math.abs(targetVelocity - currentMotorVelocity);
        return error < 25;
    }
}
