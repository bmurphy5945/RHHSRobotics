package org.riverhillrobotics.frc2013.BrandenDriveRobot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class DriveRobotSimple extends IterativeRobot {
    Joystick leftJoystick, rightJoystick, controller;
    RobotDrive robot;
    Jaguar launchM1, launchM2, turretRotateMotor;
    Victor climber;
    Relay turretElevatorLeft, turretElevatorRight, frisbeeLaunchMotor;
    public void robotInit() {
        //  Joysticks = contrillers
        leftJoystick = new Joystick(1); 
        rightJoystick = new Joystick(2);
        controller = new Joystick(3);
        //  Jaguars = variable speed motors
        robot = new RobotDrive(1,2,3,4);
        launchM1 = new Jaguar(5);
        launchM2 = new Jaguar(6);
        turretRotateMotor = new Jaguar(7);
        climber = new Victor(8);
        //  Relays
        turretElevatorLeft = new Relay(1);
        turretElevatorRight = new Relay(8);
        frisbeeLaunchMotor = new Relay(7);
    }
    
    public void autonomousPeriodic(){
        //vison code
    }

    public void teleopPeriodic() {
        //  Joysticks
        robot.tankDrive(leftJoystick.getY(), rightJoystick.getY());
        if (leftJoystick.getRawButton(1)){
            robot.tankDrive(-1.0, -1.0);
        }else{
           
        }
        if(rightJoystick.getRawButton(1)){
          robot.tankDrive(1.0, 1.0);  
        }else{
            
        }
        //  Controller Buttons
        /*if (controller.getRawButton(1)){    //    x button
            //reset turret
        }else{
            
        }
        if (controller.getRawButton(2)){    //  y  button
            
        }else{
            
        }
        if (controller.getRawButton(3)){    //  a  button
            //180 degree turn clockwise
        }else{
            
        }
        if (controller.getRawButton(4)){    //  b button
            //180 degree turn counter clockwise
        }else{
            
        }*/
        if (controller.getRawButton(5)){    //  left bumper
            launchM1.set(1.0);
            launchM2.set(1.0);
        }else{
            launchM1.set(0.0);
            launchM2.set(0.0);
        }
        if (controller.getRawButton(6)){    //  right bumper
            frisbeeLaunchMotor.set(Relay.Value.kForward);
        }else{
           frisbeeLaunchMotor.set(Relay.Value.kOff); 
        }    
        if (controller.getRawButton(7)){    //  left trigger
            turretElevatorLeft.set(Relay.Value.kReverse);
        }else{
           turretElevatorLeft.set(Relay.Value.kOff); 
        }
        if (controller.getRawButton(8)){    //  right trigger
            turretElevatorLeft.set(Relay.Value.kForward);
        }else{
            turretElevatorLeft.set(Relay.Value.kOff); 
        }  
        //Controller Joysticks
        if(controller.getX() > 0){  //  Left Horizontal
            climber.set(0.5);
        }else if(controller.getX() < 0){
            climber.set(-0.5);
        }else{
            climber.set(0.0);
        }
        if(controller.getY() > 0){  //  Left Vertical
            turretElevatorLeft.set(Relay.Value.kForward);
        }else if(controller.getY() < 0){
            turretElevatorLeft.set(Relay.Value.kReverse);
        }else{
            turretElevatorLeft.set(Relay.Value.kOff);
        }
        if(controller.getZ() > 0){  //  Right Horizontal
            turretRotateMotor.set(0.5);
        }else if (controller.getZ() < 0){
            turretRotateMotor.set(-0.5);
        }else{
            turretRotateMotor.set(0.0);
        }
        if (controller.getTwist() > 0){ //  Right Vertical
            turretElevatorRight.set(Relay.Value.kForward);
        }else if (controller.getTwist() < 0){
            turretElevatorRight.set(Relay.Value.kReverse);
        }else{
            turretElevatorRight.set(Relay.Value.kOff);
        }
    }
    
    public void testPeriodic() {
        
    }  
}