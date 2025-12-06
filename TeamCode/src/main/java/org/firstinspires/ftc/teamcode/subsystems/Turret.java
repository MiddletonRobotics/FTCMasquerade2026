package org.firstinspires.ftc.teamcode.subsystems;

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

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.constants.TurretConstants;

public class Turret extends SubsystemBase {
    private DcMotorEx turretMotor;
    private DigitalChannel leftHomingSwitch;
    private Limelight3A limelight;

    private List<LLResultTypes.FiducialResult> tagList;

    public enum SystemState {
        IDLE,
        HOME,
        FINDING_POSITION,
        RELOCALIZING,
        TARGET_POSITION,
        MANUAL
    }

    public enum WantedState {
        IDLE,
        HOME,
        FINDING_POSITION,
        RELOCALIZING,
        TARGET_POSITION,
        MANUAL
    }

    private PIDFController positionController;
    private SimpleMotorFeedforward frictionController;

    private WantedState wantedState = WantedState.IDLE;
    private SystemState systemState = SystemState.IDLE;

    private String Alliance;

    public static Turret instance;

    private Telemetry telemetry;

    private double tx;
    private boolean hasTarget;

    /** ★ ADD: targetAngle field */
    private double targetAngle = 0;

    // Encoder constants
    private static final double MOTOR_CPR = 537.7;
    private static final double GEAR_RATIO = 174.0 / 36.0;
    private static final double TURRET_CPR = MOTOR_CPR * GEAR_RATIO;
    private static final double COUNTS_PER_DEGREE = TURRET_CPR / 360.0;

    public static synchronized Turret getInstance(HardwareMap hMap, Telemetry telemetry, String Alliance) {
        if(instance == null) {
            instance = new Turret(hMap, telemetry, Alliance);
        }
        return instance;
    }

    public Turret(HardwareMap hMap, Telemetry telemetry, String Alliance) {
        turretMotor = hMap.get(DcMotorEx.class, TurretConstants.turretMotorID);
        turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        turretMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        turretMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        limelight = hMap.get(Limelight3A.class, "limelight");

        positionController = new PIDFController(
                TurretConstants.kp,
                TurretConstants.ki,
                TurretConstants.kd,
                TurretConstants.kf
        );
        frictionController = new SimpleMotorFeedforward(0, 0, 0);

        this.telemetry = telemetry;

        this.Alliance = Alliance.toUpperCase();

        tagList=limelight.getLatestResult().getFiducialResults();
    }

    public void startLimelight() {
        limelight.start();
        limelight.setPollRateHz(50);
    }

    public void stopLimelight() {
        limelight.stop();
    }

    public void initPos() {

    }

    @Override
    public void periodic() {
        positionController.setPIDF(
                TurretConstants.kp,
                TurretConstants.ki,
                TurretConstants.kd,
                TurretConstants.kf
        );

        updateLimelightData();

        systemState = handleTransition();
        applyStates();

        LLResultTypes.FiducialResult currentTag = getTag();

        telemetry.addData(("Has Target: "), hasTarget);

        /** ★ ADD: telemetry for angles */
        telemetry.addData("Turret Angle", getAngle());
        telemetry.addData("Target Angle", targetAngle);
        telemetry.addData("Wrapped Target", wrapAngle(targetAngle));
        telemetry.addData("Encoder Position", turretMotor.getCurrentPosition());
        telemetry.addData("tx", tx);
        telemetry.addData("Wanted State", wantedState);
        telemetry.addData("System State", systemState);

        if (currentTag != null) {
            telemetry.addData("Detected Tag ID", currentTag.getFiducialId());
        } else {
            telemetry.addData("Detected Tag ID", "None");
        }
    }

