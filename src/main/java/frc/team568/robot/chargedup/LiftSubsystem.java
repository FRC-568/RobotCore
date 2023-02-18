package frc.team568.robot.chargedup;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LiftSubsystem extends SubsystemBase {
	// TODO: abstract out stuff like getting position into seperate functions
	// TODO: make stuff configurable from dashboard

	private WPI_TalonSRX liftMotor;
    private DigitalInput limitSwitch;

	private ElevatorFeedforward feedforward;
	private PIDController pid;
	private TrapezoidProfile profile;
	// TODO: figure out max vel + accel w/ feedforward
	private TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(2, 2);

	private static double kStatic = 0.0;
	private static double kV = 0.0;
	private static double kA = 0.0;
	private static double kG = 0.0;

	private static double kP = 0.0;
	private static double kI = 0.0;
	private static double kD = 0.0;
	private static double tolerance = 10;

	private static double[] levels = {0, 50, 100, 150};

	private double setpoint = 0.0;
	private double timestamp = 0.0;
	private Timer timer = new Timer();

	private boolean override = false;

	public LiftSubsystem(int motorPort, int switchPort) {
		liftMotor = new WPI_TalonSRX(motorPort);
		addChild("liftMotor", liftMotor);
        limitSwitch = new DigitalInput(switchPort);
		addChild("limitSwitch", limitSwitch);
		feedforward = new ElevatorFeedforward(kStatic, kG, kV, kA);
		pid = new PIDController(kP, kI, kD);
		pid.setTolerance(10);
	}

    public double getPosition() {
        return liftMotor.getSelectedSensorPosition();
    }

	// must be constantly called
    public void setVoltage(double voltage) {
		liftMotor.setVoltage(voltage);
	}

	// set and forget
	public void set(double input) {
		override = true;
		liftMotor.set(input + feedforward.calculate(input));
	}

	public boolean isFinished() {
		return pid.atSetpoint();
	}

	public void setLevel(int level) {
		override = false;
		timer.reset();
		profile = new TrapezoidProfile(
				constraints,
				new TrapezoidProfile.State(levels[level], 0.0),
				new TrapezoidProfile.State(liftMotor.getSelectedSensorPosition(), liftMotor.getSelectedSensorVelocity()));
	}

	@Override	
	public void periodic() {
		if (limitSwitch.get()) {
			liftMotor.setSelectedSensorPosition(0.0);
		}
		if (!override) {
			timestamp = timer.getFPGATimestamp();
			// TODO: add acceleration stuff
			setVoltage(feedforward.calculate(profile.calculate(timestamp).velocity)
					+ pid.calculate(liftMotor.getSelectedSensorPosition(),
									profile.calculate(timestamp).position));
		}
	}
}