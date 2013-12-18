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

public class GrahamDrivetrain extends IterativeRobot {
    RobotDrive drive;
    Jaguar sweeper, shooter;
    Joystick leftStick, rightStick, ballStick;
    double leftValue, rightValue;
    
    //IWANNADRIVE has been disabled in this version!
    //boolean IWANNADRIVE = true;// IF YOU WANNA DRIVE, set this to true, if you want to play with the sweeper set this to false
    
    public void robotInit() {
        drive = new RobotDrive(1,2);
        shooter = new Jaguar(3);
        sweeper = new Jaguar(4);
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        ballStick = new Joystick(3);//Don't actually use this right now because I don't know what the button numbers are!
    }
    
    public void autonomousPeriodic() {

    }

    public void teleopPeriodic() {
        leftValue = leftStick.getY();
        rightValue = rightStick.getY();
        
        //Drive code
        if(leftStick.getTrigger()||rightStick.getTrigger()) {
           symetricalDrive(leftStick);
        } else {
            if(leftValue>0&&rightValue<0)
                drive.tankDrive(limit(leftValue), limit(rightValue));
            else if(leftValue<0&&rightValue>0)
                drive.tankDrive(limit(leftValue), limit(rightValue)); 
            else 
                drive.tankDrive(leftValue,rightValue);
        }
        
        //Sweeper code
        if(rightStick.getTrigger()){
            sweeper.set(-1.0);
        } else {
            sweeper.set(0.0);
        }
        
        //Shooter code
        if(rightStick.getRawButton(3)){
            shooter.set(1.0);//Not sure if this is the right direction!
        } else {
            shooter.set(0.0);
        }
    }
   
    public static double limit(double num) {
        final double limit = .5;//THRESHOLD HIGER 2/12
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
        while (leftStick.getTrigger()){
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
    
    /*
    public void AJsDrive(Joystick stick)
    {
        final double LIMIT = .5;
        double y = LIMIT*stick.getY();
        double x = stick.getX();
        double leftDrive;
        double rightDrive;
        
        if (x>=0)
        {
            rightDrive = (1-Math.abs(x))*y;
            leftDrive = y;
        }else{
            leftDrive = y;
            rightDrive = (1-Math.abs(x))*y;
        }
        drive.tankDrive(leftDrive,rightDrive);
    }
    */
}
