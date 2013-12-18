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
public class Drivetrain extends IterativeRobot {
    RobotDrive drive;
    Joystick leftStick,rightStick,ballStick;
    double leftValue,rightValue;
    //Relay belt;
    Relay sweeper, arm;
    Jaguar shooter;
    public void robotInit() {
        

        drive = new RobotDrive(1,2,3,4); //Spikes use relay (on/off motors, victor/jag is for speed control), set to DigitalModule1 and Relay1
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        ballStick = new Joystick(3);
        sweeper = new Relay(2);
        arm = new Relay(1);
        shooter = new Jaguar(5);
       
        //belt = new Relay(1,1,Direction.kBoth);
    }


    public void autonomousPeriodic() {
       final double driveTime;
       final double driveSpeed;
       final double launchSpeed;
       Timer timer = new Timer();
       timer.start();
       //IF THIS HAS NOT BE DELETED YOU NEED TO COMPLIE THIS
       shooter.set(-.5);
       timer.delay(15);
       //drive.drive(.5, 0);
       //timer.delay(8);
       
       
      
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
           
            if(leftValue>0&&rightValue<0)
                drive.tankDrive(limit(leftValue), limit(rightValue));
            else if(leftValue<0&&rightValue>0)
                drive.tankDrive(limit(leftValue), limit(rightValue)); 
            else 
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
        
        while(ballStick.getRawButton(2)){
            arm.set(Relay.Value.kForward);
            
        } 
        arm.set(Relay.Value.kOff);
        
        while(ballStick.getRawButton(3)){
            arm.set(Relay.Value.kReverse);            
        }
        arm.set(Relay.Value.kOff);
       
        
        
        shooter.set(-limit(ballStick.getY()));
            
        
        
        
           
        
       // if(rightStick.getTrigger())
       // {
       //     System.out.println("Belt running!");
       //     belt.set(Relay.Value.kForward);
       // } else {
       //     System.out.println("Belt disabled!");
       //     belt.set(Relay.Value.kOff);
       // }
        
  
        
   

        
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
    
    
    // MAKE THE BELT ABLE TO GO BACKWARDS 

}
