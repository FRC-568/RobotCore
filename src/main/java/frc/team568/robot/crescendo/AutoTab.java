package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.command.Aim;
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
import frc.team568.robot.crescendo.command.Closing;
import frc.team568.robot.crescendo.command.DownPneumatic;

public class AutoTab {
	
	public SendableChooser<Command> chooser = new SendableChooser<>();

	
	public AutoTab(RobotContainer container) {
		chooser.setDefaultOption("Wait", null);
		
		chooser.addOption("Aim", new Aim(container.pivot));
		chooser.addOption("AutoScoreAndPreload", new AutoScoreAndPreload(container.drive, container.jukebox, container.pivot));
		chooser.addOption("Closing", new Closing(container.pivot));
		// chooser.addOption("Closing", new DownPneumatic(container.dSolenoid));

	}
}
