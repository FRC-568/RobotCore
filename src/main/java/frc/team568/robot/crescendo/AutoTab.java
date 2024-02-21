package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.command.Aim;
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
import frc.team568.robot.crescendo.command.Closing;
import frc.team568.robot.crescendo.command.DownPneumatic;
import frc.team568.robot.crescendo.command.Intake;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Up;
import frc.team568.robot.crescendo.command.UpPneumatic;

public class AutoTab {
	
	public SendableChooser<Command> chooser = new SendableChooser<>();
	
	public AutoTab(RobotContainer container) {
		var pivot = container.pivot; 
		var drive = container.drive; 
		var jukebox = container.jukebox;
		//var dSolenoid = container.dSolenoid;
		
		Command aim = new Aim(pivot);
		Command autoScoreAndPreload = new AutoScoreAndPreload(drive, jukebox, pivot);
		Command closing = new Closing(pivot);
		//Command downpneumatic = new DownPneumatic(dSolenoid);
		Command intake = new Intake(jukebox, pivot);
		Command scoreamp = new ScoreAmp(jukebox, pivot);
		Command scorespeaker = new ScoreSpeaker(jukebox, pivot);
		Command up = new Up(pivot);
		//Command uppneumatic = new UpPneumatic(dSolenoid);
		
		chooser.setDefaultOption("Wait", null);
		chooser.addOption("Aim", aim);
		chooser.addOption("AutoScoreAndPreload", autoScoreAndPreload);
		chooser.addOption("Closing", closing);
		//chooser.addOption("Closing", downpneumatic);
		chooser.addOption("Intake", intake);
		chooser.addOption("Score AMP", scoreamp);
		chooser.addOption("Score Speaker", scorespeaker);
		chooser.addOption("Up", up);
		//chooser.addOption("Up Pneumatic", uppneumatic);

		OI.autoTab.add("Auto Program", chooser);

	}
}
