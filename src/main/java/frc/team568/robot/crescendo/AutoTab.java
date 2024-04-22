package frc.team568.robot.crescendo;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.crescendo.command.GoToAmp;
import frc.team568.robot.crescendo.command.GoToSpeaker;
import frc.team568.robot.crescendo.command.HomePivot;
import frc.team568.robot.crescendo.command.LookAtAmp;
import frc.team568.robot.crescendo.command.LookAtSpeaker;
import frc.team568.robot.crescendo.command.ScoreAmp;
//import frc.team568.robot.crescendo.command.ScoreSpeaker;
import frc.team568.robot.crescendo.command.Shoot;

public class AutoTab {
	public final SendableChooser<Command> preloadChooser = new SendableChooser<>();
	public final SendableChooser<Command> startPositionChooser = new SendableChooser<>();
	public final SendableChooser<Alliance> allianceChooser = new SendableChooser<>();
	// public final GenericEntry delayTime;

	public AutoTab(RobotContainer container) {
		var pivot = container.pivot;
		var jukebox = container.jukebox;
		var drive = container.drive;

		// delayTime = OI.autoTab.add("Delay at Start", "double", 0.0).getEntry("double");

		preloadChooser.setDefaultOption("bruh (wait)", new WaitCommand(1.0));
		preloadChooser.addOption(
			"EL SUSSY WUSSY AMOGUS (shoot preload)", 
			new RunCommand(
				() -> pivot.moveToward(10), 
				pivot
			)
			.withTimeout(5.0)
			.andThen(new Shoot(jukebox))
		);
		
		startPositionChooser.addOption(
			"amp side RED (leave complicated)", 
			new RunCommand(
				() -> container.drive.drive(1.0, 0, 0), container.drive
			).withTimeout(1.0)
			.andThen(
				() -> container.drive.drive(0, 0, 1 / 3.0), 
				container.drive
			).withTimeout(1.0)
			.andThen(
				() -> container.drive.drive(4 / 3.0, 0, 0), 
				container.drive
			).withTimeout(1.0)
		);
		startPositionChooser.addOption(
			"amp side BLUE (leave complicated)", 
			new RunCommand(
				() -> container.drive.drive(1.0, 0, 0), container.drive
			).withTimeout(1.0)
			.andThen(
				() -> container.drive.drive(0, 0, -1 / 3.0), 
				container.drive
			).withTimeout(1.0)
			.andThen(
				() -> container.drive.drive(4 / 3.0, 0, 0), 
				container.drive
			).withTimeout(1.0)
		);
		startPositionChooser.addOption("middle (dont move dt)", new WaitCommand(1.0));
		startPositionChooser.addOption("source (leave straight)", new RunCommand(() -> container.drive.drive(0.3, 0, 0), container.drive).withTimeout(30.0));
		// chooser.addOption("Score AMP", new ScoreAmp(jukebox, pivot)); // Scores in Amp
		// // chooser.addOption("Score Speaker", new ScoreSpeaker(jukebox, pivot)); // IMPORTANT: Assumes the robot is already in position. If used during tele-op, you should probbaly run 'Go Speaker' and 'Look Speaker' first
		// chooser.addOption("Shoot (Note?)", new Shoot(jukebox)); // Just... shoots out the note?
		// chooser.addOption("Look Speaker", new LookAtSpeaker(drive)); // Makes the robot itself look at the SPEAKER relative to where it is
		// chooser.addOption("Look Amp", new LookAtAmp(drive)); // Makes the robot itself look at the AMP relative to where it is
		// chooser.addOption("Go Speaker", new GoToSpeaker(drive)); // Gets robot to a set point at the speaker, and looks straight at it
		// chooser.addOption("Go Amp", new GoToAmp(drive)); // Gets robot to a set point at the Amp, and looks straight at it
		// chooser.addOption("Score Preload and Nearby", AutoBuilder.buildAuto("ScorePreloadAndNearNotes"));

		OI.autoTab.add("Auto Program", preloadChooser);
	}
	// source side, drive forwards a lot, amp side forward 3 feet, turn x degreses, drive forward

	public Command getAutonomousCommand() {
		// return Commands.waitSeconds(getDelayTime()).andThen(chooser.getSelected());
		// if (startPositionChooser.getSelected().equals(new RunCommand(() -> container.drive.drive(1.0, 0, 0), container.drive).withTimeout(1.0)))
			return preloadChooser.getSelected()
			.andThen(
				startPositionChooser.getSelected()
			);
	}

	// public double getDelayTime() {
	// 	return delayTime.getDouble(0.0);
	// }

}
