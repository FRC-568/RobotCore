package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;

public class TalonSRXDrive extends DriveBase {
	private static final double Kp = 0.2;
	private static final double Ki = 0;
	private static final double Kd = 0.1;

	private final DifferentialDrive drive;
	private WPI_TalonSRX[] motorsL;
	private WPI_TalonSRX[] motorsR;
	private PIDController pidController;
	private double errorRate;

	public TalonSRXDrive(final RobotBase robot) {
		super(robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);

		pidController = new PIDController(Kp, Ki, Kd, new DriftCalculator(), new ErrorRate());
		pidController.enable();
		addChild(pidController);
	}

	public TalonSRXDrive(String name, final RobotBase robot) {
		super(name, robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);
	}

	private class DriftCalculator implements PIDSource {

		@Override
		public PIDSourceType getPIDSourceType() {
			return PIDSourceType.kRate;
		}

		@Override
		public double pidGet() {
			double MAX_VELOCITY = 3000;
			double diff = getVelocity(Side.LEFT) - getVelocity(Side.RIGHT);
			return diff / MAX_VELOCITY;
		}

		@Override
		public void setPIDSourceType(PIDSourceType arg0) {}

	}

	private class ErrorRate implements PIDOutput {

		@Override
		public void pidWrite(double arg0) {
			errorRate = arg0;
		}

	}

	private DifferentialDrive buildDrive() {
		boolean invert;
		int[] ports;

		ports = configIntArray("leftMotors");
		invert = configBoolean("leftInverted");
		motorsL = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsL.length; i++) {
			motorsL[i] = new WPI_TalonSRX(ports[i]);
			// addChild(motorsL[i]);
			motorsL[i].setInverted(invert);
			if (i > 0)
				motorsL[i].follow(motorsL[0]);
		}

		ports = configIntArray("rightMotors");
		invert = configBoolean("rightInverted");
		motorsR = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsR.length; i++) {
			motorsR[i] = new WPI_TalonSRX(ports[i]);
			// addChild(motorsR[i]);
			motorsR[i].setInverted(invert);
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
		setDefaultCommand(new Command() {
			double comboStartTime = 0;
			boolean safeMode = configBoolean("enableSafeMode");
			boolean alreadyToggled = false;

			{
				requires(TalonSRXDrive.this);
			}

			@Override
			protected void initialize() {
				pidController.enable();
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

				pidController.setSetpoint(axis("turn"));

				if (safeMode)
					arcadeDrive(-axis("forward") * 0.5, axis("turn") * 0.5);
				else
					arcadeDrive(-axis("forward"), axis("turn") * 0.7);

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
		});
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Left Velocity", () -> getVelocity(Side.LEFT), null);
		builder.addDoubleProperty("Right Velocity", () -> getVelocity(Side.RIGHT), null);
		builder.addDoubleProperty("Average Distance", () -> getDistance(), null);
		builder.addDoubleProperty("PiDErrorRate", () -> errorRate, null);
	}

}