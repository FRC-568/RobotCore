package frc.team568.robot.rapidreact;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.subsystems.DriveBase.Input;

public class MecanumSubsystemDefaultCommand extends CommandBase {
	private final MecanumSubsystem drive;
	private boolean fieldRelativeControls;
	private Gyro gyro;

	Map<Input, DoubleSupplier> inputMap = new HashMap<>();

	public MecanumSubsystemDefaultCommand(final MecanumSubsystem drive) {
		this(drive, true);
	}

	public MecanumSubsystemDefaultCommand(final MecanumSubsystem drive, boolean fieldRelativeControls) {
		this.drive = drive;
		this.fieldRelativeControls = fieldRelativeControls;
		addRequirements(drive);
		drive.addChild("Default Command", this);
	}

	public void toggleUseFieldRelative(){
		fieldRelativeControls = !fieldRelativeControls;
	}

	public MecanumSubsystemDefaultCommand useAxis(Input input, DoubleSupplier axis){
		inputMap.put(input, axis);
		return this;
	}

	@Override
	public void initialize() {
		// System.out.println("initialize");
		gyro = drive.getGyro();
	}

	@Override
	public void execute() {
		// System.out.println("periodic");
		if (fieldRelativeControls && gyro != null)
			drive.getMecanumDrive().driveCartesian(axis(Input.FORWARD), axis(Input.STRAFE), axis(Input.TURN), gyro.getAngle());
		else
			drive.getMecanumDrive().driveCartesian(axis(Input.FORWARD), axis(Input.STRAFE), axis(Input.TURN));
	}

	public boolean hasFieldRelativeControls() {
		return fieldRelativeControls;
	}

	public void useFieldRelativeControls(boolean isFieldRelative) {
		this.fieldRelativeControls = isFieldRelative;
	}

	private final double axis(Input input) {
		return inputMap.get(input).getAsDouble();
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Field Relative Controls", this::hasFieldRelativeControls, this::useFieldRelativeControls);
	}
}