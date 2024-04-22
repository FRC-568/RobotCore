package frc.team568.robot.westDance.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class HeadSubsystem extends SubsystemBase {
    //=== motors ===
    private TalonSRX headMotor;
    
    private int headPort = 7;

    boolean override = false;

    public HeadSubsystem() {
        headMotor = new TalonSRX(headPort);
        headMotor.setInverted(true);
    }

    /**
     */
    public void setSpeed(double headSpeed) {
        headMotor.set(ControlMode.PercentOutput, headSpeed);
        
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
