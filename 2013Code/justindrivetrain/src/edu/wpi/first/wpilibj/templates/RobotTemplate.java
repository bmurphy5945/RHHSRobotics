/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.Relay.Direction;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    RobotDrive drive;
    Joystick leftStick,rightStick;
    double leftValue,rightValue;
    Relay belt;
    //Relay spike;
    public void robotInit() {
        drive = new RobotDrive(1,2,3,4); //Spikes use relay (on/off motors, victor/jag is for speed control), set to DigitalModule1 and Relay1
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        belt = new Relay(1);//Relay(1,1,Direction.kBoth);
        //spike = new Relay(1);
    }
    public void disabledInit() {
        System.out.println("[disabledInit] ROBOT IS OFFFFFFFF");
    }
    public void disabledPeriodic() {
        //System.out.println("[disabledPeriodic] ROBOT IS OFFFFFFFF");
    }
    public void disabledContinuous() {
        //System.out.println("[disabledContinuous] ROBOT IS OFFFFFFFF");
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        //spike.set(Relay.Value.kForward);
        leftValue = leftStick.getY();
        rightValue = rightStick.getY();
        
        leftValue = limit(leftValue);
        rightValue = limit(rightValue);
        
        if(leftStick.getTrigger()) {
           System.out.println("Symetrical driving: BEFORE CALL!! leftValue is "+leftValue+", rightValue is "+rightValue); 
           symetricalDrive(leftStick);
        } else {
            System.out.println("Driving: leftValue is "+leftValue+", rightValue is "+rightValue);
            drive.tankDrive(leftValue,rightValue);
            /*if(leftValue>0&&rightValue<0)
                drive.tankDrive(limit(leftValue), limit(rightValue));
            else if(leftValue<0&&rightValue>0)
                drive.tankDrive(limit(leftValue), limit(rightValue)); 
            else 
                drive.tankDrive(leftValue,rightValue);*/
        }
        
        if(rightStick.getTrigger())
        {
            System.out.println("Belt running!");
            belt.set(Relay.Value.kForward);
        } else {
            System.out.println("Belt disabled!");
            belt.set(Relay.Value.kOff);
        }
        
   

        
    }
   
    public static double limit(double num) {
        final double limit = .6;
        if (num > limit) {
            return limit;
        }
        if (num < -limit) {
            return -limit;
        }  
        return num;
    }
    
    public void symetricalDrive(Joystick stick){
        System.out.println("symetricalDrive triggered!");
        double offset=0;
        while (leftStick.getTrigger()){
        leftValue = stick.getY()-offset;
        rightValue = stick.getY()+offset;
        
        if(stick.getRawButton(4))
            offset -=.02;
        if(stick.getRawButton(5))
            offset +=.02;
        if(stick.getRawButton(3))
            offset=0;
        System.out.println("symetricalDrive: leftValue is "+leftValue+", rightValue is "+rightValue);
        drive.tankDrive(leftValue,rightValue); //Relay set to forward to move it forward
        }
    }
   
}
