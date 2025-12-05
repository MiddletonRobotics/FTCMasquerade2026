package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.constants.IntakeConstants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake extends SubsystemBase {
    private MotorEx intakeMotor;
    private Motor.Encoder intakeEncoder;
    private RevColorSensorV3 colorSensor;

    private Telemetry telemetry;

    public Intake(HardwareMap hmap, Telemetry telemetry) {
        intakeMotor = new MotorEx(hmap, "intakeMotor", Motor.GoBILDA.RPM_435);

        intakeMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT);

        intakeMotor.setRunMode(Motor.RunMode.RawPower);

        intakeMotor.setInverted(false);

        this.telemetry = telemetry;
    }

    public double getMotorVelocity() {
        return intakeMotor.getCorrectedVelocity();
    }

    public void setPower(double Power) {
        intakeMotor.set(Power);
    }

    public void intake() {
        setPower(IntakeConstants.spinIn);
    }

    public void outtake() {
        setPower(IntakeConstants.spinOut);
    }

    public void slowSpin() {
        setPower(IntakeConstants.spinSlow);
    }

    public void stopIntake() {
        setPower(IntakeConstants.spinIdle);
    }

    @Override
    public void periodic() {
        telemetry.addData(("Intake Motor Velocity"), getMotorVelocity());
        telemetry.addData(("Intake Motor Power"), intakeMotor.motor.getPower());
    }
}
