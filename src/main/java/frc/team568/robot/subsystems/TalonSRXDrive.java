package frc.team568.robot.subsystems;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;

public class TalonSRXDrive extends DriveBase {
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
			addChild(motorsL[i]);
			motorsL[i].setInverted(invert);
			if (i > 0)
				motorsL[i].follow(motorsL[0]);
		}

		ports = configIntArray("rightMotors");
		invert = configBoolean("rightInverted");
		motorsR = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsR.length; i++) {
			motorsR[i] = new WPI_TalonSRX(ports[i]);
			addChild(motorsR[i]);
			motorsR[i].setInverted(invert);
			if (i > 0)
				motorsR[i].follow(motorsR[0]);
		}

		DifferentialDrive d = new DifferentialDrive(motorsL[0], motorsR[0]);
		d.setRightSideInverted(false);
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
		return (motorsL[0].getSelectedSensorVelocity()
			+ motorsR[0].getSelectedSensorVelocity()) / 2;
	}

	@Override
	public double getVelocity(Side side) {
		return side == Side.RIGHT
			? motorsR[0].getSelectedSensorVelocity()
			: motorsL[0].getSelectedSensorVelocity();
	}

	@Override
	public double getDistance() {
		return (motorsL[0].getSelectedSensorPosition()
			+ motorsR[0].getSelectedSensorPosition()) / 2;
	}

	@Override
	public double getDistance(Side side) {
		return side == Side.RIGHT
			? motorsR[0].getSelectedSensorPosition()
			: motorsL[0].getSelectedSensorPosition();
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
			Joystick joystick = new Joystick(configInt("driveController"));

			{ requires(TalonSRXDrive.this); }

			@Override
			protected void initialize() {
				System.out.println("Beginning teleop control - safemode is " + (safeMode ? "Enabled" : "Disabled")
						+ ". Hold L3 + R3 5 seconds to toggle safe mode.");
			}

			@Override
			protected void execute() {
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
					arcadeDrive(-joystick.getRawAxis(configInt("driveForward")) * 0.5, joystick.getRawAxis(configInt("driveTurn")) * 0.5, false);
				else
					arcadeDrive(-joystick.getRawAxis(configInt("driveForward")), joystick.getRawAxis(configInt("driveTurn")) * 0.6, false);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}
		});
	}

}