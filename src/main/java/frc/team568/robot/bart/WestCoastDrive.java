package frc.team568.robot.bart;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
	private ShuffleboardTab shuffleboardTab;
	private NetworkTableEntry averageDistanceEntry;
	private NetworkTableEntry rightDistanceEntry;
	private NetworkTableEntry leftDistanceEntry;

	// Circumference of wheel (Inches)
	private static final double CIRCUMFERENCE = 18.8496;
	// Ticks per revolution
	private static final double TPR = 4096;
	// Distance per ticks
	private static final double DIST_PER_TICK = CIRCUMFERENCE / TPR;

	public WestCoastDrive(RobotBase robot) {
		super(robot);

		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		drive = new DifferentialDrive(new SpeedControllerGroup(fl, bl), new SpeedControllerGroup(fr, br));
		drive.setRightSideInverted(false);
		
		joystick = new Joystick(port("mainJoystick"));

		shuffleboardTab = Shuffleboard.getTab("Bart");
		averageDistanceEntry = shuffleboardTab.add("Average distance traveled", getAverageDistance()).getEntry();
		rightDistanceEntry = shuffleboardTab.add("Right motor distance", 0).getEntry();
		leftDistanceEntry = shuffleboardTab.add("Left motor distance", 0).getEntry();

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

	}

	@Override
	public void periodic() {

		averageDistanceEntry.setDouble(getAverageDistance());
		rightDistanceEntry.setDouble(fr.getSensorCollection().getQuadraturePosition() * DIST_PER_TICK);
		leftDistanceEntry.setDouble(fl.getSensorCollection().getQuadraturePosition() * DIST_PER_TICK);
		SmartDashboard.putNumber("left Output", fl.getMotorOutputPercent());
		SmartDashboard.putNumber("Right Output", fr.getMotorOutputPercent());


	}

	public void stop() {
		drive.stopMotor();
	}

	public void driveForward() {
		drive.curvatureDrive(0.5, 0, false);
	}

	public double getAverageDistance() {
		return ((fl.getSensorCollection().getQuadraturePosition() + fr.getSensorCollection().getQuadraturePosition()) / 2) * DIST_PER_TICK;
	}

	public void reset() {
		fl.getSensorCollection().setQuadraturePosition(0, 10);
		fr.getSensorCollection().setQuadraturePosition(0, 10);
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
				// double x = -joystick.getRawAxis(1);
				// double y = joystick.getRawAxis(4);
				// x = x * x * Math.signum(x);
				// y = y * y * Math.signum(y);
				// drive.curvatureDrive(x, y,
				// joystick.getRawButton(ControllerButtons.leftBumper));

				drive.arcadeDrive(-joystick.getRawAxis(Xinput.LeftStickY), joystick.getRawAxis(Xinput.RightStickX) * 0.6, false);

			}

			@Override
			protected boolean isFinished() {
				return false;
			}

		}); // End set default command

	} // End init default command

}
