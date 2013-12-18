/* FRC Robot Code (Team #4067)
 * Copyright (C) 2012 River Hill HS Robotics Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Portions copyright (c) FIRST 2008. All Rights Reserved.
 * Open Source Software - may be modified and shared by FRC teams. The code 
 * must be accompanied by the FIRST BSD license file in the root directory of
 * the project.
 * 
 * Reading this code assumes general knowledge of robot build, motor controllers,
 * and Java.
 */

/* This is the package statement - we want to name our package!
 * Name it something like edu.[Abbreviate school, 3 letters].[Fullname].[function OR classname if only one]
 * Only the last part of the package name may contain caps.
 * (Not sure if this is actually a requirement, but it IS a stylistic guideline that
 * you should follow.)
 * For example, we would name it:
 *   edu.rhh.incrediblehawk.RookieBot
 * This also depends on the directory structure - this file would be in
 * RookieBot\src\edu\rhh\incrediblehawk\ if it was named as such.
 * Just for sillyness, we've left it as the default package.
 */
package edu.wpi.first.wpilibj.templates;

/* We need to import the robotics code so we can talk to the robot! */
import edu.wpi.first.wpilibj.*;
/* We need to import the Java math class. You'll see later why. */
import java.lang.Math;

/* And now, a cryptic message from FIRST... */
/* ============================================================================== */
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
/* ============================================================================== */
/* I assume that they are referring to PROJECT_DIRECTORY/resources/MANIFEST.MF,
 * where you must change the value of MIDlet-Name to your new project name, and
 * the first argument of MIDlet-1 to the new class name.
 */
 
/* Let's define the RookieBot class! It inherits the IterativeRobot class,
 * which allows us to define what code runs at what time.
 * (See API docs: http://www.wbrobotics.com/javadoc/edu/wpi/first/wpilibj/IterativeRobot.html)
 */
public class RookieBot extends IterativeRobot {
    /* Now initialize the variables! */
    
    /* The RobotDrive type allows us to use easy, simply method calls to
     * control multiple motors at the same time - i.e. driving the bot.
     */
    RobotDrive drive;
    
    /* The Joystick type allows us to get the values from the joystick.
     * We'll assign the variables to the actual joysticks later on.
     * leftStick is our left joystick, rightStick is our right joystick,
     * and ballStick is our third, furthest to the right joystick,
     * used for other functions besides driving. More info about the
     * joystick behavior later!
     */
    Joystick leftStick,rightStick,ballStick;
    
    /* We want to store the values of the joysticks and process them.
     * leftValue will contain the leftStick's joystick value, and
     * rightValue will contain the rightStick's joystick value.
     */
    double leftValue, rightValue;
    
    /* We use a Relay here since our robot uses a Relay motor controller
     * for our sweeper (or ball collector). You assign variables for
     * those controllers to their respective controller types so that
     * you can control it with the methods.
     */
    Relay sweeper;
    
    /* We use a Jaguar here since our robot uses two Jaguar motor
     * controllers for the ball shooter and arm.
     */
    Jaguar shooter,arm;
    
    /* That's it! Let's define some methods! */
    
    /* This is the robotInit() method. We define this so that the robot's
     * cRIO, when it starts up (when you hit Enable on the FRC Driver Station),
     * will run the necessary code to fully initialize the variables for
     * controlling the robot. Note that this MUST be named robotInit().
     * The cRIO only looks for and runs this method.
     * This rule applies for any other methods that are described as
     * methods that the cRIO uses - they can NOT be named differently!
     */
    public void robotInit() {
        /* Now things start coming together! */
        
        /* We initialize the RobotDrive class and assign the object to the
         * RobotDrive typed variable, drive.
         * The arguments specify the digital PWM pins that they are connected to for
         * motors frontLeftMotor, rearLeftMotor, frontRightMotor, and rearRightMotor
         * on the digital sidecar.
         */
        drive = new RobotDrive(1,2,3,4);
        
        /* Now we initialize the joysticks!
         * The argument specifies the joystick number as assigned in the FRC Driver Station.
         */
        leftStick = new Joystick(1);
        rightStick = new Joystick(2);
        ballStick = new Joystick(3);
        
        /* Now we initialize the Relay class for the sweeper variable!
         * The argument specifies the Relay channel that the Relay motor
         * controller is connected to on the digital sidecar.
         */
        sweeper = new Relay(2);
        
        /* Now we initialize the Jaguar class for the shooter and arm
         * variables! In this case, the shooter is on digital PWM channel
         * 5, and the arm is on digital PWM channel 6.
         * 
         * Notice how RobotDrive also used a similar argument - 
         * the Jaguar uses the digital PWM channels as well.
         * The RobotDrive class should automatically detect the type of
         * controller you have. For individual controller control,
         * you need to know what controller you have.
         */
        shooter = new Jaguar(5);
        arm = new Jaguar(6);
        
        /* We're done! */
    }
    
