package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class GeneratorHanger extends SubsystemBase {

	private final double PULLER_SPEED = 0.5;

	private DoubleSolenoid hangerExtenderL;
	private DoubleSolenoid hangerExtenderR;

	private VictorSPX hangerPullerL;
	private VictorSPX hangerPullerR;
	private VictorSPX shifterWheel;

	public GeneratorHanger(RobotBase robot) {

		super(robot);

		hangerExtenderL = new DoubleSolenoid(configInt("extenderHangLeftOut"), configInt("extenderHangLeftIn"));
		hangerExtenderR = new DoubleSolenoid(configInt("extenderHangRightOut"), configInt("extenderHangRightIn"));

		hangerPullerL = new VictorSPX(configInt("hangerPullerL"));
		hangerPullerR = new VictorSPX(configInt("hangerPullerR"));
		shifterWheel = new VictorSPX(configInt("shifterWheel"));

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

					hangerExtenderL.set(Value.kForward);
					hangerPullerL.set(ControlMode.PercentOutput, PULLER_SPEED);

				} else if (button("hangerDown")) {

					

				}

			}
		
		});

	}

}