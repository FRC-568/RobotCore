package frc.team568.robot.crescendo;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.command.GoToSpeaker;
import frc.team568.robot.crescendo.command.Intake;
import frc.team568.robot.crescendo.command.LookAtSpeaker;
import frc.team568.robot.crescendo.command.ScoreAmp;
import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Shoot;
import frc.team568.robot.crescendo.subsystem.PneumaticSubsystem;

public class AutoTab {

	public SendableChooser<Command> chooser = new SendableChooser<>();
	
	public PneumaticSubsystem lift;

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var jukebox = container.jukebox;
		var drive = container.drive;
		
		Command intake = new Intake(jukebox, pivot);
		Command scoreamp = new ScoreAmp(jukebox, pivot);
		Command scorespeaker = new ScoreSpeaker(jukebox, pivot);
		Command shoot = new Shoot(jukebox);
		Command lookatspeaker = new LookAtSpeaker(drive);
		Command gotospeaker = new GoToSpeaker(drive);
		Command togglepneumatic = lift.getToggleCommand();

		chooser.setDefaultOption("Wait", null);
		chooser.addOption("Intake", intake); // Runs an Intake to take in the note that may or may not be there for the bot
		chooser.addOption("Score AMP", scoreamp); // Scores in Amp
		chooser.addOption("Score Speaker", scorespeaker); // IMPORTANT: Assumes the robot is already in position. If used during tele-op, you should probbaly run 'Go Speaker' and 'Look Speaker' first
		chooser.addOption("Shoot (Note?)", shoot); // Just... shoots out the note?
		chooser.addOption("Look Speaker", lookatspeaker); // Makes the robot itself look at the speaker
		chooser.addOption("Go Speaker", gotospeaker); // Gets robot to position
		chooser.addOption("Toggle Pneumatic", togglepneumatic);

		OI.autoTab.add("Auto Program", chooser);
	}

}
