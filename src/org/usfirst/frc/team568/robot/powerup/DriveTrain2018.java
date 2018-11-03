package org.usfirst.frc.team568.robot.powerup;

import static org.usfirst.frc.team568.util.Utilities.*;

import org.usfirst.frc.team568.robot.RobotBase;
import org.usfirst.frc.team568.robot.subsystems.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class DriveTrain2018 extends SubsystemBase {
	private Joystick joystick;
	public PIDController drivePID;
	private ADXRS450_Gyro gyro;
	private double drivePidOutput;
	private boolean _headingLocked = false;

	// INCHES
	private static final double CIRCUMFERENCE = 18.8496;
	// Ticks per revolution
	private static final double TPR = 4096;
	// To Ticks from inches
	private static final double TO_TICKS = TPR / CIRCUMFERENCE;

	WPI_TalonSRX fl;
	WPI_TalonSRX bl;
	WPI_TalonSRX fr;
	WPI_TalonSRX br;

	// Spark blinkin;

	int kTimeoutMs;
	int driveTargetL;
	int driveTargetR;

	public DriveTrain2018(RobotBase robot) {
		super(robot);

		gyro = new ADXRS450_Gyro();

		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		// blinkin = new Spark(port("blinkin"));
		addChild("FL Motor", fl);
		addChild("BL Motor", bl);
		addChild("FR Motor", fr);
		addChild("BR Motor", br);

		drivePID = new PIDController(.135, 0, .1, new PIDSource() {

			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {

			}

			@Override
			public PIDSourceType getPIDSourceType() {
				return PIDSourceType.kDisplacement;
			}

			@Override
			public double pidGet() {
				return gyro.getAngle();
			}

		}, (output) -> drivePidOutput = output);

		/*
		 * right = new SpeedControllerGroup(fr, br); left = new SpeedControllerGroup(fl,
		 * bl); left.setInverted(true); right.setInverted(true);
		 */

		fl.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		fr.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		fl.setInverted(false);
		bl.setInverted(false);
		fr.setInverted(true);
		br.setInverted(true);

		fl.setSensorPhase(true);
		fr.setSensorPhase(true);

		bl.follow(fl);
		br.follow(fr);

		fl.configNominalOutputForward(0, 10);
		fl.configNominalOutputReverse(0, 10);
		fl.configPeakOutputForward(1, 10);
		fl.configPeakOutputReverse(-1, 10);

		fr.configNominalOutputForward(0, 10);
		fr.configNominalOutputReverse(0, 10);
		fr.configPeakOutputForward(1, 10);
		fr.configPeakOutputReverse(-1, 10);

		fl.config_kF(0, 0.465, 10);
		fl.config_kP(0, 0.1, 10);
		fl.config_kI(0, 0, 10);
		fl.config_kD(0, 0, 10);

		fr.config_kF(0, .483, 10);
		fr.config_kP(0, 0.1, 10);
		fr.config_kI(0, 0, 10);
		fr.config_kD(0, 0, 10);

		fl.setSelectedSensorPosition(0, 0, 10);
		fr.setSelectedSensorPosition(0, 0, 10);

		// drive = new DifferentialDrive(fl, fr);

		joystick = new Joystick(0);
	}

	public void resetDist() {
		fl.setSelectedSensorPosition(0, 0, 10);
		fr.setSelectedSensorPosition(0, 0, 10);
	}

	public double getDist() {

		return fl.getSelectedSensorPosition(0);
	}

	public void resetGyro() {
		gyro.reset();

	}

	public void calGyro() {
		gyro.reset();
		gyro.calibrate();
	}

	public boolean isHeadingLocked() {
		return _headingLocked;
	}

	public void lockHeading() {
		drivePID.setSetpoint(getAngle());
		drivePID.enable();
		_headingLocked = true;
	}

	public void releaseHeading() {
		drivePID.disable();
		_headingLocked = false;
	}

	public void driveDist(double dist, int speed, int accel) {
		int ticks = (int) (dist * TO_TICKS);
		int flCurrPos = fl.getSensorCollection().getQuadraturePosition();
		int frCurrPos = fr.getSensorCollection().getQuadraturePosition();

		driveTargetL = flCurrPos + ticks;
		driveTargetR = frCurrPos + ticks;

		fl.configMotionCruiseVelocity(speed, 10);
		fr.configMotionCruiseVelocity(speed, 10);

		fl.configMotionAcceleration(accel, 10);
		fr.configMotionAcceleration(accel, 10);
	}

	public void driveDist(double speed, double dist) {
		double speedScale = drivePidOutput;

		// if (targetPercent >= .75)
		// speed = speed * (1 - targetPercent);
		if (speed > 0) {
			fl.set(ControlMode.PercentOutput, speed + (speedScale * speed));
			fr.set(ControlMode.PercentOutput, speed + (-speedScale * speed));
		} else if (speed < 0) {
			fl.set(ControlMode.PercentOutput, speed + (-speedScale * speed));
			fr.set(ControlMode.PercentOutput, speed + (speedScale * speed));
		}
		// System.out.println(gyro.getAngle());
	}

	public void driveExecute() {
		fl.set(ControlMode.Position, driveTargetL);
		fr.set(ControlMode.Position, driveTargetR);
		// System.out.println("LEFT ERROR:" + fl.getClosedLoopError(0));
		// System.out.println("RIGHT ERROR:" + fr.getClosedLoopError(0));

	}

	public boolean driveisFinished() {
		// abs??? overshoot
		// boolean isfin = (fl.getClosedLoopError(0) < 10 && fr.getClosedLoopError(0) <
		// 10);
		// boolean isfin = ((driveTargetL -
		// fl.getSensorCollection().getQuadraturePosition()) < 10 && (driveTargetR -
		// fr.getSensorCollection().getQuadraturePosition()) < 10);
		// if (isfin)
		// System.out.println("Finished!");
		return false;

	}

	public void stop() {
		fl.set(ControlMode.PercentOutput, 0);
		fr.set(ControlMode.PercentOutput, 0);
	}

	public void arcadeDrive(double xSpeed, double zRotation, boolean squaredInputs) {
		final double m_deadband = 0.2;

		xSpeed = clamp(xSpeed, -1, 1);
		xSpeed = applyDeadband(xSpeed, m_deadband);

		zRotation = clamp(zRotation, -1, 1);
		zRotation = applyDeadband(zRotation, m_deadband);

		// Square the inputs (while preserving the sign) to increase fine control
		// while permitting full power.
		if (squaredInputs) {
			xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
			zRotation = Math.copySign(zRotation * zRotation, zRotation);
		}

		double leftMotorOutput;
		double rightMotorOutput;

		double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

		if (xSpeed >= 0.0) {
			// First quadrant, else second quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			} else {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			}
		} else {
			// Third quadrant, else fourth quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			} else {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			}
		}

		fl.set(clamp(leftMotorOutput, -1, 1));
		fr.set(clamp(rightMotorOutput, -1, 1));
		// System.out.println("POS L" + fl.getSelectedSensorPosition(0));
		// System.out.println("POS R" + fr.getSelectedSensorPosition(0));

		// m_safetyHelper.feed();
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {
			double comboStartTime = 0;
			boolean safeMode = true;
			boolean alreadyToggled = false;

			{
				requires(DriveTrain2018.this);
			}

			@Override
			protected void initialize() {
				System.out.println("Beginning teleop control - safemode is " + (safeMode ? "Enabled" : "Disabled")
						+ ". Hold L3 + R3 5 seconds to toggle safe mode.");
			}

			@Override
			protected void execute() {
				if (joystick.getRawButton(ControllerButtons.LeftStickIn)
						&& joystick.getRawButton(ControllerButtons.RightStickIn)) {
					if (comboStartTime == 0)
						comboStartTime = Timer.getFPGATimestamp();
					else if (Timer.getFPGATimestamp() - comboStartTime >= 5.0) {
						if (!alreadyToggled) {
							safeMode = !safeMode;
							alreadyToggled = true;
							System.out.println("Safemode is " + (safeMode ? "Enabled" : "Disabled") + ".");
						}
					}
				} else {
					comboStartTime = 0;
					alreadyToggled = false;
				}

				if (safeMode)
					arcadeDrive(-joystick.getRawAxis(1) * 0.5, joystick.getRawAxis(4) * 0.5, false);
				else
					arcadeDrive(-joystick.getRawAxis(1), joystick.getRawAxis(4) * 0.6, false);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

	public void turnRight(double speed) {
		fl.set(ControlMode.PercentOutput, speed);
		fr.set(ControlMode.PercentOutput, -speed);
	}

	public void turnLeft(double speed) {
		fl.set(ControlMode.PercentOutput, -speed);
		fr.set(ControlMode.PercentOutput, speed);
	}

	public double getAngle() {
		return gyro.getAngle();
	}

	public Command getCommandTurnBy(double degrees) {
		return new PIDCommand(0.1, 0, 0) {
			double targetAngle;

			{
				requires(DriveTrain2018.this);
			}

			@Override
			protected void initialize() {
				resetGyro();
				targetAngle = getAngle() + degrees;
				setSetpoint(targetAngle);
			}

			@Override
			protected void execute() {
				// SmartDashboard.putNumber("GYRO", ref.getAngle());
				if (degrees > 0)
					turnRight(.3);
				else if (degrees < 0)
					turnLeft(.3);
				else
					stop();
			}

			@Override
			protected boolean isFinished() {
				if (degrees < 0) {
					return getAngle() < (degrees - 5);
				} else if (degrees > 0) {
					return getAngle() > (degrees + 5);
				} else
					return true;
			}

			@Override
			protected void end() {
				stop();
				Timer.delay(1);
			}

			@Override
			protected double returnPIDInput() {
				return getAngle();
			}

			@Override
			protected void usePIDOutput(double output) {
				if (degrees > 0)
					turnRight(.3);
				else if (degrees < 0)
					turnLeft(.3);
				else
					stop();
			}
		};
	}
}
