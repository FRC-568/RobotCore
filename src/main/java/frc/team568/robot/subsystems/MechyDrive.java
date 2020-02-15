package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;

import frc.team568.robot.RobotBase;

public class MechyDrive extends SubsystemBase {

	private double Kp = 0.04;
	private double Ki = 0;
	private double Kd = 0.002;
	private double correction = 0;
	private double prevAngle = 0;
	private double angleCompensation = 0;

	private WPI_TalonSRX fl;
	private WPI_TalonSRX bl;
	private WPI_TalonSRX fr;
	private WPI_TalonSRX br;

	public boolean drivePOV = true;

	public ADXRS450_Gyro gyro;

	private PIDController pidDrive;

	private boolean safeMode = false;

	public MechyDrive(RobotBase robot) {

		super(robot);

		initMotors();
		configureGyro();
		configurePID();

		reset();

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

			double comboStartTime = 0;
			boolean alreadyToggled = false;

			{
				requires(MechyDrive.this);
			}

			@Override
			protected void initialize() {
				
			}

			@Override
			protected void execute() {

				// toggle POV and field mode
				if (button("driveModeToggle")) {

					drivePOV = !drivePOV;
					gyro.reset();

				}

				if (button("safeModeToggle")) {

					if (comboStartTime == 0)
						comboStartTime = Timer.getFPGATimestamp();
					else if (Timer.getFPGATimestamp() - comboStartTime >= 3.0 && !alreadyToggled) {
					
						safeMode = !safeMode;
						alreadyToggled = true;
						System.out.println("Safemode is " + (safeMode ? "Enabled" : "Disabled") + ".");
					
					}

				} else {

					comboStartTime = 0;
					alreadyToggled = false;
				
				}

				// pid calculation
				pidDrive.setSetpoint(prevAngle);
				correction = pidDrive.calculate(gyro.getAngle());

				// resets current gyro heading and pid if turning or not moving
				if (Math.abs(axis("turn")) > 0.05) {
				
					pidDrive.reset();
					prevAngle = gyro.getAngle();
					correction = 0;

				} else if (Math.abs(axis("forward")) < 0.05 && Math.abs(axis("side")) < 0.05) {

					pidDrive.reset();
					prevAngle = gyro.getAngle();
					correction = 0;

				}

				// field oriented mode
				angleCompensation = 0;
				if (!drivePOV)
					angleCompensation = Math.toRadians(gyro.getAngle());

				// drive calculation
				double r = Math.hypot(axis("side"), axis("forward"));
				double robotAngle = Math.atan2(-axis("forward"), axis("side")) - Math.PI / 4 + angleCompensation;
				double rightX = axis("turn") * 0.7;
				final double v1 = -r * Math.cos(robotAngle) - rightX - correction;
				final double v2 = -r * Math.sin(robotAngle) + rightX + correction;
				final double v3 = -r * Math.sin(robotAngle) - rightX - correction;
				final double v4 = -r * Math.cos(robotAngle) + rightX + correction;

				double driveDamp = 1;
				if (safeMode)
					driveDamp = 0.5;

				fl.set(v1 * driveDamp);
				fr.set(v2 * driveDamp);
				bl.set(v3 * driveDamp);
				br.set(v4 * driveDamp);
				
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
		builder.addDoubleProperty("P", () -> Kp, (value) -> Kp = value);
		builder.addDoubleProperty("I", () -> Ki, (value) -> Ki = value);
		builder.addDoubleProperty("D", () -> Kd, (value) -> Kd = value);

		builder.addDoubleProperty("Forward", () -> axis("forward"), null);
		builder.addDoubleProperty("Side", () -> axis("side"), null);
		builder.addDoubleProperty("Turn", () -> axis("turn"), null);
		builder.addDoubleProperty("prevAngle", () -> prevAngle, null);
		builder.addBooleanProperty("isPOV", () -> drivePOV, null);
		builder.addDoubleProperty("angleCompensation", () -> angleCompensation, null);
		builder.addBooleanProperty("isSafeMode", () -> safeMode, null);
		builder.addDoubleProperty("fl", () -> fl.get(), null);		
		builder.addDoubleProperty("fr", () -> fr.get(), null);
		builder.addDoubleProperty("bl", () -> bl.get(), null);
		builder.addDoubleProperty("br", () -> br.get(), null);
		
	}

}
