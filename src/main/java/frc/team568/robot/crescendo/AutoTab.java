package frc.team568.robot.crescendo;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.team568.robot.crescendo.command.GoToAmp;
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
		//chooser.addOption("Score Speaker", new ScoreSpeaker(jukebox, pivot)); // IMPORTANT: Assumes the robot is already in position. If used during tele-op, you should probbaly run 'Go Speaker' first
		chooser.addOption("Shoot (Note?)", new Shoot(jukebox)); // Just... shoots out the note?
		chooser.addOption("Look Speaker", new LookAtSpeaker(drive)); // Makes the robot itself look at the speaker relative to where it is
		chooser.addOption("Go Speaker", new GoToSpeaker(drive)); // Gets robot to the speaker, and should automatically look straight at it.
		chooser.addOption("Go Amp", new GoToAmp(drive)); // Gets robot to Amp, and looks straight at it
		chooser.addOption("Score Preload and Nearby", AutoBuilder.buildAuto("ScorePreloadAndNearNotes"));

		OI.autoTab.add("Auto Program", chooser);
	}

	public Command getAutonomousCommand() {
		return Commands.waitSeconds(getDelayTime()).andThen(chooser.getSelected());
	}

	public double getDelayTime() {
		return delayTime.getDouble(0.0);
	}

}
