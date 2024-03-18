package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.command.Intake;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.subsystem.PneumaticSubsystem;

public class AutoTab {

	public SendableChooser<Command> chooser = new SendableChooser<>();
	
	public PneumaticSubsystem lift;

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var jukebox = container.jukebox;
		
		Command intake = new Intake(jukebox, pivot);
		Command scoreamp = new ScoreAmp(jukebox, pivot);
		Command togglepneumatic = lift.getToggleCommand();

		chooser.setDefaultOption("Wait", null);
		chooser.addOption("Intake", intake);
		chooser.addOption("Score AMP", scoreamp);
		chooser.addOption("Toggle Pneumatic", togglepneumatic);
		
		OI.autoTab.add("Auto Program", chooser);
	}

}
