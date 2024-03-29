package frc.team568.robot.rapidreact;

import java.util.HashMap;
import java.util.Map;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.subsystems.DriveBase.Input;

public class MecanumSubsystemDefaultCommand extends Command {
	private final MecanumSubsystem drive;
	private boolean fieldRelativeControls = false;
	private ADXRS450_Gyro gyro;

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
		gyro.reset();
	}

	public MecanumSubsystemDefaultCommand useAxis(Input input, DoubleSupplier axis){
		inputMap.put(input, axis);
		return this;
	}

	@Override
	public void initialize() {
		gyro = drive.getGyro();
	}

	@Override
	public void execute() {
		if (fieldRelativeControls && gyro != null)
			drive.getMecanumDrive().driveCartesian(axis(Input.FORWARD), -axis(Input.STRAFE), -axis(Input.TURN), Rotation2d.fromDegrees(-gyro.getAngle()));
		else
			drive.getMecanumDrive().driveCartesian(axis(Input.FORWARD), -axis(Input.STRAFE), -axis(Input.TURN));
	}

	public boolean hasFieldRelativeControls() {
		return fieldRelativeControls;
	}

	public void useFieldRelativeControls(boolean isFieldRelative) {
		this.fieldRelativeControls = isFieldRelative;
	}

	private final double axis(Input input) {
		return MathUtil.applyDeadband(inputMap.get(input).getAsDouble(),0.1);
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addBooleanProperty("Field Relative Controls", this::hasFieldRelativeControls, this::useFieldRelativeControls);
	}
}