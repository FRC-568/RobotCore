package frc.team568.robot.chargedup;

import java.util.function.Consumer;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LiftSubsystem extends SubsystemBase {
	// TODO: abstract out stuff like getting position into seperate functions
	// TODO: make stuff configurable from dashboard
	// TODO: use built-in TalonSRX limit switch stuff
	// TODO: figure out encoder stuff

    // === stage ===
	private WPI_TalonSRX stageMotor;
    private DigitalInput limitSwitch1;
	private ElevatorFeedforward feedforward1;
	// TODO: figure out max vel + accel w/ feedforward

    // feedforward constants
	private static double kStatic1 = 0.0;
	private static double kV1 = 0.0;
	private static double kA1 = 0.0;
	private static double kG1 = 0.0;

    // pid constants
	private static double kP1 = 0.0;
	private static double kI1 = 0.0;
	private static double kD1 = 0.0;

    // === carriage ===
	private WPI_TalonSRX carriageMotor;
	private DigitalInput limitSwitch2;
	private ElevatorFeedforward feedforward2;
	// TODO: figure out max vel + accel w/ feedforward

    // feedforward constants
	private static double kStatic2 = 0.0;
	private static double kV2 = 0.0;
	private static double kA2 = 0.0;
	private static double kG2 = 0.0;

    // pid constants
	private static double kP2 = 0.0;
	private static double kI2 = 0.0;
	private static double kD2 = 0.0;

    // all the way down, all the way up
    private static double[] STAGE_LEVELS = {0, 4096};
    // intake, collected, and outtake
	private static double[] CARRIAGE_LEVELS = {0, 4096, 8192};

	private Runnable setter;
	boolean override = false;

	public LiftSubsystem(int stagePort, int carriagePort, int switchPort1, int switchPort2) {
		// liftMotor = new WPI_TalonSRX(motorPort);
		stageMotor = new WPI_TalonSRX(stagePort);
		addChild("stageMotor", stageMotor);
        limitSwitch1 = new DigitalInput(switchPort1);
		addChild("limitSwitch1", limitSwitch1);
		feedforward1 = new ElevatorFeedforward(kStatic1, kG1, kV1, kA1);

        stageMotor.set(ControlMode.PercentOutput, 0);
        stageMotor.setNeutralMode(NeutralMode.Brake);
        stageMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
		stageMotor.configNeutralDeadband(0.001);
		stageMotor.configMotionAcceleration(1024);
		stageMotor.configMotionCruiseVelocity(2048);
		// what is the slot index?
		stageMotor.config_kP(0, kP1);
		stageMotor.config_kI(0, kI1);
		stageMotor.config_kD(0, kD1);

		carriageMotor = new WPI_TalonSRX(carriagePort);
		addChild("carriageMotor", carriageMotor);
        limitSwitch2 = new DigitalInput(switchPort2);
		addChild("limitSwitch2", limitSwitch2);
		feedforward2 = new ElevatorFeedforward(kStatic2, kG2, kV2, kA2);

		carriageMotor.set(ControlMode.PercentOutput, 0);
        carriageMotor.setNeutralMode(NeutralMode.Brake);
		/// i don't know what i'm doing here
        // carriageMotor.configRemoteFeedbackFilter(, RemoteSensorSource.CANCoder, 0);
		carriageMotor.configNeutralDeadband(0.001);
		carriageMotor.configMotionAcceleration(1024);
		carriageMotor.configMotionCruiseVelocity(2048);
		carriageMotor.config_kP(0, kP2);
		carriageMotor.config_kI(0, kI2);
		carriageMotor.config_kD(0, kD2);
	}

	// set and forget
	public void set(double input) {
		override = true;
		stageMotor.set(ControlMode.Velocity, input);
	}

	public void setLevel(int level) {
		override = false;
        switch (level) {
            case 0:
				setter = () -> {
						stageMotor.set(ControlMode.MotionMagic,
									STAGE_LEVELS[0],
									DemandType.ArbitraryFeedForward,
									feedforward1.calculate(stageMotor.getActiveTrajectoryVelocity()));
						carriageMotor.set(ControlMode.MotionMagic,
										  CARRIAGE_LEVELS[0],
										  DemandType.ArbitraryFeedForward,
										  feedforward2.calculate(carriageMotor.getActiveTrajectoryVelocity()));
						};
				break;
			case 1:
				setter = () -> {
						stageMotor.set(ControlMode.MotionMagic,
									STAGE_LEVELS[0],
									DemandType.ArbitraryFeedForward,
									feedforward1.calculate(stageMotor.getActiveTrajectoryVelocity()));
						carriageMotor.set(ControlMode.MotionMagic,
										  CARRIAGE_LEVELS[1],
										  DemandType.ArbitraryFeedForward,
										  feedforward2.calculate(carriageMotor.getActiveTrajectoryVelocity()));
						};
				break;
			case 2:
				setter = () -> {
						stageMotor.set(ControlMode.MotionMagic,
									STAGE_LEVELS[0],
									DemandType.ArbitraryFeedForward,
									feedforward1.calculate(stageMotor.getActiveTrajectoryVelocity()));
						carriageMotor.set(ControlMode.MotionMagic,
										  CARRIAGE_LEVELS[2],
										  DemandType.ArbitraryFeedForward,
										  feedforward2.calculate(carriageMotor.getActiveTrajectoryVelocity()));
						};
				break;
			case 3:
				setter = () -> {
						stageMotor.set(ControlMode.MotionMagic,
									STAGE_LEVELS[1],
									DemandType.ArbitraryFeedForward,
									feedforward1.calculate(stageMotor.getActiveTrajectoryVelocity()));
						carriageMotor.set(ControlMode.MotionMagic,
											CARRIAGE_LEVELS[2],
											DemandType.ArbitraryFeedForward,
											feedforward2.calculate(carriageMotor.getActiveTrajectoryVelocity()));
						};
				break;
		}
	}

	@Override	
	public void periodic() {
		if (limitSwitch1.get()) {
			stageMotor.setSelectedSensorPosition(0.0);
		}
		if (limitSwitch2.get()) {
			carriageMotor.setSelectedSensorPosition(0.0);
		}
		if (!override) {
			setter.run();
		}
	}
}
