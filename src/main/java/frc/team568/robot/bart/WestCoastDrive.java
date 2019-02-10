package frc.team568.robot.bart;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.team568.robot.RobotBase;
import frc.team568.robot.Xinput;
import frc.team568.robot.subsystems.SubsystemBase;

public class WestCoastDrive extends SubsystemBase {

	private DifferentialDrive drive;
	private Joystick joystick;
	private WPI_TalonSRX fl;
	private WPI_TalonSRX bl;
	private WPI_TalonSRX fr;
	private WPI_TalonSRX br;
	private NetworkTableEntry averageVelocityEntry;
	private NetworkTableEntry averageDistanceEntry;
	private NetworkTableEntry rightDistanceEntry;
	private NetworkTableEntry leftDistanceEntry;

	// Circumference of wheel (Inches)
	private static final double WHEEL_DIAMETER = 6.0;
	private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * 3.14159265358979;
	// Ticks per revolution
	private static final double TPR = 4096;
	// Distance per ticks
	private static final double DIST_PER_TICK = WHEEL_CIRCUMFERENCE / TPR;

	private static final double CURVATURE_MINIMUM_VELOCITY = 6.0;

	public WestCoastDrive(RobotBase robot) {
		super(robot);

		initMotors();
		drive = new DifferentialDrive(new SpeedControllerGroup(fl, bl), new SpeedControllerGroup(fr, br));
		drive.setRightSideInverted(false);
		
		joystick = new Joystick(port("mainJoystick"));

		reset();

		initShuffleboard();
	}

	@Override
	public void periodic() {
		averageVelocityEntry.setDouble(getAverageVelocity());
		averageDistanceEntry.setDouble(getAverageDistance());
		leftDistanceEntry.setDouble(fl.getSelectedSensorPosition() * DIST_PER_TICK);
		rightDistanceEntry.setDouble(fr.getSelectedSensorPosition() * DIST_PER_TICK);
	}

	protected void initMotors() {
		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		addChild("FL Motor", fl);
		addChild("BL Motor", bl);
		addChild("FR Motor", fr);
		addChild("BR Motor", br);

		fl.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		fr.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		fl.setInverted(false);
		bl.setInverted(false);
		fr.setInverted(true);
		br.setInverted(true);

		fl.setSensorPhase(true);
		fr.setSensorPhase(true);

		bl.follow(fl);
		br.follow(fr);

		fl.configNominalOutputForward(0, 0);
		fl.configNominalOutputReverse(0, 0);
		fl.configPeakOutputForward(1, 0);
		fl.configPeakOutputReverse(-1, 0);

		fr.configNominalOutputForward(0, 0);
		fr.configNominalOutputReverse(0, 0);
		fr.configPeakOutputForward(1, 0);
		fr.configPeakOutputReverse(-1, 0);

		fl.config_kF(0, 0.465, 0);
		fl.config_kP(0, 0.1, 0);
		fl.config_kI(0, 0, 0);
		fl.config_kD(0, 0, 0);

		fr.config_kF(0, .483, 0);
		fr.config_kP(0, 0.1, 0);
		fr.config_kI(0, 0, 0);
		fr.config_kD(0, 0, 0);
	}

	protected void initShuffleboard() {
		var tab = Shuffleboard.getTab("Bart");
		averageVelocityEntry = tab.add("Velocity (ave)", getAverageVelocity()).getEntry();
		averageDistanceEntry = tab.add("Distance (avg)", getAverageDistance()).getEntry();
		leftDistanceEntry = tab.add("Left distance", fl.getSelectedSensorPosition()).getEntry();
		rightDistanceEntry = tab.add("Right distance", fr.getSelectedSensorPosition()).getEntry();
	}

	public void stop() {
		drive.stopMotor();
	}

	public void driveForward() {
		drive.curvatureDrive(0.5, 0, false);
	}

	public double getAverageVelocity() {
		return (fl.getSelectedSensorVelocity() + fr.getSelectedSensorVelocity()) / 2 * DIST_PER_TICK;
	}

	public double getAverageDistance() {
		return (fl.getSelectedSensorPosition() + fr.getSelectedSensorPosition()) / 2 * DIST_PER_TICK;
	}

	public void reset() {
		fl.setSelectedSensorPosition(0);
		fr.setSelectedSensorPosition(0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {

			{
				requires(WestCoastDrive.this);
			}

			@Override
			protected void initialize() {

			}

			@Override
			protected void execute() {
				double velocity = Math.max(Math.abs(fl.getSelectedSensorVelocity()), Math.abs(fr.getSelectedSensorVelocity()));
				drive.curvatureDrive(
					-joystick.getRawAxis(Xinput.LeftStickY),
					joystick.getRawAxis(Xinput.RightStickX),
					velocity < CURVATURE_MINIMUM_VELOCITY);
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

		}); // End set default command

	} // End init default command

}
