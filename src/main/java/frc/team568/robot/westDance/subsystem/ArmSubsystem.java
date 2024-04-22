package frc.team568.robot.westDance.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmSubsystem extends SubsystemBase {
    //=== motors ===
    private TalonSRX leftArmMotor;
    private TalonSRX rightArmMotor;
    
    private int leftArmPort = 7;
    private int rightArmPort = 8;

    public ArmSubsystem() {
        leftArmMotor = new TalonSRX(leftArmPort);
        leftArmMotor.setInverted(true);

        rightArmMotor = new TalonSRX(rightArmPort);
        rightArmMotor.setInverted(false); // Adjust inversion as necessary
    }

    /**
     * Sets the speed of both left and right arm motors.
     * @param leftArmSpeed The speed for the left arm motor.
     * @param rightArmSpeed The speed for the right arm motor.
     */
    public void setSpeed(double leftArmSpeed, double rightArmSpeed) {
        leftArmMotor.set(ControlMode.PercentOutput, leftArmSpeed);
        rightArmMotor.set(ControlMode.PercentOutput, rightArmSpeed);
    }

    @Override
    public void periodic() {
        // Update any periodic functionality here
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        // Add any Sendable data if needed
    }
}
