package frc.team568.robot.crescendo;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team568.robot.crescendo.command.GoToSpeaker;
import frc.team568.robot.crescendo.command.LookAtSpeaker;
import frc.team568.robot.crescendo.command.ScoreAmp;
//import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Shoot;

public class AutoTab {
	public final SendableChooser<Command> chooser = new SendableChooser<>();
	public final GenericEntry delayTime;

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var jukebox = container.jukebox;
		var drive = container.drive;

		delayTime = OI.autoTab.add("Delay at Start", "double", 0.0).getEntry("double");

		chooser.setDefaultOption("Wait", null);
		chooser.addOption("Score AMP", new ScoreAmp(jukebox, pivot)); // Scores in Amp
		//chooser.addOption("Score Speaker", new ScoreSpeaker(jukebox, pivot)); // IMPORTANT: Assumes the robot is already in position. If used during tele-op, you should probbaly run 'Go Speaker' and 'Look Speaker' first
		chooser.addOption("Shoot (Note?)", new Shoot(jukebox)); // Just... shoots out the note?
		chooser.addOption("Look Speaker", new LookAtSpeaker(drive)); // Makes the robot itself look at the speaker
		chooser.addOption("Go Speaker", new GoToSpeaker(drive)); // Gets robot to position
		chooser.addOption("Score Preload and Nearby", AutoBuilder.buildAuto("ScorePreloadAndNearNotes"));

		OI.autoTab.add("Auto Program", chooser);
	}

	public double getDelayTime() {
		return delayTime.getDouble(0.0);
	}

}
