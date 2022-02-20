package frc.team568.robot.rapidreact;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.subsystems.DriveBase.Input;

public class MecanumSubsystemDefaultCommand extends CommandBase {
	final MecanumSubsystem drive;

	Map<Input, DoubleSupplier> inputMap = new HashMap<>();

	public MecanumSubsystemDefaultCommand(final MecanumSubsystem drive) {

		this.drive = drive;

		addRequirements(drive);
	}

	public MecanumSubsystemDefaultCommand useAxis(Input input, DoubleSupplier axis){
		inputMap.put(input, axis);
		return this;
	}

	@Override
	public void execute() {
		drive.getMecanumDrive().driveCartesian(axis(Input.FORWARD), axis(Input.STRAFE), axis(Input.TURN));
	}

	private final double axis(Input input) {
		return inputMap.get(input).getAsDouble();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
	}
}