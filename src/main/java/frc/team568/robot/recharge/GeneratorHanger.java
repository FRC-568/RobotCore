package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class GeneratorHanger extends SubsystemBase {

	private final double PULLER_SPEED = 0.3;

	private WPI_TalonSRX hangerPullerL;
	private WPI_TalonSRX hangerPullerR;

	public GeneratorHanger(RobotBase robot) {

		super(robot);

		hangerPullerL = new WPI_TalonSRX(configInt("hangerPullerL"));
		hangerPullerR = new WPI_TalonSRX(configInt("hangerPullerR"));
		hangerPullerL.setInverted(false);
		hangerPullerR.setInverted(true);

		initDefaultCommand();

	}

	public void initDefaultCommand() {

		setDefaultCommand(new CommandBase() {
		
			{
				addRequirements(GeneratorHanger.this);
			}

			@Override
			public void execute() {

				if (button("hangerUp")) {

					hangerPullerL.set(-PULLER_SPEED);
					hangerPullerR.set(-PULLER_SPEED);

				} else if (button("hangerDown")) {

					hangerPullerL.set(PULLER_SPEED);
					hangerPullerR.set(PULLER_SPEED);

				} else {

					hangerPullerL.set(axis("hangerL") * 0.3);
					hangerPullerR.set(axis("hangerR") * 0.3);

				}

			}
		
		});

	}

	@Override
	public String getConfigName() {
		return "hanger";
	}

}