package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.constants.IntakeConstants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake extends SubsystemBase {
    private MotorEx intakeMotor;
    private MotorEx transferMotor;
    private Motor.Encoder intakeEncoder;
    private RevColorSensorV3 colorSensor;

    private Telemetry telemetry;
    private Boolean intakeState;

    public Intake(HardwareMap hmap, Telemetry telemetry) {
        intakeMotor = new MotorEx(hmap, "intakeMotor", Motor.GoBILDA.RPM_435);
        transferMotor = new MotorEx(hmap, "transferMotor", Motor.GoBILDA.RPM_312);

        colorSensor = hmap.get(RevColorSensorV3.class, "intakeColorSensor");

        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);
        transferMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);

        intakeMotor.setRunMode(Motor.RunMode.RawPower);
        transferMotor.setRunMode(Motor.RunMode.RawPower);

        intakeMotor.setInverted(false);
        transferMotor.setInverted(false);

        this.telemetry = telemetry;
        intakeState = false;
    }

    public double getMotorVelocity() {
        return intakeMotor.getCorrectedVelocity();
    }

    public void setPower(double Power) {
        intakeMotor.set(Power);
        transferMotor.set(Power);
    }

    public void intake() {
        setPower(IntakeConstants.spinIn);
        intakeState = true;
    }

    public void outtake() {
        setPower(IntakeConstants.spinOut);
    }

    public void slowSpin() {
        setPower(IntakeConstants.spinSlow);
    }

    public void stopIntake() {
        setPower(IntakeConstants.spinIdle);
        intakeState = false;
    }

    public boolean getIntakeState() {
        return intakeState;
    }

    @Override
    public void periodic() {
        telemetry.addData(("Intake Motor Velocity"), getMotorVelocity());
        telemetry.addData(("Intake Motor Power"), intakeMotor.motor.getPower());
        telemetry.addData("Intake State: ", intakeState);
        telemetry.addData("Intake Sensor Distance", colorSensor.getDistance(DistanceUnit.INCH));
    }
}
