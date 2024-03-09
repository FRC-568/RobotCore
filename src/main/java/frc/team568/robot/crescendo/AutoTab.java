package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoTab {

	public SendableChooser<Command> chooser = new SendableChooser<>();

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var drive = container.drive;
		var jukebox = container.jukebox;
		//var dSolenoid = container.dSolenoid;
		
		//Command downpneumatic = new DownPneumatic(dSolenoid);
		//Command intake = new Intake(jukebox, pivot);
		//Command scoreamp = new ScoreAmp(jukebox, pivot);
		//Command uppneumatic = new UpPneumatic(dSolenoid);
		/* 
		chooser.setDefaultOption("Wait", null);
		chooser.addOption("Intake", intake);
		chooser.addOption("Score AMP", scoreamp);
		OI.autoTab.add("Auto Program", chooser);
		*/
	}

}
