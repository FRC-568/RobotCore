package frc.team568.robot.rapidreact;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class ChargeAndScore extends SequentialCommandGroup {
	ChargeAndScore(MecanumSubsystem drive, Intake intake, AutonomousParameters param, BuiltInAccelerometer accel){
		this(drive, intake, param.chargeTime(), accel);
	}
	
	// ChargeAndScore(MecanumSubsystem drive, Intake intake, double chrageTime, BuiltInAccelerometer accel){
	// 	this(drive, intake, chrageTime, new BooleanSupplier() {
	// 		public boolean getAsBoolean() {
	// 			return accel.getZ() > 0.5;
	// 		}
	// 	});
	// }

	ChargeAndScore(MecanumSubsystem drive, Intake intake, double chargeTime, BuiltInAccelerometer accel){
		addRequirements(drive, intake);
		addCommands(
			intake.commandOpenLid(),
			new ParallelCommandGroup(
				new InstantCommand(() -> intake.setIntakeMotor(-0.7)),
				new RunCommand(() -> drive.driveStraight(0.7))
						.withTimeout(chargeTime),
		new WaitUntilCommand(() -> hit(accel)).andThen(new InstantCommand(() -> intake.setLidOpen(false))),
			intake.commandCloseLid()
		));
	}

	public boolean hit(BuiltInAccelerometer accel) {
		// Assuming that negative acceleration is the hit.
		return accel.getZ() > 0.5;
	}
}