    /* This is the autonomousPeriodic() method... for autonomous mode.
     * It's not very interesting, but we'll look at it anyway.
     */
    public void autonomousPeriodic() {
        /* This is a constant to quickly change the shooter's speed. 
         * The maximum speed is 1.0.
         */
        final double launchSpeed=.8;
        
        /* Let's move some motors! */
        
        /* We call the method set to the Relay class, and pass a special
         * constant that tells the Relay class to move its assigned Relay
         * controller to tell the motor to move a certain direction.
         * 
         * The Relay motor, as you may have guessed, can only be commanded
         * to move forward and backward. There's no speed setting here.
         * Here, we set it to Relay.Value.kForward, the special constant for
         * going "forward", so that the balls in our robot can be moved
         * towards the shooter.
         */
        sweeper.set(Relay.Value.kForward);
        
        /* Now we call the method set to the Jaguar class, and simply pass
         * that constant to it. This will turn on the shooter and shoot the
         * ball when the ball gets there.
         */
        shooter.set(launchSpeed);
        
        /* This method, as well as the other teleopPeriodic method, is executed
         * repeatedly while it's in that mode. Yup - that code above will be repeated
         * for 15 seconds. Once it's over, the internal code - the code that powers all
         * of this, NOT our code - will reset the motor controllers and either
         * stop the execution or move on to the other mode, teleop.
         */
    }
    
    /* This is the teleopPeriodic method - things do get interesting here! */
    public void teleopPeriodic() {
        /* We move the left and right joysticks back and forth to control
         * the robot's movement. On the stick classes, we call the method getY()
         * to fetch the position of the joystick in the Y direction - how much
         * forward or backward it is. It returns a double value, which we assigned
         * to those double variables we've created earlier.
         */
        leftValue = leftStick.getY();
        rightValue = rightStick.getY();
        
        /* Now we call a method - getTrigger() - from the Joystick class.
         * This basically checks if a person is holding the trigger on a
         * specified joystick or not. In this case, we're checking if the
         * right joystick's trigger is being pressed or not. It returns a
         * boolean, so the if conditional's code will execute if the
         * joystick's trigger is pressed.
         */
        if(rightStick.getTrigger()) {
            /* Why do a check? We designed it so that when you pressed the trigger,
             * the values would flip, and you would be able to drive it backwards.
             * This is useful if the robot has turned backward, and the driver
             * doesn't want to drive backward on the field in the flipped robot
             * orientation.
             */
            leftValue = -leftValue;
            rightValue = -rightValue;
        }
        
        /* Again, we use the lovely getTrigger(), but on the left joystick this time.
         * Note that we don't use brackets here, since we're only executing one statement.
         */
        if(leftStick.getTrigger())
            /* We call tankDrive() and pass the same values for left and right, which
             * lets the driver drive with one (left) joystick, with that same joystick's
             * trigger held down.
             * We use the modified leftValue here to allow backwards synchronized driving.
             */
            drive.tankDrive(leftValue, leftValue);
        else
            /* Otherwise, just drive regularly!
             * If you haven't figured it out yet, tankDrive() takes two arguments - 
             * one value to drive the left side motors of the robot, and another
             * for the right.
             */
            drive.tankDrive(leftValue,rightValue);
        
        /* Now let's play with the ballStick!
         * We're using the ballStick to handle all of the shooter and sweeper
         * movement.
         * Again, we use the lovely getTrigger(), but on the last joystick this time.
         */
        if(ballStick.getTrigger()){
            /* When the trigger is pressed, we want the sweeper to turn on.
             * Since this is the Relay controller, we use the special constant
             * to tell the direction we want it to spin.
             */
            sweeper.set(Relay.Value.kForward);
        } else {
            /* When the trigger is let go, we want the sweeper shut off.
             * Again, there's a special constant, but this time to turn it off.
             */
            sweeper.set(Relay.Value.kOff);
        }
        
        /* Now we use the getRawButton() method. This time, we're looking for a
         * joystick's button press - aside from the regular joystick movements,
         * you can also detect button presses based on the labeled button
         * numbers. On our joystick, this particular button was labeled 5, so we
         * used that. This method returns a boolean as well to check if it's
         * pressed or not.
         */
        if(ballStick.getRawButton(5)) {
            /* When this button is pressed, we want to make the sweeper go in reverse.
             * This is for emergency purposes - that is, if the ball gets stuck
             * on its way to the shooter, and we want to clear the jam.
             */
            sweeper.set(Relay.Value.kReverse);
        }
        
        /* We use the getRawButton() method again here, but this time to control
         * the robot arm. Since this uses a Jaguar (for more control and power),
         * we just need to pass numerical values to it.
         * A positive value is going forward, and a negative value is going
         * backward, with the maximum at 1 and -1, respectively. We used maximum power
         * here to exert as much force as possible on the balance boards!
         */
        if(ballStick.getRawButton(2))
            /* When this button is pressed, move the robot arm down! */
            arm.set(1);
        else if(ballStick.getRawButton(3))
            /* When this button is pressed, move the robot arm back up! */
            arm.set(-1);            
        else
            /* If there's no button pressed, tell it to stop moving!
             * It's important to remember that the values that you set are NOT
             * forgotten by the motor controller - it will still apply until you
             * change the value.
             */
            arm.set(0);
        
        /* Now we want to move the shooter's wheels!
         * This is also a Jaguar, so we pass a value.
         * However, due to the design of our bot, we NEVER want the shooter to
         * spin backwards. This will jam up the ball into a rather bad area of
         * our bot, which means no more shooting...
         * 
         * To enforce that (positive values go forward), we just use Math.abs()
         * to make any value detected positive.
         */
        shooter.set(Math.abs(ballStick.getY()));
        
        /* And just like that, we're done! */
    }
}
