package frc.team568.robot.rapidreact;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

public class ChargeAndScore extends SequentialCommandGroup {
	ChargeAndScore(MecanumSubsystem drive, Intake intake, AutonomousParameters param){
		this(drive, intake, param.chargeTime());
	}
	
	ChargeAndScore(MecanumSubsystem drive, Intake intake, double chrageTime){
		this(drive, intake, chrageTime, new BooleanSupplier() {
			private BuiltInAccelerometer accel = new BuiltInAccelerometer();

			public boolean getAsBoolean() {
				return accel.getZ() > 0.5;
			}
		});
	}

	ChargeAndScore(MecanumSubsystem drive, Intake intake){
		this(drive, intake, AutonomousParameters.CHARGE_TIME_DEF, new BooleanSupplier() {
			private BuiltInAccelerometer accel = new BuiltInAccelerometer();

			public boolean getAsBoolean() {
				return accel.getZ() > 0.5;
			}
		});
	}

	ChargeAndScore(MecanumSubsystem drive, Intake intake, double chargeTime, BooleanSupplier scoreTrigger){
		addRequirements(drive, intake);
		addCommands(
			intake.commandOpenLid(),
			new WaitUntilCommand(scoreTrigger).withTimeout(chargeTime).deadlineWith(
				new RunCommand(() -> intake.setIntakeMotor(-0.7)),
				new RunCommand(() -> drive.driveStraight(0.7))
			),
			intake.commandCloseLid()
		);
	}
}
