
package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Jaguar;

public class Test extends IterativeRobot {
    Joystick leftStick;
    Joystick rightStick;
    RobotDrive robot; 
    Relay spike1;
    Relay spike2;
    Relay spike3;
    Relay spike4;
    Relay spike5;
    Relay spike6;
    Relay spike7;
    Relay spike8;
    //double leftValue, rightValue;
    //Jaguar frontLeft, rearLeft, frontRight, rearRight;
    
    public void robotInit() {
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        robot = new RobotDrive(1,2,3,4);
        //robot= new RobotDrive(frontLeft,rearLeft,frontRight,rearRight);
        spike1 = new Relay(1);
        spike2 = new Relay(2);
        spike3 = new Relay(3);
        spike4 = new Relay(4);
        spike5 = new Relay(5);
        spike6 = new Relay(6);
        spike7 = new Relay(7);
        spike8 = new Relay(8);
       
        spike1.set(Relay.Value.kOn);
        spike2.set(Relay.Value.kOn);
        spike3.set(Relay.Value.kOn);
        spike4.set(Relay.Value.kOn);
        spike5.set(Relay.Value.kOn);
        spike6.set(Relay.Value.kOn);
        spike7.set(Relay.Value.kOn);
        spike8.set(Relay.Value.kOn);
    }

    public void disabledPeriodic(){
       
    }
    public void autonomousPeriodic() {
      
    }


    public void teleopPeriodic() {
        //robot.setSafetyEnabled(false);
        
        System.out.println("Left stick @ "+leftStick.getY()+", Right stick @ "+rightStick.getY());
        robot.tankDrive(leftStick.getY(), rightStick.getY());
                
        if(leftStick.getRawButton(1)){
            System.out.println("RELAY ACTIVATED");
            spike1.set(Relay.Value.kForward);
            spike2.set(Relay.Value.kForward);
            spike3.set(Relay.Value.kForward);
            spike4.set(Relay.Value.kForward);
            spike5.set(Relay.Value.kForward);
            spike6.set(Relay.Value.kForward);
            spike7.set(Relay.Value.kForward);
            spike8.set(Relay.Value.kForward);
        } else {
            System.out.println("RELAY OFF");
            spike1.set(Relay.Value.kOff);
            spike2.set(Relay.Value.kOff);
            spike3.set(Relay.Value.kOff);
            spike4.set(Relay.Value.kOff);
            spike5.set(Relay.Value.kOff);
            spike6.set(Relay.Value.kOff);
            spike7.set(Relay.Value.kOff);
            spike8.set(Relay.Value.kOff);
        }
        
    }
    
    public void teleopContinuous(){
        //robot.tankDrive(leftStick.getY(), rightStick.getY());
    }
    
}