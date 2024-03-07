// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.crescendo.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.FunctionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Shoot extends SequentialCommandGroup {
  /** Creates a new Shoot. */
  double outtakeStartTime;
  public Shoot(JukeboxSubsystem jukebox) {
    super();
    addCommands(
      new RunCommand(() -> {jukebox.setIntakeSpeed(-0.1);}).withTimeout(0.25),
      new SpinUp(jukebox).withTimeout(3)
      .raceWith(
        new WaitCommand(1)
        .andThen(new RunCommand(() -> { jukebox.setIntakeSpeed(1); }).withTimeout(3))
      )
    );
  }
}
