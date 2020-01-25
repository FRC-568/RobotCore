package frc.team568.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team568.robot.RobotBase;

public class TalonSRXDrive extends DriveBase {
	
	private double Kp = 0.5;
	private double Ki = 0;
	private double Kd = 0;
	private double correction = 0;
	private double prevAngle = 0;

	private final DifferentialDrive drive;
	private WPI_TalonSRX[] motorsL;
	private WPI_TalonSRX[] motorsR;

	private Gyro gyro;

	private PIDController pidDrive;

	public TalonSRXDrive(final RobotBase robot) {

		super(robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);
		configureGyro();
		configurePID();
	
	}

	public TalonSRXDrive(String name, final RobotBase robot) {
		
		super(name, robot);
		drive = buildDrive();
		configureEncoder(motorsL[0]);
		configureEncoder(motorsR[0]);
		configureGyro();
		configurePID();
	
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
			//motorsL[i].configOpenloopRamp(0.2);
			motorsL[i].configContinuousCurrentLimit(27);
			if (i > 0)
				motorsL[i].follow(motorsL[0]);

		}

		ports = configIntArray("rightMotors");
		invert = configBoolean("rightInverted");
		motorsR = new WPI_TalonSRX[ports.length];
		for (int i = 0; i < motorsR.length; i++) {

			motorsR[i] = new WPI_TalonSRX(ports[i]);
			motorsR[i].setInverted(invert);
			motorsR[i].setNeutralMode(NeutralMode.Coast);
			//motorsL[i].configOpenloopRamp(0.2);
			motorsR[i].configContinuousCurrentLimit(27);
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

	private void configureGyro() {
		
		//gyro = robot.getSubsystem(GyroSubsystem.class).getGyro();
		//gyro.calibrate();
		//gyro.reset();

	}

	private void configurePID() {

		pidDrive = new PIDController(Kp, Ki, Kd);

	}
	
	public double getError() {

		double error = gyro.getAngle() - prevAngle;

        return error;

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

	public void resetGyro() {

		gyro.reset();

	}

	@Override
	protected void initDefaultCommand() {

		setDefaultCommand(new Command() {

			double comboStartTime = 0;
			boolean safeMode = configBoolean("enableSafeMode");
			boolean alreadyToggled = false;
			boolean driveReverse = false;
			boolean reverseIsHeld = false;

			NetworkTableEntry tankMode;

			{ 

				requires(TalonSRXDrive.this); 
				TalonSRXDrive.this.addChild(this);
			
			}

			@Override
			protected void initialize() {
				System.out.println("Beginning teleop control - safemode is " + (safeMode ? "Enabled" : "Disabled") + ". Hold L3 + R3 5 seconds to toggle safe mode.");	
			}

			@Override
			protected void execute() {
				
				if(button("tankModeToggle")) {
					tankMode.setBoolean(!tankMode.getBoolean(false));
				}
				
				if (button("safeModeToggle")) {

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
				
				} else
					reverseIsHeld = false;

				if (tankMode.getBoolean(false)) {

					double left = axis("left");
					double right = axis("right");

					if(button("launch")) {
						left = 1;
						right = 1;
					}

					if (driveReverse) {

						double leftTemp = left * -1;
						left = right * -1;
						right = leftTemp;
					
					}
					
					if (safeMode)
						tankDrive(left * 0.5, right * 0.5);
					else 
						tankDrive(left, right);

				} else {

					double forward = axis("forward");
					double turn = axis("turn");

					if (button("launch")) {

						forward = 1;
						turn = 0;
				
					}

					if (driveReverse)
						forward *= -1;
					/*
					if (turn != 0) {

						pidDrive.reset();
						prevAngle = gyro.getAngle();
						correction = 0;

					}
					else {

						// Going straight enough
						pidDrive.setSetpoint(prevAngle);
						correction = pidDrive.calculate(getError());
		
					} */

					if (safeMode)
						arcadeDrive(forward * 0.5, turn * 0.5 + correction);						
					else
						arcadeDrive(forward, turn * 0.6 + correction);

				}
				if (button("stopMotors"))
					drive.stopMotor();
				else if (button("idleMotors"))
					drive.arcadeDrive(0, 0, false);
				
			}

			@Override
			protected boolean isFinished() {
				return false;
			}

			@Override
			public void initSendable(SendableBuilder builder) {

				super.initSendable(builder);
				builder.setSmartDashboardType("Anti-Drift");
				builder.addDoubleProperty("P", () -> Kp, (value) -> Kp = value);
				builder.addDoubleProperty("I", () -> Ki, (value) -> Ki = value);
				builder.addDoubleProperty("D", () -> Kd, (value) -> Kd = value);
				//builder.addDoubleProperty("Correction", () -> pidDrive.calculate(getError()), null);

				tankMode = builder.getEntry("tankMode");
				tankMode.setPersistent();
				tankMode.setDefaultBoolean(false);

			}

		});
		
	}

	@Override
	public void initSendable(SendableBuilder builder) {

		super.initSendable(builder);
		builder.addDoubleProperty("Left Velocity", () -> getVelocity(Side.LEFT), null);
		builder.addDoubleProperty("Right Velocity", () -> getVelocity(Side.RIGHT), null);
		builder.addDoubleProperty("Average Velocity", () -> getVelocity(), null);
		builder.addDoubleProperty("Average Distance", () -> getDistance(), null);
		//builder.addDoubleProperty("Gyro", () -> gyro.getAngle(), null);
		//builder.addDoubleProperty("Error", () -> getError(), null);
	
	}

}