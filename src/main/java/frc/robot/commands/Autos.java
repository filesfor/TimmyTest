package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.Drivetrain;

public final class Autos {
    
  /** A simple routine that drives forward at 50% speed for 2 seconds. */
  public static Command simpleDriveAuto(Drivetrain drivetrain) {
      // 1. Run the arcadeDrive method (0.5 forward, 0.0 rotation)
      return new RunCommand(() -> drivetrain.arcadeDrive(0.5, 0.0), drivetrain)
          // 2. Stop after 2 seconds
          .withTimeout(2.0)
          // 3. Make sure the motors are set to 0 when it finishes
          .andThen(() -> drivetrain.arcadeDrive(0.0, 0.0));
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}