    private void updateLimelightData() {
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();

            if (!fiducialResults.isEmpty()) {
                tx = result.getTx();
                hasTarget = true;
            } else {
                tx = result.getTx();
                hasTarget = (Math.abs(tx) > 0.1);
            }
        } else {
            tx = 0;
            hasTarget = false;
        }
    }

    private SystemState handleTransition() {
        double angle = getAngle();

        if (angle > 360 && systemState != SystemState.RELOCALIZING) {
            return SystemState.RELOCALIZING;
        }

        switch(wantedState) {
            case IDLE:
                return SystemState.IDLE;
            case HOME:
                return SystemState.HOME;
            case FINDING_POSITION:
                return SystemState.FINDING_POSITION;
            case RELOCALIZING:
                return SystemState.RELOCALIZING;
            case TARGET_POSITION:
                return SystemState.TARGET_POSITION;
            case MANUAL:
                return SystemState.MANUAL;
        }

        return systemState;
    }

    public void applyStates() {
        switch(systemState) {
            case IDLE:
                turretMotor.setPower(0);
                break;

            case HOME:
            case FINDING_POSITION:
            case RELOCALIZING:
                /** ★ ADD: FINDING_POSITION uses goToSetpoint() instead of relocalize */
                if (systemState == SystemState.FINDING_POSITION) {
                    goToSetpoint();
                } else {
                    relocalize();
                }
                break;

            case TARGET_POSITION:
                if (hasTarget) {
                    aimWithVision();
                } else {
                    turretMotor.setPower(0);
                }
                break;

            case MANUAL:
                break;
        }
    }

    private void aimWithVision() {
        double error = tx;
        double power = -positionController.calculate(0, error);

        power = Math.max(-1.0, Math.min(1.0, power));

        turretMotor.setPower(power);
    }

    public void relocalize() {
        double currentAngle = getAngle();
        double target = 0;

        double power = -positionController.calculate(currentAngle, target);
        turretMotor.setPower(power);

        if (Math.abs(currentAngle - 0) < 2.0) {
            turretMotor.setPower(0);
            wantedState = WantedState.TARGET_POSITION;
        }
    }

    public void setManualPowerControl(double power) {
        wantedState = WantedState.MANUAL;
        turretMotor.setPower(power);
    }

    public void setLimelightData(double tx, boolean hasTarget) {
        this.tx = tx;
        this.hasTarget = hasTarget;
    }

    public void startTracking() {
        wantedState = WantedState.TARGET_POSITION;
    }

    public void stopTracking() {
        wantedState = WantedState.IDLE;
    }

    public void forceReturnToZero() {
        wantedState = WantedState.RELOCALIZING;
    }

    private double getAngle() {
        int encoderCounts = turretMotor.getCurrentPosition();
        return encoderCounts / COUNTS_PER_DEGREE;
    }

    public int getEncoderPosition() {
        return turretMotor.getCurrentPosition();
    }

    public double getCurrentAngle() {
        return getAngle();
    }

    public SystemState getSystemState() {
        return systemState;
    }

    public double getTx() {
        return tx;
    }

    public boolean hasTarget() {
        return hasTarget;
    }

    public LLResultTypes.FiducialResult getTag() {
        // Get the latest tag list every time this method is called
        tagList = limelight.getLatestResult().getFiducialResults();

        LLResultTypes.FiducialResult target = null;

        if (Alliance.equals("BLUE")) {
           limelight.pipelineSwitch(1);

        } else if ( Alliance.equals("RED")) {
            limelight.pipelineSwitch(0);
        }
        return target;
    }

    // ======================================================
    // ★★★★★ ADDED NEW FEATURES BELOW ★★★★★
    // ======================================================

    /** ★ Add: wrapAngle */
    private double wrapAngle(double angle) {
        if (angle > 90) return 90;
        if (angle < -90) return -90;
        return angle;
    }

    /** ★ Add: setAngle */
    public void setAngle(double angleDegrees) {
        angleDegrees = wrapAngle(angleDegrees);
        this.targetAngle = angleDegrees;
        this.wantedState = WantedState.FINDING_POSITION;
    }

    /** ★ Add: goToSetpoint */
    private void goToSetpoint() {
        double currentAngle = getAngle();
        double clampedTarget = wrapAngle(targetAngle);

        double power = positionController.calculate(currentAngle, clampedTarget);
        power = Math.max(-1, Math.min(1, power));

        turretMotor.setPower(power);

        if (Math.abs(clampedTarget - currentAngle) < 1.5) {
            turretMotor.setPower(0);
            wantedState = WantedState.IDLE;
        }
    }
}