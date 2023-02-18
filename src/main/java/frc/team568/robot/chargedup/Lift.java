package frc.team568.robot.chargedup;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Lift extends SubsystemBase {
	// TODO: abstract out stuff like getting position into seperate functions

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

	private static double[] levels = {0, 50, 100, 150};

	private double setpoint = 0.0;
	private double timestamp = 0.0;
	private double dt = 0.02;

	public Lift(int motorPort, int switchPort) {
		liftMotor = new WPI_TalonSRX(motorPort);
        limitSwitch = new DigitalInput(switchPort);
		feedforward = new ElevatorFeedforward(kStatic, kG, kV, kA);
		pid = new PIDController(kP, kI, kD);
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
		liftMotor.set(input);
	}

	public void setLevel(int level) {
		timestamp = 0.0;
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
		// TODO: add acceleration stuff
		setVoltage(feedforward.calculate(profile.calculate(timestamp).velocity)
				+ pid.calculate(liftMotor.getSelectedSensorPosition(),
								profile.calculate(timestamp).position));
		timestamp += dt;
	}
}