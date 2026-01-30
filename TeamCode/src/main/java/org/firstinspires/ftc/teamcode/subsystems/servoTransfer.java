package org.firstinspires.ftc.teamcode.subsystems;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.ServoEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.constants.transferConstants;


public class servoTransfer extends SubsystemBase {
    private ServoEx pitchServo;
    private  ServoEx yawServo;
    private RevColorSensorV3 transferSensor;

    private boolean kickerExtended;
    private boolean gateOpen;

    private Telemetry telemetry;

    public servoTransfer(HardwareMap hmap, Telemetry telemetry) {
        pitchServo = new ServoEx(hmap, transferConstants.pitchServoID);
        yawServo = new ServoEx(hmap, transferConstants.yawServoID);
        transferSensor = hmap.get(RevColorSensorV3.class, "transferSensor");

        yawServo.setInverted(false);

        kickerExtended = false;
        gateOpen = true;

        this.telemetry = telemetry;
    }

    public void init() {
        pitchServo.set(0.00);
        yawServo.set(0);

         gateOpen = true;
         kickerExtended = false;
    }

    public void extendPitch() {
        pitchServo.set(0.70);
        kickerExtended = true;
    }

    public void openGate() {
        yawServo.set(0.0);
        gateOpen = true;
    }

    public void closeGate() {
        yawServo.set(0.35);
        gateOpen = false;
    }

    public void retractPitch() {
        pitchServo.set(0.00);
        kickerExtended = false;
    }

    public boolean isGateOpen() {
        return gateOpen;
    }

    public boolean getKickerState () {
        return kickerExtended;
    }

    //Sensor
    public double getSensorDistance() {
        return transferSensor.getDistance(DistanceUnit.INCH);
    }

    public boolean artifactDetected() {
        return transferSensor.getDistance(DistanceUnit.INCH) < 1.25;
    }

    public void autoKick() {
        if(artifactDetected()) {
            extendPitch();
        }
        else {
            retractPitch();
        }
    }

    @Override
    public void periodic() {
        telemetry.addData(("Is the kicker Extended: "), kickerExtended);
        telemetry.addData(("Gate Open"), gateOpen);
        telemetry.addData(("transferSensor Distance: "), getSensorDistance());
        telemetry.addData(("Artifact Detected: "), artifactDetected());
    }
}
