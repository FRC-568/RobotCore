package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;

public class TalonSRXDrive extends DriveBase {
	private static final double Kp = 0.25;
	private static final double Ki = 0.01;
	private static final double Kd = 0.01;
	private static final double MAX_VELOCITY = 3000;

	private final DifferentialDrive drive;
	private WPI_TalonSRX[] motorsL;
	private WPI_TalonSRX[] motorsR;

	public TalonSRXDrive(final RobotBase robot) {
		super(robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);
	}

	public TalonSRXDrive(String name, final RobotBase robot) {
		super(name, robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);
	}

	private DifferentialDrive buildDrive() {
		boolean invert;
		int[] ports;

		ports = configIntArray("leftMotors");
		invert = configBoolean("leftInverted");
		motorsL = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsL.length; i++) {
			motorsL[i] = new WPI_TalonSRX(ports[i]);
			motorsL[i].setInverted(invert);
			motorsL[i].setNeutralMode(NeutralMode.Coast);
			if (i > 0)
				motorsL[i].follow(motorsL[0]);
		}

		ports = configIntArray("rightMotors");
		invert = configBoolean("rightInverted");
		motorsR = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsR.length; i++) {
			motorsR[i] = new WPI_TalonSRX(ports[i]);
			motorsR[i].setInverted(invert);
			motorsL[i].setNeutralMode(NeutralMode.Coast);
			if (i > 0)
				motorsR[i].follow(motorsR[0]);
		}

		DifferentialDrive d = new DifferentialDrive(motorsL[0], motorsR[0]);
		d.setRightSideInverted(false);
		addChild(d);
		return d;
	}

	private void configureEncoder(WPI_TalonSRX talon) {
		talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
		talon.setSensorPhase(true);

		talon.configNominalOutputForward(0);
		talon.configNominalOutputReverse(0);
		talon.configPeakOutputForward(1);
		talon.configPeakOutputReverse(-1);

		talon.config_kF(0, 0.465);
		talon.config_kP(0, 0.1);
		talon.config_kI(0, 0);
		talon.config_kD(0, 0);
	}

	@Override
	public void tankDrive(double leftValue, double rightValue, boolean squareInputs) {
		drive.tankDrive(leftValue, rightValue, squareInputs);
	}

	@Override
	public void arcadeDrive(double moveValue, double rotateValue, boolean squareInputs) {
		drive.arcadeDrive(moveValue, rotateValue, squareInputs);
	}

	@Override
	public double getHeading() {
		return 0;
	}

	@Override
	public double getVelocity() {
		return (motorsL[0].getSelectedSensorVelocity() + motorsR[0].getSelectedSensorVelocity()) / 2;
	}

	@Override
	public double getVelocity(Side side) {
		return side == Side.RIGHT ? motorsR[0].getSelectedSensorVelocity() : motorsL[0].getSelectedSensorVelocity();
	}

	@Override
	public double getDistance() {
		return (motorsL[0].getSelectedSensorPosition() + motorsR[0].getSelectedSensorPosition()) / 2;
	}

	@Override
	public double getDistance(Side side) {
		return side == Side.RIGHT ? motorsR[0].getSelectedSensorPosition() : motorsL[0].getSelectedSensorPosition();
	}

	@Override
	public void resetSensors() {
		motorsL[0].setSelectedSensorPosition(0);
		motorsR[0].setSelectedSensorPosition(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new PIDCommand(Kp, Ki, Kd) {
			double comboStartTime = 0;
			boolean safeMode = configBoolean("enableSafeMode");
			boolean alreadyToggled = false;
			boolean driveReverse = false;
			boolean reverseIsHeld = false;
			double driftCompensation = 0;

			{ 
				requires(TalonSRXDrive.this); 
				TalonSRXDrive.this.addChild(this);
			}

			@Override
			protected void initialize() {
				System.out.println("Beginning teleop control - safemode is " + (safeMode ? "Enabled" : "Disabled")
						+ ". Hold L3 + R3 5 seconds to toggle safe mode.");
			}

			@Override
			protected void execute() {
				if (button("safeMode1") && button("safeMode2")) {
					if (comboStartTime == 0)
						comboStartTime = Timer.getFPGATimestamp();
					else if (Timer.getFPGATimestamp() - comboStartTime >= 5.0 && !alreadyToggled) {
						safeMode = !safeMode;
						alreadyToggled = true;
						System.out.println("Safemode is " + (safeMode ? "Enabled" : "Disabled") + ".");
					}
				} else {
					comboStartTime = 0;
					alreadyToggled = false;
				}
				
				if (button("driveReverse")) {
					if (!reverseIsHeld)
						driveReverse = !driveReverse;
					reverseIsHeld = true;
				} else {
					reverseIsHeld = false;
				}

				double forward = axis("forward");
				double turn = axis("turn");

				//if (forward > 0) // <- increase 0 to add a dead zone
				//	turn *= -1;

				if (!driveReverse)
					forward *= -1;

				setSetpoint(turn);
				turn += driftCompensation;

				if (safeMode)
					arcadeDrive(forward * 0.5, turn * 0.5);
				else
					arcadeDrive(forward, turn * 0.6);
					
				if (button("stopMotors")) {
					drive.stopMotor();
				} else if (button("idleMotors")) {
					drive.arcadeDrive(0, 0, false);
				}
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			protected double returnPIDInput() {
				return (getVelocity(Side.LEFT) - getVelocity(Side.RIGHT)) / MAX_VELOCITY;
			}

			@Override
			protected void usePIDOutput(double pidOut) {
				driftCompensation = pidOut;
			}

			@Override
			public void initSendable(SendableBuilder builder) {
				super.initSendable(builder);
				builder.addDoubleProperty("Drift Compensation", () -> driftCompensation, null);
				builder.addDoubleProperty("Error Rate", () -> getPIDController().getError(), null);
				builder.addDoubleProperty("Setpoint", () -> getPIDController().getSetpoint(), null);
			}
		});
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Left Velocity", () -> getVelocity(Side.LEFT), null);
		builder.addDoubleProperty("Right Velocity", () -> getVelocity(Side.RIGHT), null);
		builder.addDoubleProperty("Average Distance", () -> getDistance(), null);
	}

}