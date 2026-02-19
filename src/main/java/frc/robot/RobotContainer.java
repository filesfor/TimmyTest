package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.IntakeShooter;
import frc.robot.commands.Autos;

public class RobotContainer {
    // 1. Grab our Lego Bricks (Subsystems)
    private final Drivetrain m_drivetrain = new Drivetrain();
    private final IntakeShooter m_intakeShooter = new IntakeShooter();

    // 2. Grab our Controller (USB Port 0)
    private final CommandXboxController m_driverController = new CommandXboxController(0);

    public RobotContainer() {
        // 3. Set default driving behavior (Arcade Drive on the sticks)
        m_drivetrain.setDefaultCommand(
            new RunCommand(
                () -> m_drivetrain.arcadeDrive(
                    -m_driverController.getLeftY(), 
                    -m_driverController.getRightX()
                ),
                m_drivetrain
            )
        );

        // 4. Bind the buttons
        configureBindings();
    }

    
    private void configureBindings() {
        // L1: Intake (70% speed)
        m_driverController.leftBumper().whileTrue(
            new StartEndCommand(
                () -> m_intakeShooter.setIntakeSpeed(0.7), 
                () -> m_intakeShooter.setIntakeSpeed(0.0), 
                m_intakeShooter
            )
        );

        // R1: Rev Shooter (90% speed)
        m_driverController.rightBumper().whileTrue(
            new StartEndCommand(
                () -> m_intakeShooter.setShooterSpeed(0.9), 
                () -> m_intakeShooter.setShooterSpeed(0.0), 
                m_intakeShooter
            )
        );

        // R2: Feed/Shoot (100% speed) - Trigger axis > 0.5 means pressed halfway
        m_driverController.rightTrigger(0.5).whileTrue(
            new StartEndCommand(
                () -> m_intakeShooter.setFeederSpeed(1.0), 
                () -> m_intakeShooter.setFeederSpeed(0.0), 
                m_intakeShooter
            )
        );
    }

    /**
     * The background Robot.java file requires this method to exist!
     * It asks: "What should the robot do during the 15-second Auto period?"
     */
    public Command getAutonomousCommand() {
        // Run the 2-second drive routine we just built!
        return Autos.simpleDriveAuto(m_drivetrain);
    }
}