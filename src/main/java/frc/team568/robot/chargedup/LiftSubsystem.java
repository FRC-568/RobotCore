package frc.team568.robot.chargedup;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.SparkRelativeEncoder;
import com.revrobotics.CANSparkBase.IdleMode;
//import com.revrobotics.CANSparkBase.SoftLimitDirection;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LiftSubsystem extends SubsystemBase {
    // === stage ===
	private WPI_TalonSRX stageMotor;
    private DigitalInput limitSwitch1;
	private static double feedforward1 = 0.0;
    private static double accel1 = 1024;
    private static double maxV1 = 2048;
	private static double kP1 = 0.0;
	private static double kI1 = 0.0;
	private static double kD1 = 0.0;
	private static double tolerance1 = 10;

    // === carriage ===
	private CANSparkMax carriageMotor;
	private SparkPIDController carriagePid;
	private RelativeEncoder carriageEncoder;
	private DigitalInput limitSwitch2;
	private static double feedforward2 = 0.0;
    private static double accel2 = 1024;
    private static double maxV2 = 2048;
	private static double kP2 = 0.0;
	private static double kI2 = 0.0;
	private static double kD2 = 0.0;
	private static double tolerance2 = 10;

    private static double neutralDeadband = 0.001;
    // all the way down, mid, all the way up
    private static double[] STAGE_LEVELS = {0, 4096, 8192};
    // intake, collected, prep, PUSHHHH
	private static double[] CARRIAGE_LEVELS = {0, 4096, 8192, 16384};
	private static int level1 = 0;
	private static int level2 = 1;

	boolean override = false;

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
		stageMotor.configPeakCurrentLimit(20);
		stageMotor.configPeakCurrentDuration(250);
		stageMotor.configContinuousCurrentLimit(20);
		stageMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(false, 24, 30.0, 100.0));
		stageMotor.configOpenloopRamp(0.5);
		stageMotor.configForwardSoftLimitThreshold(STAGE_LEVELS[2]);
		stageMotor.configForwardSoftLimitEnable(false);
		stageMotor.setSelectedSensorPosition(0.0);

		carriageMotor = new CANSparkMax(carriagePort, MotorType.kBrushed);
		carriageMotor.setOpenLoopRampRate(0.5);
		carriagePid = carriageMotor.getPIDController();
		carriageEncoder = carriageMotor.getEncoder(SparkRelativeEncoder.Type.kQuadrature, 8192);
		carriageEncoder.setPosition(0.0);
		// addChild("carriageMotor", carriageMotor);
        limitSwitch2 = new DigitalInput(switchPort2);
		// addChild("limitSwitch2", limitSwitch2);

		carriageMotor.set(0);
        carriageMotor.setIdleMode(IdleMode.kBrake);
		// scale issues: in rotations
		// carriageMotor.setSoftLimit(SoftLimitDirection.kForward, CARRIAGE_LEVELS[3]);
		// carriageMotor.d; ??? deadband?
		carriagePid.setSmartMotionMaxVelocity(maxV2, 0);
		carriagePid.setSmartMotionMaxAccel(accel2, 0);
		carriagePid.setP(kP2);
		carriagePid.setI(kI2);
		carriagePid.setD(kD2);
		carriagePid.setFF(feedforward2);
	}

    public double getStagePos() {
        return stageMotor.getSelectedSensorPosition();
	}

	public double getCarriagePos() {
		return carriageEncoder.getPosition();
	}

	public boolean onTarget() {
		return (Math.abs(getStagePos()-STAGE_LEVELS[level1]) < tolerance1)
			   && (Math.abs(getCarriagePos()-CARRIAGE_LEVELS[level2]) < tolerance2);
	}

	public void setCarriage(double input) {
		carriageMotor.set(input);
	}

	public void setStage(double input) {
		stageMotor.set(input);
	}

	// intake, collected, low, mid, high, PUSHHHH
	public void setLevel(int level) {
        switch (level) {
            case 0:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[0],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriagePid.setReference(CARRIAGE_LEVELS[0], CANSparkMax.ControlType.kSmartMotion);
				level1 = 0;
				level2 = 0;
				break;
			case 1:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[0],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriagePid.setReference(CARRIAGE_LEVELS[1], CANSparkMax.ControlType.kSmartMotion);
				level1 = 0;
				level2 = 1;
				break;
			case 2:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[0],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriagePid.setReference(CARRIAGE_LEVELS[2], CANSparkMax.ControlType.kSmartMotion);
				level1 = 0;
				level2 = 2;
				break;
			case 3:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[1],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriagePid.setReference(CARRIAGE_LEVELS[2], CANSparkMax.ControlType.kSmartMotion);
				level1 = 1;
				level2 = 2;
				break;
			case 4:
				stageMotor.set(ControlMode.MotionMagic,
							STAGE_LEVELS[2],
							DemandType.ArbitraryFeedForward,
							feedforward1);
				carriagePid.setReference(CARRIAGE_LEVELS[2], CANSparkMax.ControlType.kSmartMotion);
				level1 = 2;
				level2 = 2;
				break;
			case 5:
				carriagePid.setReference(CARRIAGE_LEVELS[3], CANSparkMax.ControlType.kSmartMotion);
				level2 = 3;
				break;
		}
	}

	@Override
	public void periodic() {
		if (!limitSwitch1.get()) {
			stageMotor.setSelectedSensorPosition(0.0);
		}
		if (!limitSwitch2.get()) {
			carriageEncoder.setPosition(0.0);
		}
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		super.initSendable(builder);
		builder.addDoubleProperty("Stage position", () -> getStagePos(), null);
		builder.addDoubleProperty("Carriage pos", () -> getCarriagePos(), null);
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
		builder.addDoubleProperty("STAGE_LEVELS[2]", () -> STAGE_LEVELS[2], (value) -> STAGE_LEVELS[2] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[0]", () -> CARRIAGE_LEVELS[0], (value) -> CARRIAGE_LEVELS[0] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[1]", () -> CARRIAGE_LEVELS[1], (value) -> CARRIAGE_LEVELS[1] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[2]", () -> CARRIAGE_LEVELS[2], (value) -> CARRIAGE_LEVELS[2] = value);
		builder.addDoubleProperty("CARRIAGE_LEVELS[3]", () -> CARRIAGE_LEVELS[3], (value) -> CARRIAGE_LEVELS[3] = value);
	}
}
