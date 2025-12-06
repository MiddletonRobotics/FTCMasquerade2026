////package org.firstinspires.ftc.teamcode.subsystems;
////
////import com.bylazar.telemetry.TelemetryManager;
////import com.seattlesolvers.solverslib.command.SubsystemBase;
////
////import org.firstinspires.ftc.library.command.Command;
////import org.firstinspires.ftc.library.command.Commands;
////import org.firstinspires.ftc.library.command.type.InstantCommand;
////import org.firstinspires.ftc.teamcode.constants.GlobalConstants;
////
////import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
////import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
////import org.firstinspires.ftc.teamcode.subsystems.;
////import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
////
////public class Superstructure extends SubsystemBase {
////    private final Drivetrain drivetrain;
////    private final Intake intake;
////    private final Turret turret;
////    private final Shooter shooter;
////    private final TelemetryManager telemetryManager;
////
////    public enum WantedSuperState {
////        HOME,
////        STOPPED,
////        DEFAULT_STATE,
////        INTAKING_FROM_GROUND,
////        CLIMB
////    }
////
////    public enum CurrentSuperState {
////        HOME,
////        STOPPED,
////        NO_PIECE_TELEOP,
////        HOLDING_ARTIFACT_TELEOP,
////        NO_PIECE_AUTO,
////        HOLDING_ARTIFACT_AUTO,
////        INTAKING_FROM_GROUND,
////        CLIMB
////    }
////
////    private WantedSuperState wantedSuperState = WantedSuperState.STOPPED;
////    private CurrentSuperState currentSuperState = CurrentSuperState.STOPPED;
////    private CurrentSuperState previousSuperState;
////
////    public Superstructure(Drivetrain drivetrain, Intake intake, Turret turret, Shooter shooter, TelemetryManager telemetryManager) {
////        this.drivetrain = drivetrain;
////        this.intake = intake;
////        this.turret = turret;
////        this.shooter = shooter;
////        this.telemetryManager = telemetryManager;
////    }
////
////    @Override
////    public void periodic() {
////        telemetryManager.addData("Superstructure/WantedSuperState", wantedSuperState);
////        telemetryManager.addData("Superstructure/CurrentSuperState", currentSuperState);
////        telemetryManager.addData("Superstructure/PreviousSuperState", previousSuperState);
////
////        currentSuperState = handleStateTransitions();
////        applyStates();
////    }
////
////    private CurrentSuperState handleStateTransitions() {
////        previousSuperState = currentSuperState;
////        switch (wantedSuperState) {
////            default:
////                currentSuperState = CurrentSuperState.STOPPED;
////                break;
////            case HOME:
////                currentSuperState = CurrentSuperState.HOME;
////                break;
////            case DEFAULT_STATE:
////                if (intake.currentlyHoldingBalls()) {
////                    if (GlobalConstants.opModeType.equals(GlobalConstants.OpModeType.AUTONOMOUS)) {
////                        currentSuperState = CurrentSuperState.HOLDING_ARTIFACT_AUTO;
////                    } else {
////                        currentSuperState = CurrentSuperState.HOLDING_ARTIFACT_TELEOP;
////                    }
////                } else {
////                    if (GlobalConstants.opModeType.equals(GlobalConstants.OpModeType.AUTONOMOUS)) {
////                        currentSuperState = CurrentSuperState.NO_PIECE_AUTO;
////                    } else {
////                        currentSuperState = CurrentSuperState.NO_PIECE_TELEOP;
////                    }
////                }
////                break;
////            case INTAKING_FROM_GROUND:
////                currentSuperState = CurrentSuperState.INTAKING_FROM_GROUND;
////        }
////
////        return currentSuperState;
////    }
////
////    private void applyStates() {
////        switch (currentSuperState) {
////            case HOME:
////                home();
////            case INTAKING_FROM_GROUND:
////                intakeArtifactFromGround();
////        }
////    }
////
////    public void home() {
////        drivetrain.setWantedState(Drivetrain.WantedState.TELEOP_DRIVE);
////    }
////
////    public void intakeArtifactFromGround() {
////        drivetrain.setWantedState(Drivetrain.WantedState.TELEOP_DRIVE);
////        intake.setIntakeTargetRPM(400);
////    }
////
////    public void setWantedSuperState(WantedSuperState superState) {
////        this.wantedSuperState = superState;
////    }
////
////    public Command setStateCommand(WantedSuperState superState) {
////        return setStateCommand(superState, false);
////    }
////
////    public Command setStateCommand(WantedSuperState superState, boolean runIfClimberDeployed) {
////        Command commandToReturn = new InstantCommand(() -> setWantedSuperState(superState));
////        if (!runIfClimberDeployed) {
////            commandToReturn = commandToReturn.onlyIf(() -> currentSuperState != CurrentSuperState.CLIMB);
////        }
////        return commandToReturn;
////    }
////
////    public Command configureButtonBinding(WantedSuperState hasBallsCondition, WantedSuperState noBallsCondition) {
////        return Commands.either(
////                setStateCommand(hasBallsCondition),
////                setStateCommand(noBallsCondition),
////                intake::currentlyHoldingBalls
////        );
////    }
////}
//
//
//package org.firstinspires.ftc.teamcode.subsystems; import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.kf; import com.qualcomm.hardware.limelightvision.LLResult; import com.qualcomm.hardware.limelightvision.LLResultTypes; import com.qualcomm.hardware.limelightvision.Limelight3A; import com.qualcomm.robotcore.hardware.DcMotorSimple; import com.qualcomm.robotcore.hardware.HardwareMap; import com.seattlesolvers.solverslib.command.SubsystemBase; import com.seattlesolvers.solverslib.hardware.ServoEx; import org.firstinspires.ftc.robotcore.external.Telemetry; import org.firstinspires.ftc.teamcode.constants.ShooterConstants; import com.seattlesolvers.solverslib.controller.PIDFController; import com.seattlesolvers.solverslib.controller.wpilibcontroller.SimpleMotorFeedforward; import com.seattlesolvers.solverslib.hardware.motors.Motor; import com.seattlesolvers.solverslib.hardware.motors.MotorEx; import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.kp; import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.ki; import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.kd; import static org.firstinspires.ftc.teamcode.constants.ShooterConstants.shooterMotorID; import java.lang.Math; import java.util.List; public class Shooter extends SubsystemBase { private MotorEx shooterMotor; private ServoEx hoodServo; private ServoEx hoodServo2; private Limelight3A limelight; private Motor.Encoder shooterEncoder; private double currentMotorVelocity; private double currentEncoderVelocity; private double targetVelocity; private double currentDistance; private double Distance; private String Alliance; private double hoodPosition; private boolean flyWheelEnabled; private boolean trackingEnabled; private List<LLResultTypes.FiducialResult> tagList; LLResult result; LLResultTypes.FiducialResult currentTag; PIDFController motorController; SimpleMotorFeedforward FFController; private Telemetry telemetry; public Shooter(HardwareMap hmap, Telemetry telemetry, String Alliance) { shooterMotor = new MotorEx(hmap, shooterMotorID, Motor.GoBILDA.BARE); shooterMotor.setRunMode(Motor.RunMode.RawPower); shooterMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.FLOAT); shooterMotor.motor.setDirection(DcMotorSimple.Direction.REVERSE); shooterEncoder = shooterMotor.encoder; shooterEncoder.reset(); limelight = hmap.get(Limelight3A.class, "limelight"); limelight.pipelineSwitch(0); limelight.start(); limelight.setPollRateHz(50); hoodServo = new ServoEx(hmap, "hoodServo"); motorController = new PIDFController(kp, ki, kd, kf); FFController = new SimpleMotorFeedforward(0.01, 0.0, 0.0); hoodServo.set(0); flyWheelEnabled = false; this.Alliance = Alliance.toUpperCase(); flyWheelEnabled = true; targetVelocity = 100; //Default value tagList=limelight.getLatestResult().getFiducialResults(); Alliance.toUpperCase(); this.telemetry = telemetry; } @Override public void periodic() { //Current Shooter Velocity currentMotorVelocity = getMotorVelocity(); currentEncoderVelocity = getVelocity(); trackingEnabled = limelight.getLatestResult().isValid(); result = limelight.getLatestResult(); currentDistance = getCamDistance(); currentTag = getTag(); motorController.setPIDF(kp, ki, kd, kf); if(flyWheelEnabled) { setShooterVelocity(); } else { stopMotor(); } //Telemetry telemetry.addData(("Encoder Velocity: "), currentEncoderVelocity); telemetry.addData(("Motor Velocity: "), currentMotorVelocity); telemetry.addData(("Target RPM: "), targetVelocity); //telemetry.addData(("Current RPM"), cur) telemetry.addData(("Fly Wheel Enabled: "), flyWheelEnabled); telemetry.addData(("Servo Position: "), hoodServo.getRawPosition()); telemetry.addLine(""); telemetry.addLine("Limelight Values"); telemetry.addData(("Camera Distance: "), currentDistance); telemetry.addData(("Limelight tracking: "), trackingEnabled); telemetry.addData(("Tag ID: "), result.getFiducialResults()); telemetry.addData(("Alliance Color: "), Alliance); if (currentTag != null) { telemetry.addData("Detected Tag ID", currentTag.getFiducialId()); } else { telemetry.addData("Detected Tag ID", "None"); } telemetry.update(); } public double getVelocity() { return shooterEncoder.getCorrectedVelocity(); } public double getMotorVelocity() {return shooterMotor.getVelocity();} public void setMotorVelocity(Double RPM) { shooterMotor.setVelocity(RPM); } public void setMotorPower(Double POWER) { shooterMotor.motor.setPower(POWER); } public void setMax() { shooterMotor.set(1); } public void reverseMotor() { shooterMotor.motor.setPower(-0.20); } public void stopMotor() { shooterMotor.motor.setPower(0); } public void disableFlyWheel() { flyWheelEnabled = false; } public void enableFlyWheel() { targetVelocity = 100; flyWheelEnabled = true; } public void setShooterVelocity() { // shooterMotor.setVeloCoefficients(kp, ki, kd); motorController.setPIDF(kp, ki, kd, kf); shooterMotor.motor.setPower(-(motorController.calculate(currentMotorVelocity, targetVelocity))); } public void setServoPos() { hoodServo.set(0); if(currentTag != null) { if (currentDistance > 0 && currentDistance < 48) { hoodServo.set(0); } else if (currentDistance >= 48 && currentDistance < 84) { hoodServo.set(0.25); } else if (currentDistance > 100) { hoodServo.set(.70); } else { hoodServo.set(0); } } } //Manual Servo Control: public void setHoodServoPos(Double POS) { hoodServo.set(POS); } public double getCamDistance() { double LLAngle = 15; double LLHeight = 13.5; double aprilTagHeight = 29.5; double angleToGoalDegrees = LLAngle + limelight.getLatestResult().getTy(); double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0); return (aprilTagHeight - LLHeight) / Math.tan(angleToGoalRadians); } public LLResultTypes.FiducialResult getTag() { // Get the latest tag list every time this method is called tagList = limelight.getLatestResult().getFiducialResults(); LLResultTypes.FiducialResult target= null; if(Alliance.equals("BLUE")) { if (tagList!=null) { for(LLResultTypes.FiducialResult tar:tagList) { if(tar!=null && tar.getFiducialId() == 20) { target=tar; break; } } } } else if(Alliance.equals("RED")) { if(tagList!=null) { for(LLResultTypes.FiducialResult tar:tagList) { if(tar!=null && tar.getFiducialId() == 24) { target=tar; break; } } } } return target; } //Shooter Methods public void shootClose() { targetVelocity = ShooterConstants.closeShot; setShooterVelocity(); } public void shootMid() { targetVelocity = ShooterConstants.midShot; setShooterVelocity(); } public void shootFar() { targetVelocity = ShooterConstants.farShot; setShooterVelocity(); } public void idleRPM() { targetVelocity = 200; setShooterVelocity(); } public boolean isAtSpeed() { double error = Math.abs(targetVelocity - currentMotorVelocity); return error < 25; // <-- adjust tolerance as needed } } need you to fix the get tag method, I only track the red id which is 24 never blue
