/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;


public class Robot2013xbox extends IterativeRobot
{
    RobotDrive drive;
    Joystick leftStick,rightStick,turretStick;
    Joystick xboxShooter,xboxDriver;
    Jaguar shooterClose, shooterFar,turret;
    Relay anglerLeft,anglerRight;
    Victor feeder;
    
    public void robotInit()
    {
        leftStick=new Joystick(1);
        rightStick=new Joystick(2);
        xboxShooter=new Joystick(3);     
        drive = new RobotDrive(1,2,3,4);
        shooterClose=new Jaguar(5);
        shooterFar=new Jaguar(6);
        anglerLeft=new Relay(1);
        anglerRight=new Relay(3);
        feeder=new Victor(7);
        turret=new Jaguar(8);
    }
    public void autonomousPeriodic() {

    }
    public void teleopPeriodic()
    {
        //driving///////////////////////////////////////////////////////////////////////////////
        if (leftStick.getTrigger())
        {
            drive.tankDrive(.6*leftStick.getY(), .6*leftStick.getY());
        }else{
            drive.tankDrive(.6*leftStick.getY(), .6*rightStick.getY());
        }
        //shooting///////////////////////////////////////////////////////////////////////////////////
        if(xboxShooter.getRawButton(5))
        {
            shooterClose.set(-1.0);
            shooterFar.set(-1.0);
        }else{
            shooterClose.set(0.0);
            shooterFar.set(0.0); 
        }
        if(xboxShooter.getRawButton(6))
        {
            feeder.set(-.1);//redo//////////////////////////////////////////////////////////////////////////////
        }else{
            feeder.set(0.0);
        }
        if(Math.abs(xboxShooter.getRawAxis(4))>.5)
        {
            if(xboxShooter.getRawAxis(4)>.0)
            {
                anglerLeft.set(Relay.Value.kForward);
                anglerRight.set(Relay.Value.kForward);
            }else{
                anglerLeft.set(Relay.Value.kReverse);
                anglerRight.set(Relay.Value.kReverse);
            }
        }else{
            anglerLeft.set(Relay.Value.kOff);
            anglerRight.set(Relay.Value.kOff);
        }
        if(Math.abs(xboxShooter.getY())>.5)
        {
            if(xboxShooter.getY()>.0)
            {
                anglerLeft.set(Relay.Value.kForward);
            }else{
                anglerLeft.set(Relay.Value.kReverse);
            }
        }else{
            anglerLeft.set(Relay.Value.kOff);
        }
        turret.set(Math.abs(xboxShooter.getZ())*xboxShooter.getZ());
        
    }
    
    public void testPeriodic() {
    
    }
    
}
