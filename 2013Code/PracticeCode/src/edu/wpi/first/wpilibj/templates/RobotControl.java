package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Relay;

public class RobotControl extends IterativeRobot {
    RobotDrive robot;
    Joystick leftStick, rightStick, controller;
    Jaguar launchM1, launchM2;
    Victor trigger;
    Relay turretElevatorLeft, turretElevatorRight, turretRotate;
    
    public void robotInit() {
        //controllers
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        controller = new Joystick(3);
        //Motor Controllers
        robot = new RobotDrive(1,2,3,4);
        launchM1 = new Jaguar(5);
        launchM2 = new Jaguar(6);
        trigger = new Victor(7);
        //Relays
        turretElevatorLeft = new Relay(1);
        turretElevatorRight = new Relay(2);
        turretRotate = new Relay(3);
    }

    public void autonomousPeriodic() {
        
    }

    public void teleopPeriodic() {
        //Drive
        robot.tankDrive(leftStick.getY(), rightStick.getY());
        if(rightStick.getRawButton(1)){
            robot.tankDrive(rightStick.getY(), rightStick.getY());
        }else{
            robot.tankDrive(0.0, 0.0);
        }
        
        //Turret
        if(controller.getY() > 0){  //Left Elevator
            turretElevatorRight.set(Relay.Value.kForward);
        }else if(controller.getY() < 0){
            turretElevatorRight.set(Relay.Value.kReverse);
        }else{
            turretElevatorRight.set(Relay.Value.kOff);
        }
        if(controller.getTwist() > 0){  //Right Elevator
            turretElevatorLeft.set(Relay.Value.kForward);
        }else if(controller.getTwist() < 0){
            turretElevatorRight.set(Relay.Value.kReverse);
        }else{
            turretElevatorRight.set(Relay.Value.kOff);
        }
        
        if(controller.getX() > 0){
            turretRotate.set(Relay.Value.kForward);
        }else if(controller.getX() < 0){
            turretRotate.set(Relay.Value.kReverse);
        }else{
            turretRotate.set(Relay.Value.kOff);
        }
    }
    
    public void testPeriodic() {
        
    }
    
}
