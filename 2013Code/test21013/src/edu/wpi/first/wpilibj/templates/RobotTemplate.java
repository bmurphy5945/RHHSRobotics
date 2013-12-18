/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot 
{
    RobotDrive drive;
    Joystick leftStick,rightStick,turretStick;
    double leftValue,rightValue;
    Relay turretMotor;
    Jaguar shooter, shooter2;
    Relay angler,angler2;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {
        // RobotDrive uses PWM 1, 2, 3, and 4
        drive = new RobotDrive(1,2,3,4); // j/k
        
        // turretMotor uses Relay output 1
        turretMotor = new Relay(1);
        
        angler = new Relay(2);
        angler2 = new Relay(3);
        
        // Joystick: leftStick uses Joystick 1
        leftStick = new Joystick(1);
        
        // Joystick: rightStick uses Joystick 2
        rightStick = new Joystick(2);
        
        // Joystick: turretStick uses Joystick 3
        turretStick = new Joystick(3);
        
        shooter = new Jaguar(5);
        shooter2 = new Jaguar(6);
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
                if (leftStick.getTrigger())
            // Just pass leftStick.getY() to both arguments so that both sides get
            // the same value, hence forward/backward with one stick.
            // Technically, you could do:
            // drive.tankDrive(leftStick, leftStick);
            // ...but this makes more sense. :P DEAL WITH IT!!!!1!
            drive.tankDrive(leftStick.getY(), leftStick.getY());
        else
        {
            drive.tankDrive(leftStick.getY(), rightStick.getY());
        }
        shooter.set(-1*Math.abs(turretStick.getY()));
        shooter2.set(-1*Math.abs(turretStick.getY()));
        
         if (turretStick.getRawButton(2))
        {
            angler.set(Relay.Value.kForward);
            angler2.set(Relay.Value.kForward);
        }
        if (turretStick.getRawButton(3))
        {
            angler.set(Relay.Value.kOff);
            angler2.set(Relay.Value.kOff);
        }
        else
        {
            angler.set(Relay.Value.kOff);
            angler2.set(Relay.Value.kOff);
        }
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
