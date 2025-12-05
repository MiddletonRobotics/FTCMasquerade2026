package org.firstinspires.ftc.teamcode.OpModes.Auto.RED;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.constants.DrivetrainConstants;
import org.firstinspires.ftc.teamcode.constants.ShooterConstants;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;


@Autonomous(name= "Far Shoot Auto Red", group = "First Comp")
public class Red3Ball extends LinearOpMode {

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
            shooter.setPower(0.80);
            //hoodServo.setPosition(0.);
            while (timer.getElapsedTimeSeconds() < 1.5) {
                leftFront.setPower(-0.5);
                rightFront.setPower(-0.5);
                leftBack.setPower(-0.5);
                rightBack.setPower(-0.5);
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


    }}

