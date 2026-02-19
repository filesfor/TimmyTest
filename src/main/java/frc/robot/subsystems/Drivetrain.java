package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
    private final PWMSparkMax m_leftMotor = new PWMSparkMax(0);
    private final PWMSparkMax m_rightMotor = new PWMSparkMax(1);
    private final DifferentialDrive m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);

    public Drivetrain() {
        m_rightMotor.setInverted(true); 
    }

    public void arcadeDrive(double forwardSpeed, double rotationSpeed) {
        m_drive.arcadeDrive(forwardSpeed, rotationSpeed);
    }
}