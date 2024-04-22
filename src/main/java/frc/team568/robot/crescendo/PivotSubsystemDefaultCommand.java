package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.subsystem.PivotSubsystem;

public class PivotSubsystemDefaultCommand extends Command{
	public PivotSubsystem aim;

	public PivotSubsystemDefaultCommand(PivotSubsystem aim){
		this.aim = aim;
		addRequirements(aim);
	
	}
}
