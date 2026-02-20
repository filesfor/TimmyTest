package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeShooter extends SubsystemBase {
    private final PWMSparkMax m_intakeMotor  = new PWMSparkMax(2);
    private final PWMSparkMax m_shooterMotor = new PWMSparkMax(3);
    private final PWMSparkMax m_feederMotor  = new PWMSparkMax(4);

    public void setIntakeSpeed(double speed)  { m_intakeMotor.set(speed); }
    public void setShooterSpeed(double speed) { m_shooterMotor.set(speed); }
    public void setFeederSpeed(double speed)  { m_feederMotor.set(speed); }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Intake Speed",  m_intakeMotor.get());
        SmartDashboard.putNumber("Shooter Speed", m_shooterMotor.get());
        SmartDashboard.putNumber("Feeder Speed",  m_feederMotor.get());
    }
}