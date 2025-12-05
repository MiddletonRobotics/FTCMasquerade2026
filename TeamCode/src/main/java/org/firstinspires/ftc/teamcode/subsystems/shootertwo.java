//package org.firstinspires.ftc.teamcode.subsystems;
//
//import com.qualcomm.hardware.limelightvision.LLResult;
//import com.qualcomm.hardware.limelightvision.LLResultTypes;
//import com.qualcomm.hardware.limelightvision.Limelight3A;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorEx;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.seattlesolvers.solverslib.command.SubsystemBase;
//import com.seattlesolvers.solverslib.hardware.ServoEx;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
//import org.firstinspires.ftc.teamcode.constants.ShooterConstants;
//
//import java.util.List;
//import java.util.TreeMap;
//
//public class Shooter extends SubsystemBase {
//
//    private final ServoEx hoodServo;
//    private final DcMotorEx shooterMotor;
//    private final Limelight3A limelight;
//    private final Telemetry telemetry;
//
//    private boolean flywheelEnabled = false;
//    private double targetVelocity = 0;
//    private double currentVelocity = 0;
//
//    // Reference the constants table instead of storing locally
//    private final TreeMap<Double, Double> distanceToRPM = ShooterConstants.distanceToRPM;
//
//    public Shooter(HardwareMap hmap, Telemetry telemetry) {
//        this.telemetry = telemetry;
//
//        hoodServo = new ServoEx(hmap, ShooterConstants.hoodServoID);
//        shooterMotor = hmap.get(DcMotorEx.class, ShooterConstants.shooterMotorID);
//        limelight = hmap.get(Limelight3A.class, "limelight");
//
//        // Motor setup
//        shooterMotor.setDirection(DcMotorSimple.Direction.REVERSE);
//        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
//        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//
//        shooterMotor.setVelocityPIDFCoefficients(
//                ShooterConstants.kp,
//                ShooterConstants.ki,
//                ShooterConstants.kd,
//                ShooterConstants.kf
//        );
//    }
//
//    public void setPipeline(int pipeline) {
//        limelight.pipelineSwitch(pipeline);
//    }
//
//    // ----------------------------- LIMELIGHT DISTANCE --------------------------------
//    public double getDistanceMeters() {
//        LLResult result = limelight.getLatestResult();
//        if (result == null || !result.isValid()) return -1;
//
//        List<LLResultTypes.FiducialResult> tags = result.getFiducialResults();
//        if (tags.isEmpty()) return -1;
//
//        LLResultTypes.FiducialResult tag = tags.get(0);
//
//        // FIXED: This returns Pose3D, not double[]
//        Pose3D pose = tag.getTargetPoseCameraSpace();
//        if (pose == null) return -1;
//
//        // Extract XYZ in meters
//        double x = pose.getPosition().x;   // forward distance
//        double y = pose.getPosition().y;   // sideways distance
//        double z = pose.getPosition().z;   // vertical difference
//
//        // Full 3D distance:
//        return Math.sqrt(x * x + y * y + z * z);
//    }
//
//    // ----------------------------- DISTANCE → RPM INTERPOLATION -----------------------
//    public double getInterpolatedRPM(double dist) {
//        if (dist <= 0) return 0;
//
//        Double low = distanceToRPM.floorKey(dist);
//        Double high = distanceToRPM.ceilingKey(dist);
//
//        if (low == null) return distanceToRPM.firstEntry().getValue();
//        if (high == null) return distanceToRPM.lastEntry().getValue();
//        if (low.equals(high)) return distanceToRPM.get(low);
//
//        double lowRPM = distanceToRPM.get(low);
//        double highRPM = distanceToRPM.get(high);
//        double t = (dist - low) / (high - low);
//
//        return lowRPM + t * (highRPM - lowRPM);
//    }
//
//    // ----------------------------- PERIODIC LOOP --------------------------------------
//    @Override
//    public void periodic() {
//        if (flywheelEnabled)
//            shooterMotor.setVelocity(targetVelocity);
//        else
//            shooterMotor.setVelocity(0);
//
//        currentVelocity = shooterMotor.getVelocity();
//
//        telemetry.addData("Shooter Target", targetVelocity);
//        telemetry.addData("Shooter Current", currentVelocity);
//
//        double dist = getDistanceMeters();
//        telemetry.addData("Distance (m)", dist);
//
//        telemetry.update();
//    }
//
//    // ----------------------------- SHOOTER CONTROL ------------------------------------
//    public void enableFlywheel()  { flywheelEnabled = true; }
//    public void disableFlywheel() { flywheelEnabled = false; }
//
//    public void setTargetVelocity(double rpm) {
//        targetVelocity = rpm;
//        enableFlywheel();
//    }
//
//    public void autoTuneRPM() {
//        double dist = getDistanceMeters();
//        if (dist > 0) {
//            targetVelocity = getInterpolatedRPM(dist);
//            flywheelEnabled = true;
//        }
//    }
//
//    public double getVelocity() {
//        return shooterMotor.getVelocity();
//    }
//
//    // ----------------------------- PRESET SHOTS ---------------------------------------
//    public void shootClose() { setTargetVelocity(ShooterConstants.shootClose); }
//    public void shootMid()   { setTargetVelocity(ShooterConstants.shootMid); }
//    public void shootFar()   { setTargetVelocity(ShooterConstants.shootFar); }
//
//    // ----------------------------- HOOD CONTROL ----------------------------------------
//    public void setHoodInit() { hoodServo.set(ShooterConstants.hoodInit); }
//    public void setHoodMid()  { hoodServo.set(ShooterConstants.hoodMid); }
//    public void setHoodMax()  { hoodServo.set(ShooterConstants.hoodMax); }
//}
