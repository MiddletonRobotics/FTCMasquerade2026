package org.firstinspires.ftc.teamcode.Commands;

import com.seattlesolvers.solverslib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;
import com.pedropathing.util.Timer;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

public class ShootCommand extends CommandBase {
    private Shooter shooter;
    private servoTransfer transfer;
    private Intake intake;
    private Timer timer;
    private int stateVar;

    public ShootCommand(Shooter shooter, servoTransfer transfer, Intake intake) {
        this.shooter = shooter;
        this.transfer = transfer;
        this.intake = intake;
        timer = new Timer();
    }


    @Override
    public void initialize()
    {
        stateVar = 1;
        transfer.init();
        timer.resetTimer();
    }

    @Override
    public void execute() {
        switch (stateVar) {
            case 1:
                if(timer.getElapsedTimeSeconds() <= 1) {
                    intake.intake();
                }
                intake.stopIntake();
                setState(2);
                break;

            case 2:
                transfer.extendPitch();
                setState(3);
                timer.resetTimer();
                break;

            case 3:
                if(timer.getElapsedTimeSeconds() < .5) {
                    transfer.retractPitch();
                }
                setState(4);
                break;
        }
    }

    @Override
    public boolean isFinished()
    {
        return stateVar == 4;
    }

    public void setState(int state) {
        stateVar = state;
    }
}
