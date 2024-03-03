package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;

public class AutoTab {

	public SendableChooser<Command> chooser = new SendableChooser<>();

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var drive = container.drive;
		var jukebox = container.jukebox;

		Command autoScoreAndPreload = new AutoScoreAndPreload(drive, jukebox, pivot);
		Command scoreamp = new ScoreAmp(jukebox, pivot);
		Command scorespeaker = new ScoreSpeaker(jukebox, pivot);

		chooser.setDefaultOption("Wait", null);
		chooser.addOption("AutoScoreAndPreload", autoScoreAndPreload);
		chooser.addOption("Score AMP", scoreamp);
		chooser.addOption("Score Speaker", scorespeaker);

		OI.autoTab.add("Auto Program", chooser);
	}

}
