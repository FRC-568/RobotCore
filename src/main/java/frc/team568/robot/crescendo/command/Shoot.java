// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.team568.robot.crescendo.command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team568.robot.crescendo.subsystem.JukeboxSubsystem;

public class Shoot extends SequentialCommandGroup {
  double outtakeStartTime;

  public Shoot(JukeboxSubsystem jukebox) {
    addRequirements(jukebox);
    addCommands(
        new RunCommand(() -> jukebox.runIntake(-0.2)).withTimeout(0.25),
        new SpinUp(jukebox).withTimeout(3)
            .raceWith(
                new WaitCommand(2)
                  .andThen(new RunCommand(jukebox::runIntake).withTimeout(1))));
  }
}
