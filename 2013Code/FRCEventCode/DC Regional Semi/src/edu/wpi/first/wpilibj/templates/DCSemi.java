/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import java.lang.Math;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class DCSemi extends IterativeRobot {
    RobotDrive drive;
    Joystick leftStick,rightStick,ballStick;
    double leftValue,rightValue;
    //Relay belt;
    Relay sweeper;
    Jaguar shooter,arm;
    public void robotInit() {
        
        drive = new RobotDrive(1,2,3,4); //Spikes use relay (on/off motors, victor/jag is for speed control), set to DigitalModule1 and Relay1
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        ballStick = new Joystick(3);
        sweeper = new Relay(2);
        
        
        shooter = new Jaguar(5);
        arm = new Jaguar(6);
        
    } 
    public void autonomousPeriodic() {
        /* CODE FOR SHARING BALLS */
        sweeper.set(Relay.Value.kOff);
        final double delay1 = 6;
        final double spinTime1 = 10;
        final double delay2 = 1.5;
        final double spinTime2 = 5;
        
        Timer.delay(delay1);
        sweeper.set(Relay.Value.kReverse);
        
        Timer.delay(spinTime1);
        sweeper.set(Relay.Value.kOff);
        
        //Timer.delay(delay2);
        //sweeper.set(Relay.Value.kReverse);        //Timer.delay(spinTime2);
        //sweeper.set(Relay.Value.kOff);
        
        // 180 degree turn code
        /*drive.drive(1.0, 1.0);
        Timer.delay(delay2);
        drive.drive(-0.85, 0.85);
        Timer.delay(6);
        drive.drive(0, 0);
        int count = 0;*/
        //while (count <= 1000) {
        //    drive.setLeftRightMotorOutputs(-1, 1);
        //    count++;
        //}
        //drive.setLeftRightMotorOutputs(0, 0);
        // SWEEPER AUTO CODE
        //sweeper.set(Relay.Value.kForward);
        //shooter.set(1);
        

    }


    public void teleopPeriodic() {
        leftValue = leftStick.getY();
        rightValue = rightStick.getY();
        
        if(rightStick.getTrigger()) {
            leftValue = -leftValue;
            rightValue = -rightValue;
        }
        
        
        if(leftStick.getTrigger()) {
           symetricalDrive(leftStick);
        } 
        else if (rightStick.getTrigger())
           drive.arcadeDrive(rightStick);
        
        else {
           
            //if(leftValue>0&&rightValue<0)
            //    drive.tankDrive(limit(leftValue), limit(rightValue));
            //else if(leftValue<0&&rightValue>0)
            //    drive.tankDrive(limit(leftValue), limit(rightValue)); 
            //else 
                drive.tankDrive(leftValue,rightValue);
        }
        
        
        if(ballStick.getTrigger()){
            sweeper.set(Relay.Value.kForward);
        } else {
            sweeper.set(Relay.Value.kOff);
        }
        
        if(ballStick.getRawButton(5)) {
            sweeper.set(Relay.Value.kReverse);
        }
        
        
        
        
        //arm.set(Relay.Value.kForward);
        //Timer.delay(1);
        //arm.set(Relay.Value.kReverse);
        //Timer.delay(2);
        //arm.set(Relay.Value.kOff);
        
        
        
        if(ballStick.getRawButton(2))
            arm.set(1);
        
        
        else if(ballStick.getRawButton(3))
            arm.set(-1);            
        
        else
            arm.set(0);
       
        
        
        //shooter.set(-limit(ballStick.getY
        // CHANGED: 11/17/2012 @ 10:07 AM
        // By: Albert Huang
        // Prevent accidental trigger of spinner.
        /*double ballStickY = 0.0;
        ballStickY = ballStick.getY();
        if (ballStickY >= 0.05)
            shooter.set(Math.abs(ballStickY));
        */
        shooter.set(Math.abs(ballStick.getY()));
    }
   
    public static double limit(double num) {
        final double limit = 1.0;//THRESHOLD HIGER on 2/12/12    //CHANGED FROM .5 TO .75 ON 2/20/12
        if (num > limit) {
            return limit;
        }
        if (num < -limit) {
            return -limit;
        }  
        return num;
    }
    
    public void symetricalDrive(Joystick stick){
        double offset=0;
        if (leftStick.getTrigger()){
        leftValue = stick.getY()-offset;
        rightValue = stick.getY()+offset;
        
        if(stick.getRawButton(4))
            offset -=.02;
        if(stick.getRawButton(5))
            offset +=.02;
        if(stick.getRawButton(3))
            offset=0;
        
            drive.tankDrive(leftValue,rightValue); //Relay set to forward to move it forward
        }
    }
    
    
    // MAKE THE BELT ABLE TO GO BACKWARDS 

}
