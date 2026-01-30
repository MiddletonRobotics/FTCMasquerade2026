package org.firstinspires.ftc.teamcode.Commands.TELEOP;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;
public class shootAutomation extends SequentialCommandGroup {
    private Shooter shooter;
    private Intake intake;
    private servoTransfer transfer;

    public shootAutomation(Intake intake, Shooter shooter, servoTransfer transfer) {
        this.intake = intake;
        this.shooter = shooter;
        this.transfer = transfer;

        addRequirements(intake,shooter,transfer);

        addCommands(
                new InstantCommand(shooter::enableFlyWheel),
                new InstantCommand(transfer::openGate).alongWith(new InstantCommand(shooter::shootMid)),
                new InstantCommand(intake::intake).andThen(new WaitCommand(1200)),
                new InstantCommand(transfer::extendPitch),
                new WaitCommand(500).andThen(new InstantCommand(transfer::retractPitch)),
                new InstantCommand(intake::stopIntake).alongWith(new InstantCommand(shooter::disableFlyWheel)),
                new InstantCommand(transfer::closeGate)
        );
    }
}
