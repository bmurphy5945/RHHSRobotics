/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;


public class Robot2013xbox extends IterativeRobot
{
    
    Joystick leftStick,rightStick,turretStick;
    Joystick xboxShooter,xboxDriver;
    RobotDrive drive;
    Jaguar shooterClose, shooterFar,turret;
    Relay anglerLeft,anglerRight,feeder;
    Victor climber;
    
    AxisCamera camera;
    
    DigitalInput anglerSwitch; //Limit switch that stops the shooter from coming down to far
    boolean debounce = false;
    public void robotInit()
    {
        leftStick = new Joystick(1); //Port 1
        rightStick = new Joystick(2); //Port 2
        xboxShooter = new Joystick(3); //Port 3
        xboxDriver = new Joystick(4);
        drive = new RobotDrive(1,2,3,4); //Initializes the Robot Drive
        shooterClose = new Jaguar(5);
        shooterFar = new Jaguar(6);
        turret = new Jaguar(7);
        feeder = new Relay(7);//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!
        anglerLeft = new Relay(1);//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!
        anglerRight = new Relay(8);//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!//RELAY INPUT 5 NOT FUNCTIONAL!
        climber = new Victor(8);
        
        camera = AxisCamera.getInstance();
        camera.writeRotation(AxisCamera.RotationT.k180);
        
        anglerSwitch = new DigitalInput(1);
    }
    public void autonomousPeriodic()
    {
        
    }
    public void teleopPeriodic()
    {
        //driving///////////////////////////////////////////////////////////////////////////////
        /*if(xboxDriver.getRawButton(5))
        {
            drive.tankDrive(xboxDriver.getRawAxis(4),xboxDriver.getRawAxis(4));
        }else if(xboxDriver.getRawButton(6)){
            //arcade drive          ////////////////////////////////////////////////////////////////////////
        }else{
             drive.tankDrive(xboxDriver.getY(),xboxDriver.getRawAxis(4));
        }*/
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if(leftStick.getTrigger()){
            drive.tankDrive(leftStick.getY(), leftStick.getY());
        }else if(rightStick.getTrigger()){
                //arcadeDrive//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }else{
            drive.tankDrive(leftStick.getY(), rightStick.getY());
        }
        //shooting///////////////////////////////////////////////////////////////////////////////////
        // 16 presses MAX
        
        if(xboxShooter.getRawButton(4))
        {
            climber.set(.50);
        }
        else if(xboxShooter.getRawButton(2))
        {
           climber.set(-0.50);
        }
        else
        {
            climber.set(0);
        }
        
        if(xboxShooter.getRawButton(5))
        {
            shooterClose.set(-1.0);
            shooterFar.set(-1.0);
        } else if (xboxShooter.getRawButton(7))
        {
            shooterClose.set(-0.9);
            shooterFar.set(-0.9);
        }
        else
        {
            shooterClose.set(0.0);
            shooterFar.set(0.0); 
        }
        if( (xboxShooter.getRawButton(5) || xboxShooter.getRawButton(7)) && xboxShooter.getRawButton(6))
        {
            if (!debounce) {
                debounce = true;
                feeder.set(Relay.Value.kReverse);
                Timer.delay(.225);
                feeder.set(Relay.Value.kOff);
            }
        }else{
            debounce = false;
            feeder.set(Relay.Value.kOff);
        }
        
        //if (!anglerSwitch.get()) {
            if(Math.abs(xboxShooter.getRawAxis(4))>.5)
            {
                if(xboxShooter.getRawAxis(4)>.0)
                {
                    anglerLeft.set(Relay.Value.kForward);
                    anglerRight.set(Relay.Value.kForward);
                }else if(xboxShooter.getRawAxis(4)<.0) {
                    anglerLeft.set(Relay.Value.kReverse);
                    anglerRight.set(Relay.Value.kReverse);
                }
            }
                else{
                    anglerLeft.set(Relay.Value.kOff);
                    anglerRight.set(Relay.Value.kOff);
                }
            //    if(xboxShooter.getY()>.5)
            //    {
            //        anglerLeft.set(Relay.Value.kForward);
            //    }
            //    if(xboxShooter.getY()<-.5)
            //    {
            //        anglerLeft.set(Relay.Value.kReverse);
            //    }
            //}else if(Math.abs(xboxShooter.getY())>.5){
            //    if(xboxShooter.getY()>.0)
            //    {
            //        anglerLeft.set(Relay.Value.kForward);
            //    }else{
            //        anglerLeft.set(Relay.Value.kReverse);
            //    }
            //}else{
            //    anglerLeft.set(Relay.Value.kOff);
            //    anglerRight.set(Relay.Value.kOff);
           // }
        //}
        
        turret.set((.5)*(xboxShooter.getZ()));
        
    }
    
    public void testPeriodic() {
    
    }
    
}
