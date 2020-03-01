package frc.team568.robot.recharge;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team568.robot.RobotBase;
import frc.team568.robot.subsystems.SubsystemBase;

public class Intake extends SubsystemBase {

	private final double INTAKE_SPEED = -1;
	private double intakePressedHold = 0;

	private VictorSPX intakeWheels;
	private DoubleSolenoid extenderLeft;
	private DoubleSolenoid extenderRight;

	public Intake(RobotBase robot) {

		super(robot);

		intakeWheels = new VictorSPX(configInt("intakeWheels"));
		
		//extenderLeft = new DoubleSolenoid(configInt("extenderLeftOut"), configInt("extenderLeftIn"));
		//extenderRight = new DoubleSolenoid(configInt("extenderRightOut"), configInt("extenderRightIn"));
		//extenderLeft.set(Value.kReverse);
		//extenderRight.set(Value.kReverse);

		initDefaultCommand();

	}

	public void initDefaultCommand() {

		setDefaultCommand(new CommandBase() {
		
			{
				addRequirements(Intake.this);
			}

			@Override
			public void execute() {

				// wheels
				if (button("intake"))
					intakeWheels.set(ControlMode.PercentOutput, INTAKE_SPEED);
				else 
					intakeWheels.set(ControlMode.PercentOutput, 0);
/*
				// solenoids
				if (button("intakeExtenderToggle"))
					intakePressedHold++;
				else
					intakePressedHold = 0;
				if (intakePressedHold == 1) {

					if (extenderLeft.get() == Value.kForward) {

						extenderLeft.set(Value.kReverse);
						extenderRight.set(Value.kReverse);

					} else {

						extenderLeft.set(Value.kForward);
						extenderRight.set(Value.kForward);

					}

				}
*/			
			}

		});

	}

	@Override
	public String getConfigName() {
		return "intake";
	}

}