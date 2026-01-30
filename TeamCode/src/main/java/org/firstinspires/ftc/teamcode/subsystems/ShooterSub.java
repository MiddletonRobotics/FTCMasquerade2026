package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.util.InterpLUT;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class ShooterSub extends SubsystemBase {
    public DcMotorEx shooterMotor;
    private Servo hoodServo;
    private double hoodAngle;
    private Servo light;
    private Vision limelight;
    private InterpLUT hoodTable;
    private Telemetry telemetry;

    public double currentRPM;
    public double targetRPM;
    public double autoRPM;
    public boolean shooterOn;
    public boolean useAuto = false;
    private boolean idlePowerEnabled = false;
    private double distance = 1;

    public static double kP = 0.0009;
    public static double kI = 0.0;
    public static double kD = 0.0001;
    public static double kF = 0.0002;

    public static double closeRPM = 3250;
    public static double midRPM = 4000;
    public static double farRPM = 5000;
    public static double idleRPM = 2500;
    public static double rpmTolerance = 50;

    public static double regA = -0.0457;
    public static double regB = 21.1466;
    public static double regC = 2805.64;

    public static double tagHeight = 30.0;
    public static double cameraHeight = 11.5;
    public static double cameraAngle = 18.9;

    private static final double TICKS_PER_REV = 28.0;
    private static final double initialServoPos = 1;

    public PIDFController velocityController;

    public ShooterSub(HardwareMap hardwareMap, Telemetry telemetry, Vision limelight) {
        this.telemetry = telemetry;
        this.limelight = limelight;

        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooterMotor");
        shooterMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
        hoodServo.setDirection(Servo.Direction.REVERSE);
        hoodServo.setPosition(0);

        //light = hardwareMap.get(Servo.class, "light");
        distance = 1;
        autoRPM = 0;

        hoodTable = new InterpLUT();
        hoodTable.add(0, initialServoPos);
        hoodTable.add(37, initialServoPos);
        hoodTable.add(75, initialServoPos - 0.15);
        hoodTable.add(110, initialServoPos - 0.25);
        hoodTable.add(140, initialServoPos - 0.35);
        hoodTable.add(200, initialServoPos - 0.35);
        hoodTable.add(300, initialServoPos - 0.35);
        hoodTable.createLUT();

        velocityController = new PIDFController(kP, kI, kD, kF);
        shooterOn = false;
        targetRPM = closeRPM;
    }

    @Override
    public void periodic() {
        updateDistanceFromLimelight();
        updateAutoRPM();
        autoHood();

        if (useAuto) {
            shootAutoRPM();
            setHood(hoodAngle);
        }



        double tps = shooterMotor.getVelocity();
        currentRPM = (tps / TICKS_PER_REV) * 60.0;

        if (shooterOn) {
            shooterMotor.setPower(velocityController.calculate(currentRPM, targetRPM));
        } else if (idlePowerEnabled) {
            shooterMotor.setPower(velocityController.calculate(currentRPM, idleRPM));
        } else {
            shooterMotor.setPower(0);
        }

        setLight();

        telemetry.addData("Shooter On", shooterOn);
        telemetry.addData("Current RPM", currentRPM);
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Distance", distance);
        telemetry.addData("Auto RPM", autoRPM);
        telemetry.addData("Use Auto", useAuto);
    }

    public void shootClose() {
        targetRPM = closeRPM;
    }

    public void shootMid() {
        targetRPM = midRPM;
    }

    public void shootFar() {
        targetRPM = farRPM;
    }

    public void shootFarAuto() {
        targetRPM = 2000;
    }

    public void setTargetRPM(double rpm) {
        targetRPM = rpm;
    }

    public void shootAutoRPM() {
        targetRPM = autoRPM;
    }

    public void reverseWheel() {
        shooterMotor.setPower(-0.1);
        shooterOn = false;
    }

    public void toggleShooter() {
        shooterOn = !shooterOn;
    }

    public void stopShooter() {
        shooterMotor.setPower(0);
        shooterOn = false;
    }

    public void enableShooter() {
        shooterOn = true;
    }

    public void setHoodServo(double angle) {
        hoodServo.setPosition(angle);
    }

    public void setFullPower() {
        shooterMotor.setPower(1);
    }

    public void setLight() {
        if (atSpeed()) {
            light.setPosition(0.5);
        } else {
            light.setPosition(0.277);
        }
    }

    public boolean atSpeed() {
        return Math.abs(currentRPM - targetRPM) < rpmTolerance;
    }

    public void updateDistance(double dist) {
        distance = Math.max(Math.min(dist, 148), 1);
    }

    public void updateAutoRPM() {
        autoRPM = (regA * distance * distance) + (regB * distance) + regC;
    }

    public void hoodUp() {
        hoodServo.setPosition(0.8);
    }

    public void setHood(double d) {
        hoodServo.setPosition(d);
    }

    private void autoHood() {
        double dist = Math.max(Math.min(distance, 300), 0);
        hoodAngle = hoodTable.get(dist);
    }

    public void toggleAuto() {
        useAuto = !useAuto;
    }

    public void toggleIdlePower() {
        idlePowerEnabled = !idlePowerEnabled;
    }

    public void enableIdlePower() {
        idlePowerEnabled = true;
    }

    public void disableIdlePower() {
        idlePowerEnabled = false;
    }

    public boolean isIdlePowerEnabled() {
        return idlePowerEnabled;
    }

    public double getDistanceFromLimelight() {
        if (limelight == null || !limelight.hasTarget()) {
            return 72.0;
        }

        double ty = limelight.getTy();
        double angleRad = Math.toRadians(cameraAngle + ty);

        return (tagHeight - cameraHeight) / Math.tan(angleRad);
    }

    public void updateDistanceFromLimelight() {
        distance = getDistanceFromLimelight();
        distance = Math.max(Math.min(distance, 148), 1);
    }
}