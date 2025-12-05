package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Masquerade extends Robot {
    public Drivetrain dt;
    private Intake intake;
    private Shooter shooter;
    private Turret turret;
    private servoTransfer kicker;
    private GamepadEx driver1;
    private GamepadEx driver2;
    private Telemetry telemetry;
    private Timer timer;
    private String Alliance;

    public Masquerade(HardwareMap hmap, Telemetry telemetry, Gamepad d1, Gamepad d2, String Alliance) {
        dt = new Drivetrain(hmap, telemetry);
        intake = new Intake(hmap, telemetry);
        shooter = new Shooter(hmap, telemetry, Alliance);
        kicker = new servoTransfer(hmap, telemetry);
        turret = Turret.getInstance(hmap, telemetry, Alliance);
        driver1 = new GamepadEx(d1);
        driver2 = new GamepadEx(d2);
        this.Alliance = Alliance.toUpperCase();
        this.telemetry = telemetry;
        timer = new Timer();
        register(dt, intake, shooter, turret, kicker);
    }

    public void Periodic() {
        dt.periodic();
        intake.periodic();
        shooter.periodic();
        turret.periodic();
        telemetry.update();
    }

    @Override
    public void reset() {
        shooter.disableFlyWheel();
        turret.stopTracking();
        intake.stopIntake();
        kicker.init();
        dt.resetHeading();
        dt.follower.setStartingPose(dt.follower.getPose());
    }

    public void start() {
        dt.follower.setMaxPower(0.85);
        dt.follower.startTeleopDrive();

        turret.startLimelight();
        turret.startTracking();
    }

    public void controlMap() {
        dt.setMovementVectors(driver1.getLeftX(), driver1.getLeftY(), driver1.getRightX(), false);

        if(driver1.getButton(GamepadKeys.Button.TOUCHPAD)) {
            dt.resetHeading();
        }


        if(driver1.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            //Add Wrapping or Zeroing Method
            turret.stopTracking();
            turret.stopLimelight();
        }

        if(driver1.getButton(GamepadKeys.Button.DPAD_UP)) {
            turret.startLimelight();
            turret.startTracking();
        }

        if(driver1.getButton(GamepadKeys.Button.A)) {
            intake.intake();
        }

        else if(driver1.getButton(GamepadKeys.Button.B)) {
            intake.outtake();
        }

        else {
            intake.stopIntake();
        }

        if(driver1.getButton(GamepadKeys.Button.Y)) {
            shooter.setHoodServoPos(0.0);
        }

        else if (driver1.getButton(GamepadKeys.Button.X)) {
            shooter.setHoodServoPos(0.55);
        }

        //Driver 2 Controls

        //Shooter Speeds

        if (driver2.getButton(GamepadKeys.Button.DPAD_LEFT)) {
            shooter.shootClose();
        }

        if (driver2.getButton(GamepadKeys.Button.DPAD_RIGHT)) {
            shooter.shootFar();
        }

        if (driver2.getButton(GamepadKeys.Button.DPAD_UP)) {
            shooter.shootMid();
        }

        if (driver2.getButton(GamepadKeys.Button.DPAD_DOWN)) {
            shooter.stopMotor();
        }

        //Toggle FlyWheel

        if (driver2.getButton(GamepadKeys.Button.Y)) {
            shooter.disableFlyWheel();
        }

        if (driver2.getButton(GamepadKeys.Button.X)) {
            shooter.enableFlyWheel();
        }

        //Servo Flick

        if (driver2.getButton(GamepadKeys.Button.B)) {
            kicker.extendPitch();
        }

        if (driver2.getButton(GamepadKeys.Button.A)) {
            kicker.retractPitch();
        }
    }
}
