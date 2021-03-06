package frc.team568.robot.powerup;

import static frc.team568.util.Utilities.*;

import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class BlockLift2018 extends SubsystemBase {
	private static final int PPR = 2048; // Encoder set for 2048 pulses per rotation
	private static final double RAISE_SPEED = 1.0;
	private static final double LOWER_SPEED = -1.0;
	private static final double BOTTOM_POSITION = -6;
	private static final double TOP_POSITION = 20;

	private SpeedController liftMotor;
	private Encoder liftEncoder;
	private double target = 0;
	private boolean hasTarget = false;

	public static final double BOTTOM = 0;
	public static final double TOP = 1;

	public double shootHeight = 22;

	public BlockLift2018(final RobotBase robot) {
		super(robot);

		liftMotor = new WPI_TalonSRX(port("liftMotor"));
		liftMotor.setInverted(false);
		liftEncoder = new Encoder(port("liftEncoderA"), port("liftEncoderB"), false, EncodingType.k4X);
		liftEncoder.setReverseDirection(true);
		liftEncoder.setDistancePerPulse(1.0 / PPR); // base unit is one full rotation
		liftEncoder.reset(); // ensure encoder is at zero
		initDefaultCommand();
	}

	public double getPosition() {
		return rawToPosition(getPositionRaw());
	}

	public double getPositionRaw() {
		return liftEncoder.getDistance();
	}

	public void moveLiftTo(double position) {
		position = clamp(position, BOTTOM, TOP);
		getCurrentCommand().cancel();
		target = positionToRaw(position);
		hasTarget = true;
		if (position >= getPosition())
			liftMotor.set(RAISE_SPEED);
		else if (position <= getPosition())
			liftMotor.set(LOWER_SPEED);
		else
			liftMotor.stopMotor();
	}

	public double rawToPosition(double raw) {
		return (raw - BOTTOM_POSITION) / (TOP_POSITION - BOTTOM_POSITION);
	}

	public double positionToRaw(double position) {
		return position * (TOP_POSITION - BOTTOM_POSITION) + BOTTOM_POSITION;
	}

	public void setSpeed(double speed) {
		hasTarget = false;
		if ((speed < 0 && getPositionRaw() <= BOTTOM_POSITION) || (speed > 0 && getPositionRaw() >= TOP_POSITION))
			stop();
		else
			liftMotor.set(speed);
	}

	public void stop() {
		hasTarget = false;
		liftMotor.stopMotor();
	}

	public void reset() {
		liftEncoder.reset();
	}

	public Command getCommandMoveLiftTo(final double position) {
		return new CommandBase() {
			private double startingPosition;

			{
				addRequirements(BlockLift2018.this);
			}

			@Override
			public void initialize() {
				// target = positionToRaw(clamp(position, BOTTOM, TOP));
				target = position;
				hasTarget = true;
				startingPosition = getPositionRaw();
			}

			@Override
			public void execute() {
				if (target > getPositionRaw())
					setSpeed(RAISE_SPEED);
				else if (target < getPositionRaw())
					setSpeed(LOWER_SPEED);
				else
					stop();
			}

			@Override
			public boolean isFinished() {
				return (startingPosition <= target && getPositionRaw() >= target)
						|| (startingPosition >= target && getPositionRaw() <= target);
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		};
	}

	public Command getCommandRaise() {
		return new CommandBase() {
			{
				addRequirements(BlockLift2018.this);
			}

			@Override
			public void execute() {
				setSpeed(RAISE_SPEED);
				// System.out.println(getPositionRaw());
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		};
	}

	public Command getCommandLower() {
		return new CommandBase() {
			{
				addRequirements(BlockLift2018.this);
			}

			@Override
			public void execute() {
				setSpeed(LOWER_SPEED);
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		};
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {
			{
				addRequirements(BlockLift2018.this);
				SendableRegistry.addChild(BlockLift2018.this, this);
			}

			@Override
			public void execute() {
				boolean movingUp = liftEncoder.getDirection();
				if (hasTarget) {
					if (getPositionRaw() < target)
						liftMotor.set(RAISE_SPEED);
					else if (getPositionRaw() > target)
						liftMotor.set(LOWER_SPEED);
					else
						liftMotor.stopMotor();
				}
				if ((movingUp && getPositionRaw() >= TOP_POSITION)
						|| (!movingUp && getPositionRaw() <= BOTTOM_POSITION))
					liftMotor.stopMotor();
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		});
	}

}
