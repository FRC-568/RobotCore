package frc.team568.robot.chargedup;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LiftSubsystem extends SubsystemBase {
	// TODO: use built-in TalonSRX limit switch stuff?
	// TODO: figure out encoder stuff

    // === stage ===
	private WPI_TalonSRX stageMotor;
    private DigitalInput limitSwitch1;
	private static double feedforward1 = 0.0;
    private static double accel1 = 1024;
    private static double maxV1 = 2048;
	private static double kP1 = 0.0;
	private static double kI1 = 0.0;
	private static double kD1 = 0.0;

    // === carriage ===
	private WPI_TalonSRX carriageMotor;
	private DigitalInput limitSwitch2;
	private static double feedforward2 = 0.0;
    private static double accel2 = 1024;
    private static double maxV2 = 2048;
	private static double kP2 = 0.0;
	private static double kI2 = 0.0;
	private static double kD2 = 0.0;

    private static double neutralDeadband = 0.001;
    // all the way down, all the way up
    private static double[] STAGE_LEVELS = {0, 4096};
    // intake, collected, and outtake
	private static double[] CARRIAGE_LEVELS = {0, 4096, 8192};

	public LiftSubsystem(int stagePort, int carriagePort, int switchPort1, int switchPort2) {
		stageMotor = new WPI_TalonSRX(stagePort);
		addChild("stageMotor", stageMotor);
        limitSwitch1 = new DigitalInput(switchPort1);
		addChild("limitSwitch1", limitSwitch1);

        stageMotor.set(ControlMode.PercentOutput, 0);
        stageMotor.setNeutralMode(NeutralMode.Brake);
        stageMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
		stageMotor.configNeutralDeadband(neutralDeadband);
		stageMotor.configMotionAcceleration(accel1);
		stageMotor.configMotionCruiseVelocity(maxV1);
		// what is the slot index?
		stageMotor.config_kP(0, kP1);
		stageMotor.config_kI(0, kI1);
		stageMotor.config_kD(0, kD1);

		carriageMotor = new WPI_TalonSRX(carriagePort);
		addChild("carriageMotor", carriageMotor);
        limitSwitch2 = new DigitalInput(switchPort2);
		addChild("limitSwitch2", limitSwitch2);

		carriageMotor.set(ControlMode.PercentOutput, 0);
        carriageMotor.setNeutralMode(NeutralMode.Brake);
		carriageMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 30);
		carriageMotor.configNeutralDeadband(neutralDeadband);
		carriageMotor.configMotionAcceleration(accel2);
		carriageMotor.configMotionCruiseVelocity(maxV2);
		carriageMotor.config_kP(0, kP2);
		carriageMotor.config_kI(0, kI2);
		carriageMotor.config_kD(0, kD2);
	}

    public double getStagePos() {
        return stageMotor.getSelectedSensorPosition();
	}

	public double getCarriagePos() {
		return carriageMotor.getSelectedSensorPosition();
	}

	public void set(double input) {
		stageMotor.set(ControlMode.Velocity, input);
	}

	public void setLevel(int level) {
        switch (level) {
            case 0:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[0],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriageMotor.set(ControlMode.MotionMagic,
									CARRIAGE_LEVELS[0],
									DemandType.ArbitraryFeedForward,
									feedforward2);
				break;
			case 1:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[0],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriageMotor.set(ControlMode.MotionMagic,
									CARRIAGE_LEVELS[1],
									DemandType.ArbitraryFeedForward,
									feedforward2);
				break;
			case 2:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[0],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriageMotor.set(ControlMode.MotionMagic,
									CARRIAGE_LEVELS[2],
									DemandType.ArbitraryFeedForward,
									feedforward2);
				break;
			case 3:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[1],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriageMotor.set(ControlMode.MotionMagic,
									CARRIAGE_LEVELS[2],
									DemandType.ArbitraryFeedForward,
									feedforward2);
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
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Stage position", () -> getStagePos(), null);
		builder.addDoubleProperty("Carrage pos", () -> getCarriagePos(), null);
		builder.addDoubleProperty("ff1", () -> feedforward1, (value) -> feedforward1 = value);
		builder.addDoubleProperty("accel1", () -> accel1, (value) -> accel1 = value);
		builder.addDoubleProperty("maxV1", () -> maxV1, (value) -> maxV1 = value);
		builder.addDoubleProperty("kP1", () -> kP1, (value) -> kP1 = value);
		builder.addDoubleProperty("kI1", () -> kI1, (value) -> kI1 = value);
		builder.addDoubleProperty("kD1", () -> kD1, (value) -> kD1 = value);
		builder.addDoubleProperty("ff2", () -> feedforward2, (value) -> feedforward2 = value);
		builder.addDoubleProperty("accel2", () -> accel2, (value) -> accel2 = value);
		builder.addDoubleProperty("maxV2", () -> maxV2, (value) -> maxV2 = value);
		builder.addDoubleProperty("kP2", () -> kP2, (value) -> kP2 = value);
		builder.addDoubleProperty("kI2", () -> kI2, (value) -> kI2 = value);
		builder.addDoubleProperty("kD2", () -> kD2, (value) -> kD2 = value);
		builder.addDoubleProperty("STAGE_LEVELS[0]", () -> STAGE_LEVELS[0], (value) -> STAGE_LEVELS[0] = value);
		builder.addDoubleProperty("STAGE_LEVELS[1]", () -> STAGE_LEVELS[1], (value) -> STAGE_LEVELS[1] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[0]", () -> CARRIAGE_LEVELS[0], (value) -> CARRIAGE_LEVELS[0] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[1]", () -> CARRIAGE_LEVELS[1], (value) -> CARRIAGE_LEVELS[1] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[2]", () -> CARRIAGE_LEVELS[2], (value) -> CARRIAGE_LEVELS[2] = value);
	}
}
