package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
<<<<<<< HEAD
import frc.team568.robot.crescendo.command.AutoScoreAndPreload;
=======
import frc.team568.robot.crescendo.command.Aim;
import frc.team568.robot.crescendo.command.Closing;
import frc.team568.robot.crescendo.command.DownPneumatic;
import frc.team568.robot.crescendo.command.Intake;
>>>>>>> a1b0507 (add one button outtake)
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;

public class AutoTab {

	public SendableChooser<Command> chooser = new SendableChooser<>();

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var drive = container.drive;
		var jukebox = container.jukebox;
<<<<<<< HEAD

		Command autoScoreAndPreload = new AutoScoreAndPreload(drive, jukebox, pivot);
		Command scoreamp = new ScoreAmp(jukebox, pivot);
		Command scorespeaker = new ScoreSpeaker(jukebox, pivot);

		chooser.setDefaultOption("Wait", null);
		chooser.addOption("AutoScoreAndPreload", autoScoreAndPreload);
		chooser.addOption("Score AMP", scoreamp);
		chooser.addOption("Score Speaker", scorespeaker);
=======
		//var dSolenoid = container.dSolenoid;
		
		Command closing = new Closing(pivot);
		//Command downpneumatic = new DownPneumatic(dSolenoid);
		Command intake = new Intake(jukebox, pivot);
		Command scoreamp = new ScoreAmp(jukebox, pivot);
		Command up = new Up(pivot);
		//Command uppneumatic = new UpPneumatic(dSolenoid);
		
		chooser.setDefaultOption("Wait", null);
		chooser.addOption("Closing", closing);
		//chooser.addOption("Closing", downpneumatic);
		chooser.addOption("Intake", intake);
		chooser.addOption("Score AMP", scoreamp);
		chooser.addOption("Up", up);
		//chooser.addOption("Up Pneumatic", uppneumatic);
>>>>>>> a1b0507 (add one button outtake)

		OI.autoTab.add("Auto Program", chooser);
	}

}
