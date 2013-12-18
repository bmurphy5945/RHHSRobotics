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
public class IbrahimWorkingCode2013 extends IterativeRobot 
{
    RobotDrive drive;
    Joystick leftStick,rightStick,turretStick;
    double leftValue,rightValue;
    Relay turretMotor;
    Relay angler,angler2;
    Victor feeder;
    
    public void robotInit() 
    {
        
        //turretMotor = new Relay(1);
        turretStick = new Joystick(3);
        angler = new Relay(1);
        angler2 = new Relay(3);
        feeder = new Victor(7);

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        if (turretStick.getRawButton(4))
        {
            turretMotor.set(Relay.Value.kForward);
        }
        else if(turretStick.getRawButton(5))
        {
            turretMotor.set(Relay.Value.kReverse);
        }
        else
        {
           turretMotor.set(Relay.Value.kOff);
        }
        
        /*Angler*/
        
        if(turretStick.getRawButton(2))
        {
            angler.set(Relay.Value.kForward);
            angler2.set(Relay.Value.kForward);
        }
        else if(turretStick.getRawButton(3))
        {
            angler.set(Relay.Value.kReverse);
            angler2.set(Relay.Value.kReverse);
        }
        else
        {
            angler.set(Relay.Value.kOff);
            angler2.set(Relay.Value.kOff);
        }
            
        if(turretStick.getTrigger())
        {
            feeder.set(1.0);
        }
        else
        {
            feeder.set(0.0);
        }
        
        
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
