//package org.firstinspires.ftc.teamcode.Commands;
//
//import com.pedropathing.follower.Follower;
//import com.seattlesolvers.solverslib.command.InstantCommand;
//import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
//
//import org.firstinspires.ftc.teamcode.subsystems.Shooter;
//import org.firstinspires.ftc.teamcode.subsystems.Masquerade;
//import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
//import org.firstinspires.ftc.teamcode.subsystems.Intake;
//import org.firstinspires.ftc.teamcode.subsystems.Turret;
//import org.firstinspires.ftc.teamcode.subsystems.servoTransfer;
//
//public class shooterCommand extends SequentialCommandGroup {
//    private Intake intake;
//    private Shooter shooter;
//    private servoTransfer transfer;
//
//    public shooterCommand(Intake intake, Shooter shooter, servoTransfer transfer) {
//        this.intake = intake;
//        this.shooter = shooter;
//        this.transfer = transfer;
//
//        addRequirements(intake, shooter, transfer);
//
//        addCommands(
//                new InstantCommand()
//        );
//    }
//
//
//}
