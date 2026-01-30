package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.util.Timer;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.ServoEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.CRServo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class servoTilt extends SubsystemBase {
    private CRServo tiltServo;
    private Timer timer;
    private Telemetry telemetry;

    public servoTilt(HardwareMap hmap, Telemetry telemetry) {
       tiltServo = new CRServo(hmap, "tiltServo");
       tiltServo.setInverted(true);
       timer = new Timer();
       timer.resetTimer();
    }

    public void setServoSpeed(double power) {
        tiltServo.set(power);
    }

    public void stopServo() {
        tiltServo.set(0);
    }
}
