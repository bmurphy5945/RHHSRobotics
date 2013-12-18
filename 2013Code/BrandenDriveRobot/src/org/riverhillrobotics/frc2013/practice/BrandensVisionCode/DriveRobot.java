package org.riverhillrobotics.frc2013.practice.BrandensVisionCode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class DriveRobot extends IterativeRobot {
    double x, y, z, twist;
    Joystick leftJoystick, rightJoystick, controller;
    RobotDrive robot;
    Jaguar launchM1, launchM2;
    Victor frisbeeLaunchMotor;
    Relay turretElevatorLeft, turretElevatorRight, turretRotateMotor;
    long startTime, endTime, leftElevatorTime = 0, rightElevatorTime = 0, turretRotateTime = 0;
    long rotateReset, leftReset, rightReset;
    Reset reset = new Reset();
    public void robotInit() {
        //Joysticks = contrillers
        leftJoystick = new Joystick(1); 
        rightJoystick = new Joystick(2);
        controller = new Joystick(3);
        //Jaguars = variable speed motors
        robot = new RobotDrive(1,2,3,4);
        launchM1 = new Jaguar(5);
        launchM2 = new Jaguar(6);
        turretRotateMotor = new Relay(1);
        turretElevatorLeft = new Relay(2);
        turretElevatorRight = new Relay(3);
        frisbeeLaunchMotor = new Victor(7);
    }

    public void teleopPeriodic() {
        //driving
        robot.tankDrive(leftJoystick.getY()*.7, rightJoystick.getY()*.7); // Joysticks
        if(rightJoystick.getRawButton(1)){ // Left Joystick Trigger
            robot.tankDrive(1.0, 1.0);
        }else{
            robot.tankDrive(0, 0);
        }
        if(leftJoystick.getRawButton(1)){ // Right Joystick Trigger
            robot.tankDrive(-1.0, -1.0);
        } else {
            robot.tankDrive(0, 0);
        }
        
        //controller
        if(controller.getZ() > 0){
            
        }else{
            
        }
        if(controller.getZ() < 0){
            
        }else{
            
        }
        if(controller.getZ() > 0){
            startTime = reset.startTimer();
        }else{
            endTime = reset.endTimer(startTime);
            turretRotateTime = turretRotateTime + endTime;
        }
        if(controller.getZ() < 0){
            startTime = reset.startTimer();            
        }else{
            endTime = reset.endTimer(startTime);
            turretRotateTime = turretRotateTime - endTime;            
        }
        if(controller.getY() > 0){
            turretElevatorLeft.set(Relay.Value.kForward);
        }else if(controller.getY() < 0){
            turretElevatorLeft.set(Relay.Value.kReverse);
        }else{
            turretElevatorLeft.set(Relay.Value.kOff);
        }
        if(controller.getTwist() > 0){
            turretElevatorRight.set(Relay.Value.kForward);
        }else if(controller.getTwist() < 0){
           turretElevatorRight.set(Relay.Value.kReverse); 
        }else{
           turretElevatorRight.set(Relay.Value.kOff); 
        }
        if(controller.getY() > 0){
            startTime = reset.startTimer();   
        }else{
            endTime = reset.endTimer(startTime);
            leftElevatorTime = leftElevatorTime + endTime;
        }
        if(controller.getY() < 0){
            startTime = reset.startTimer();            
        }else{
            endTime = reset.endTimer(startTime);
            leftElevatorTime = leftElevatorTime - endTime;
        }
        if(controller.getTwist() > 0){
            startTime = reset.startTimer();            
        }else{
            endTime = reset.endTimer(startTime);
            leftElevatorTime = leftElevatorTime + endTime;            
        }
        if(controller.getTwist() < 0){
            startTime = reset.startTimer();            
        }else{
            endTime = reset.endTimer(startTime);
            leftElevatorTime = leftElevatorTime - endTime;            
        }
        
        
        
        if(controller.getRawButton(1)){ //x button reset turret
            if (rotateReset <= turretRotateTime) {
                turretRotateMotor.set(Relay.Value.kReverse);
                Timer.delay(0.1);
                rotateReset += 0.1;
            }else if (rotateReset >= turretRotateTime){
                turretRotateMotor.set(Relay.Value.kForward);
                Timer.delay(0.1);
                rotateReset += 0.1;
            }else if (rotateReset == turretRotateTime){
            rotateReset = 0;
            turretRotateTime = 0;
            }
            
            
            
            
            
            if (leftReset <= leftElevatorTime) {
                turretElevatorLeft.set(Relay.Value.kReverse);
                Timer.delay(0.1);
                leftReset += 0.1;
            }else if (leftReset == leftElevatorTime){
                leftReset = 0;
                leftElevatorTime = 0;
            }
            if (rightReset <= rightElevatorTime) {
                turretElevatorRight.set(Relay.Value.kReverse);
                Timer.delay(0.1);
                rightReset += 0.1;
            } else if (rightReset <= rightElevatorTime){
                rightReset = 0;
                rightElevatorTime = 0;
            }         
        }else{
            
        }
        /*if(controller.getRawButton(2)){ // a button
            
        }else{
            
        }
        if(controller.getRawButton(3)){ // b button  
            
        }else{
            
        }
        if(controller.getRawButton(4)){ // y button run vision
            
        }else{
            
        }*/
        if(controller.getRawButton(5)){ // left bumper
            launchM1.set(1.0);
            launchM2.set(1.0);
        }else{
            launchM1.set(0);
            launchM2.set(0);
        }
        if(controller.getRawButton(6)){ // right bumper
            frisbeeLaunchMotor.set(1);
        }else{
           frisbeeLaunchMotor.set(0); 
        }
        if(controller.getRawButton(7)){ // left trigger
            startTime = reset.startTime;
            turretElevatorLeft.set(Relay.Value.kReverse);
            turretElevatorRight.set(Relay.Value.kReverse);
        }else{     
           endTime = reset.endTimer(startTime);
           leftElevatorTime = leftElevatorTime-endTime;
           rightElevatorTime = rightElevatorTime + endTime;
           if(leftElevatorTime < 0){
               leftElevatorTime = 0;
           }
           if(rightElevatorTime < 0){
               rightElevatorTime = 0;
           }
           turretElevatorLeft.set(Relay.Value.kOff);
           turretElevatorRight.set(Relay.Value.kOff);
        }
        if(controller.getRawButton(8)){ // right trigger
            startTime = reset.startTime;            
            turretElevatorLeft.set(Relay.Value.kForward);
            turretElevatorRight.set(Relay.Value.kForward);
        }else{
           endTime = reset.endTimer(startTime);
           rightElevatorTime = rightElevatorTime+endTime;
           leftElevatorTime = leftElevatorTime + endTime;
           turretElevatorLeft.set(Relay.Value.kOff); 
           turretElevatorRight.set(Relay.Value.kOff);
        }
    }
    
    public void testPeriodic() {
        
    }  
}