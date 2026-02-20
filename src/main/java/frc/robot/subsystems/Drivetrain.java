package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.RobotController;

public class Drivetrain extends SubsystemBase {

    // --- Hardware ---
    private final PWMSparkMax m_leftMotor  = new PWMSparkMax(0);
    private final PWMSparkMax m_rightMotor = new PWMSparkMax(1);
    private final DifferentialDrive m_drive;

    // Track voltages manually so the sim gets clean values
    private double m_leftVolts  = 0.0;
    private double m_rightVolts = 0.0;

    // --- Simulation ---
    private final Field2d m_field = new Field2d();

    // Struct publisher for AdvantageScope 2026 - Pose3d for proper 3D rotation
    private final StructPublisher<Pose3d> m_posePublisher = NetworkTableInstance.getDefault()
        .getStructTopic("RobotPose", Pose3d.struct).publish();
    private final DifferentialDrivetrainSim m_driveSim = new DifferentialDrivetrainSim(
        DCMotor.getNEO(2), // 2 NEO motors per side
        7.29,              // Gearbox reduction
        0.5,               // Moment of inertia (kg*m^2) - reduced for snappier sim response
        10.0,              // Robot mass (kg) - reduced for snappier sim response
        0.0762,            // Wheel radius (meters) - 6 inch wheels
        0.7112,            // Trackwidth (meters)
        null               // No measurement noise
    );

    public Drivetrain() {
        m_rightMotor.setInverted(true);
        m_drive = new DifferentialDrive(m_leftMotor, m_rightMotor);
        m_drive.setDeadband(0.0);         // We'll handle deadband ourselves
        m_drive.setMaxOutput(1.0);
        SmartDashboard.putData("Field", m_field);
    }

    public Pose3d getPose() {
        return new Pose3d(m_driveSim.getPose());
    }

    public void arcadeDrive(double forwardSpeed, double rotationSpeed) {        // Apply deadband manually so the robot stops instantly when sticks are released
        forwardSpeed  = Math.abs(forwardSpeed)  > 0.05 ? forwardSpeed  : 0.0;
        rotationSpeed = Math.abs(rotationSpeed) > 0.05 ? rotationSpeed : 0.0;

        m_drive.arcadeDrive(rotationSpeed, forwardSpeed, false); // false = no input squaring = snappier

        // Capture the voltages that were just sent to the motors.
        // Right motor is inverted so we negate it to get the true physical direction.
        double battery = RobotController.getBatteryVoltage();
        m_leftVolts  =  m_leftMotor.get()  * battery;
        m_rightVolts = -m_rightMotor.get() * battery;
    }

    @Override
    public void simulationPeriodic() {
        m_driveSim.setInputs(m_leftVolts, m_rightVolts);
        m_driveSim.update(0.02);
        m_field.setRobotPose(m_driveSim.getPose());

        // Publish as Pose3d for AdvantageScope 2026 - includes full 3D rotation
        m_posePublisher.set(new Pose3d(m_driveSim.getPose()));

        // Zero voltages after each sim step so they don't persist into the next loop
        m_leftVolts  = 0.0;
        m_rightVolts = 0.0;
    }
}