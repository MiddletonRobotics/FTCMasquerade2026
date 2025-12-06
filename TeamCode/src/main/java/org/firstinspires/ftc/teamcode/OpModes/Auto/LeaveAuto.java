package org.firstinspires.ftc.teamcode.OpModes.Auto;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.constants.DrivetrainConstants;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.hardware.ServoEx;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;


import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@Autonomous(name="Leave Auto", group = "First Comp")
public class LeaveAuto extends LinearOpMode {

    private DcMotorEx leftFront;
    private DcMotorEx rightFront;
    private DcMotorEx leftBack;
    private DcMotorEx rightBack;

    private DcMotorEx shooter;

    private Timer timer;

    private Intake intake;

    private Servo hoodServo;
    private Servo stupidServo;

    @Override
    public void runOpMode() {
        leftFront = hardwareMap.get(DcMotorEx.class, DrivetrainConstants.fLMotorID);
        rightFront = hardwareMap.get(DcMotorEx.class, DrivetrainConstants.fRMotorID);
        leftBack = hardwareMap.get(DcMotorEx.class, DrivetrainConstants.bLMotorID);
        rightBack = hardwareMap.get(DcMotorEx.class, DrivetrainConstants.bRMotorID);
        shooter = hardwareMap.get(DcMotorEx.class, ShooterConstants.shooterMotorID);
        intake = new Intake(hardwareMap, telemetry);
        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
        stupidServo = hardwareMap.get(Servo.class, "pitchServo");

        servoTransfer transfer = new servoTransfer(hardwareMap, telemetry);

        timer = new Timer();

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);

        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        if (opModeIsActive()) {
            timer.resetTimer();
            //shooter.setPower(0.80);
            //hoodServo.setPosition(0.);
            while (timer.getElapsedTimeSeconds() < 1.5) {
                leftFront.setPower(0.5);
                rightFront.setPower(0.5);
                leftBack.setPower(0.5);
                rightBack.setPower(0.5);
            }
            timer.resetTimer();
            leftFront.setPower(0.0);
            rightFront.setPower(0.0);
            leftBack.setPower(0.0);
            rightBack.setPower(0.0);
            while (timer.getElapsedTimeSeconds() < 3) {

            }
            timer.resetTimer();
            while (timer.getElapsedTimeSeconds() < 3) {
                intake.setPower(0.90);
            }

            timer.resetTimer();

            intake.setPower(0);
            stupidServo.setPosition(0.60);
//
            timer.resetTimer();
            while (timer.getElapsedTimeSeconds() < 0.5) {
                leftFront.setPower(1);
                rightFront.setPower(-1);
                leftBack.setPower(-1);
                rightBack.setPower(1);
            }

            shooter.setPower(0);
        }
    }
}

