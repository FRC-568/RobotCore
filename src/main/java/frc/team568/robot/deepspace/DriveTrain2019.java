package frc.team568.robot.deepspace;

import static frc.team568.util.Utilities.applyDeadband;
import static frc.team568.util.Utilities.clamp;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.SubsystemBase;

public class DriveTrain2019 extends SubsystemBase {
	private Joystick joystick;
	public PIDController drivePID;
	private ADXRS450_Gyro gyro;
	private double drivePidOutput;
	private boolean _headingLocked = false;
	private DoubleSolenoid doubleSolenoid;

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

	public DriveTrain2019(RobotBase robot) {
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

		drivePID = new PIDController(0.135, 0, 0.1);

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

		joystick = new Joystick(port("mainJoystick"));

		doubleSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, port("forwardChannel"), port("reverseChannel"));

		// Start in low gear
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse);

		initDefaultCommand();
	}

	@Override
	public void periodic() {
		if (_headingLocked)
			drivePidOutput = drivePID.calculate(gyro.getAngle());
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
		_headingLocked = true;
	}

	public void releaseHeading() {
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
	}

	public void driveExecute() {
		fl.set(ControlMode.Position, driveTargetL);
		fr.set(ControlMode.Position, driveTargetR);
	}

	public boolean driveisFinished() {
		// abs??? overshoot
		// boolean isfin = (fl.getClosedLoopError(0) < 10 && fr.getClosedLoopError(0) <
		// 10);
		// boolean isfin = ((driveTargetL -
		// fl.getSensorCollection().getQuadraturePosition()) < 10 && (driveTargetR -
		// fr.getSensorCollection().getQuadraturePosition()) < 10);
		// if (isfin)
		return false;

	}

	public void stop() {
		fl.set(ControlMode.PercentOutput, 0);
		fr.set(ControlMode.PercentOutput, 0);
	}

	public void setSpeed(double leftValue, double rightValue) {
		fl.set(leftValue);
		fr.set(rightValue);
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

		// m_safetyHelper.feed();
	}

	public void shiftHigh() {
		doubleSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	public void shiftLow() {
		doubleSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new CommandBase() {
			double comboStartTime = 0;
			boolean safeMode = true;
			boolean alreadyToggled = false;

			boolean isShiftHigh = false;

			{ addRequirements(DriveTrain2019.this); }

			@Override
			public void initialize() {
				System.out.println("Beginning teleop control - safemode is " + (safeMode ? "Enabled" : "Disabled")
						+ ". Hold L3 + R3 5 seconds to toggle safe mode.");
			}

			@Override
			// runs every 20 milliseconds
			public void execute() {
				if (joystick.getRawButton(Xinput.LeftStickIn) && joystick.getRawButton(Xinput.RightStickIn)) {
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
					arcadeDrive(-joystick.getRawAxis(Xinput.LeftStickY) * 0.5, joystick.getRawAxis(Xinput.RightStickX) * 0.5, false);
				else 
					arcadeDrive(-joystick.getRawAxis(Xinput.LeftStickY), joystick.getRawAxis(Xinput.RightStickX) * 0.6, false);

				if (joystick.getRawButton(Xinput.LeftBumper)) {
					if (!isShiftHigh) {
						// Set to high gear
						shiftHigh();
						isShiftHigh = true;
					} else {
						// Set to low gear
						shiftLow();
						isShiftHigh = false;
					}
				}
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
		return new CommandBase() {
			double targetAngle;

			{ addRequirements(DriveTrain2019.this); }

			@Override
			public void initialize() {
				resetGyro();
				targetAngle = getAngle() + degrees;
			}
			
			@Override
			public void execute() {
				double remainder = targetAngle - getAngle();
				if (remainder > 5)
					turnRight(.3);
				else if (remainder < -5)
					turnLeft(.3);
				else
					stop();
			}

			@Override
			public boolean isFinished() {
				return Math.abs(targetAngle - getAngle()) < 5;
			}

			@Override
			public void end(boolean interrupted) {
				stop();
			}
		}.andThen(new WaitCommand(1));
	}
}