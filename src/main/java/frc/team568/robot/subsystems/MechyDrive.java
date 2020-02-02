package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.controller.PIDController;

import frc.team568.robot.RobotBase;

public class MechyDrive extends SubsystemBase {

	private double Kp = 0.04;
	private double Ki = 0;
	private double Kd = 0.002;
	private double correction = 0;
	private double prevAngle = 0;

	private MecanumDrive drive;

	private WPI_TalonSRX fl;
	private WPI_TalonSRX bl;
	private WPI_TalonSRX fr;
	private WPI_TalonSRX br;

	private ADXRS450_Gyro gyro;

	private PIDController pidDrive;

	public MechyDrive(RobotBase robot) {

		super(robot);

		drive = buildDrive();
		configureGyro();
		configurePID();

		reset();

	}

	private MecanumDrive buildDrive() {

		initMotors();
		MecanumDrive d = new MecanumDrive(fl, bl, fr, br);
		d.setRightSideInverted(false);

		return d;

	}

	private void initMotors() {
		
		fl = new WPI_TalonSRX(port("leftFrontMotor"));
		bl = new WPI_TalonSRX(port("leftBackMotor"));
		fr = new WPI_TalonSRX(port("rightFrontMotor"));
		br = new WPI_TalonSRX(port("rightBackMotor"));

		addChild("FL Motor", fl);
		addChild("BL Motor", bl);
		addChild("FR Motor", fr);
		addChild("BR Motor", br);

		fl.setInverted(true);
		bl.setInverted(true);
		fr.setInverted(false);
		br.setInverted(false);
		
	}

	private void configureGyro() {
		
		gyro = new ADXRS450_Gyro();
		gyro.reset();

	}

	private void configurePID() {
		
		pidDrive = new PIDController(Kp, Ki, Kd);

	}

	@Override
	public void periodic() {
		
	}

	public void stop() {

	}

	public void reset() {
		
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Command() {

			{
				requires(MechyDrive.this);
			}

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {

				// Mecanum drive

				//drive.driveCartesian(-axis("side"), axis("forward"), -axis("turn"), gyro.getAngle());

				pidDrive.setSetpoint(prevAngle);
				correction = pidDrive.calculate(gyro.getAngle());

				if (Math.abs(axis("turn")) > 0.1) {
					prevAngle = gyro.getAngle();
					correction = 0;
				}

				double r = Math.hypot(axis("side"), axis("forward"));
				double robotAngle = Math.atan2(-axis("forward"), axis("side")) - Math.PI / 4;
				double rightX = axis("turn");
				final double v1 = -r * Math.cos(robotAngle) - rightX - correction;
				final double v2 = -r * Math.sin(robotAngle) + rightX + correction;
				final double v3 = -r * Math.sin(robotAngle) - rightX - correction;
				final double v4 = -r * Math.cos(robotAngle) + rightX + correction;
				fl.set(v1);
				fr.set(v2);
				bl.set(v3);
				br.set(v4);
				
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

		}); // End set default command

	} // End init default command

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);
		builder.addDoubleProperty("Forward", () -> axis("forward"), null);
		builder.addDoubleProperty("Side", () -> axis("side"), null);
		builder.addDoubleProperty("Turn", () -> axis("turn"), null);
		builder.addDoubleProperty("prevAngle", () -> prevAngle, null);
	
	}

}
