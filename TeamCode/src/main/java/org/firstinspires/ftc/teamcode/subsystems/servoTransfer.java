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
    private RevColorSensorV3 transfer2;

    private boolean kickerExtended;
    private boolean gateOpen;

    private Telemetry telemetry;

    public servoTransfer(HardwareMap hmap, Telemetry telemetry) {
        pitchServo = new ServoEx(hmap, transferConstants.pitchServoID);
        yawServo = new ServoEx(hmap, transferConstants.yawServoID);

        transferSensor = hmap.get(RevColorSensorV3.class, "transferSensor");
        transfer2 = hmap.get(RevColorSensorV3.class, "transferSensor2");

        yawServo.setInverted(false);

        kickerExtended = false;
        gateOpen = true;

        this.telemetry = telemetry;

        transferSensor.enableLed(false);
    }

    //Sensor
    public boolean artifactDetected() {
        return transferSensor.getDistance(DistanceUnit.INCH) < 2;
    }

    public boolean transferDetect2() {
        return transfer2.getDistance(DistanceUnit.INCH) < 2;
    }

    public double transferDistance() {
        return transfer2.getDistance(DistanceUnit.INCH);
    }

    public double returnDistance() {
        return transferSensor.getDistance(DistanceUnit.INCH);
    }

    public void AutoGate() {
        if((transfer2.getDistance(DistanceUnit.INCH) < 6 || (transferSensor.getDistance(DistanceUnit.INCH)) < 6)){
            closeGate();
        }

        else {
            openGate();
        }

    }


    public void init() {
        pitchServo.set(0.00);
        yawServo.set(0);

         gateOpen = true;
         kickerExtended = false;
    }

    public void extendPitch() {
        pitchServo.set(0.65);
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

    @Override
    public void periodic() {
        telemetry.addData(("Is the kicker Extended: "), kickerExtended);
        telemetry.addData(("Gate Open"), gateOpen);
        telemetry.addData("Artificat Detected", artifactDetected());
        telemetry.addData("Transfer Distance", returnDistance());
        telemetry.addData("transferDistance 2", transferDistance());
        telemetry.addData("transfer 2 Detection", transferDetect2());
    }
}
