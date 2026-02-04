package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.GlobalConstants;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

public class Masquerade extends Robot {
    public Drivetrain dt;
    private Intake intake;
    public Shooter shooter;
    public Turret turret;
    public servoTilt endGame;
    public servoTransfer transfer;
    private GamepadEx driver1;
    private GamepadEx driver2;
    private Telemetry telemetry;
    private Timer timer;
    private String Alliance;

    private PathChain redPark;
    private PathChain bluePark;
    private static Pose blueZone= new Pose(105.3,33,Math.toRadians(0));
    private static Pose redZone= blueZone.mirror();

    // Transfer state machine
    // -1 = idle
    //  1 = open gate + wait
    //  2 = feed intake for a bit
    //  3 = flick (extend pitch)
    //  4 = finish (retract/close/stop) then idle
    private int transferState = -1;

    // prevents "holding Y" from constantly restarting the state machine
    private boolean rapidRequested = false;

    public Masquerade(HardwareMap hmap, Telemetry telemetry, Gamepad d1, Gamepad d2, String Alliance) {
        dt = new Drivetrain(hmap, telemetry);
        intake = new Intake(hmap, telemetry);
        shooter = new Shooter(hmap, telemetry, Alliance);
        transfer = new servoTransfer(hmap, telemetry);
        turret = new Turret(hmap, telemetry, Alliance);
        endGame = new servoTilt(hmap, telemetry);

        driver1 = new GamepadEx(d1);
        driver2 = new GamepadEx(d2);

        this.Alliance = Alliance.toUpperCase();
        this.telemetry = telemetry;

        timer = new Timer();
        register(dt, intake, shooter, turret, transfer);

        setState(-1);
    }

    public void Periodic() {
        dt.periodic();
        intake.periodic();
        transfer.periodic();
        shooter.periodic();

//        turret.setPosition(turret.computeAngle(dt.follower.getPose(), turret.getTargetPose(GlobalConstants.allianceColor), 0, 0)); // set turret offset here

        // run transfer state machine every loop
        rapidFire();
        turret.periodic();
        telemetry.addData("Compute Angle", turret.computeAngle(dt.follower.getPose(), turret.getTargetPose(GlobalConstants.allianceColor), 0, 0));
        telemetry.update();
    }

    @Override
    public void reset() {
        shooter.disableFlyWheel();
        turret.setManualPower(0.0);
        intake.stopIntake();
        transfer.init();
        dt.resetHeading();
        dt.follower.setStartingPose(dt.follower.getPose());

        //resetRapid();
    }

    public void start() {
        dt.follower.setMaxPower(0.9);
        dt.follower.startTeleopDrive();
    }

    public void controlMap() {
        dt.setMovementVectors(driver1.getLeftX(), driver1.getLeftY(), driver1.getRightX(), false);

        if (driver1.getButton(GamepadKeys.Button.TOUCHPAD)) {
            dt.resetHeading();
        }

        // ----------------------
        // Driver 1: Intake manual
        // ----------------------
        if (driver1.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.5) {
            intake.outtake();
        } else if (driver1.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5) {
            intake.intake();
        } else {
            // don't fight the state machine
            if (transferState == -1) {
                intake.stopIntake();
            }
        }

        if(driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0) {
            endGame.setServoSpeed(1);
        }

        else if (driver2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0) {
            endGame.setServoSpeed(-1);
        }

        else {
            endGame.stopServo();
        }


        boolean yNow = driver1.getButton(GamepadKeys.Button.Y);
        if (yNow && !rapidRequested && transferState == -1) {
            startTransfer();
        }
        rapidRequested = yNow;

        if (driver1.getButton(GamepadKeys.Button.X)) {
            resetRapid();
        }

        // ----------------------
        // Driver 2: Shooter speed + flywheel
        // ----------------------
        if (driver2.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            shooter.shootClose();
            shooter.setHoodServoPos(0.0);
        }
        if (driver2.getButton(GamepadKeys.Button.DPAD_RIGHT)) {
            shooter.shootFar();
        }
        if (driver2.getButton(GamepadKeys.Button.DPAD_UP)) {
            shooter.shootMid();
            shooter.setHoodServoPos(0.42);
        }

        if (driver2.getButton(GamepadKeys.Button.X)) {
            shooter.enableFlyWheel();
        }

//        if(driver2.getButton(GamepadKeys.Button.B)) {
//            park();
//        }

        // ----------------------
        // Driver 2: Transfer manual controls (ONLY when idle)
        // ----------------------
        if (transferState == -1) {
            // Manual flick: only command extend when trigger pressed;
            // don't constantly retract every loop unless you want that behavior.
            if (driver2.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0) {
                transfer.extendPitch();
            }

            if(driver2.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0){
                transfer.retractPitch();
            }

            if (driver2.getButton(GamepadKeys.Button.A)) {
                transfer.closeGate();
            }
            if (driver2.getButton(GamepadKeys.Button.B)) {
                transfer.openGate();
            }
        }
    }

    private void setState(int x) {
        transferState = x;
        timer.resetTimer();
    }

    private void startTransfer() {
        // start from a known safe state
        transfer.retractPitch();
        transfer.openGate();
        intake.stopIntake();
        setState(1);
    }

    public void resetRapid() {
        intake.stopIntake();
        transfer.retractPitch();
        transfer.closeGate();
        setState(-1);
    }

    public void park () {
        if(Alliance.equals("BLUE"))
        {
            bluePark = dt.follower.pathBuilder().addPath(new BezierLine(dt.follower.getPose(),blueZone)).setConstantHeadingInterpolation(0).build();
            if(!dt.follower.isBusy())
            {
                dt.follower.followPath(bluePark,true);
            }
        }
        else if(Alliance.equals("RED"))
        {
            redPark= dt.follower.pathBuilder().addPath(new BezierLine(dt.follower.getPose(),redZone)).setConstantHeadingInterpolation(0).build();
            if(!dt.follower.isBusy())
            {
                dt.follower.followPath(redPark,true);
            }
        }
    }

    public void rapidFire() {
        switch (transferState) {

            case 1:
                // Ensure gate is open, then wait before feeding
                transfer.openGate();
                if (timer.getElapsedTimeSeconds() > 0.1) {
                    setState(2);
                }
                break;

            case 2:
                intake.intake();
                if (timer.getElapsedTimeSeconds() > 0.5) {
                    setState(3);
                }
                break;

            case 3:
                // Flick shot
                transfer.extendPitch();
                if (timer.getElapsedTimeSeconds() > .5) {
                    setState(4);
                }
                break;

            case 4:
                // Reset mechanisms and finish
                transfer.retractPitch();
                transfer.closeGate();
                intake.stopIntake();

                // Done (single shot). If you want continuous rapid-fire,
                // change this to setState(2) or setState(1).
                setState(-1);
                break;

            default:
                // idle
                break;
        }
    }
}
