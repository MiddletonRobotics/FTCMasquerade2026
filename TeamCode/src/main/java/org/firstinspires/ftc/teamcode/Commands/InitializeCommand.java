package org.firstinspires.ftc.teamcode.Commands;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.Shooter;
import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.Turret;
import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;

public class InitializeCommand extends SequentialCommandGroup {
    private Shooter shooter;
    private Intake intake;
    private servoTransfer transfer;
    private Turret turret;
    private Drivetrain drivetrain;
    private Follower follower;

    public InitializeCommand(Intake intake, Shooter shooter, Drivetrain drivetrain, Turret turret, servoTransfer transfer) {
        this.intake = intake;
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        this.turret = turret;
        this.transfer = transfer;

        addRequirements(intake,shooter,transfer,turret,drivetrain);

        addCommands(
                new InstantCommand(shooter::enableFlyWheel),
                new InstantCommand(shooter::shootMid),
                new InstantCommand(() -> turret.setPosition(8)),
                new InstantCommand((transfer::init))
        );
    }
}
