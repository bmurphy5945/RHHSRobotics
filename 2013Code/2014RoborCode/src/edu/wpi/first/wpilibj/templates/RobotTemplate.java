package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.*;

public class RobotTemplate extends IterativeRobot 
{
    Joystick dave;
    RobotDrive claire;
    
    public void robotInit() 
    {
        dave = new Joystick(2);
        claire = new RobotDrive(1,2,3,4);
    }

    public void autonomousPeriodic() 
    {  
    }

    public void teleopPeriodic() 
    {
        claire.tankDrive(dave.getY(), dave.getTwist());
    }
    
    public void testPeriodic() 
    {    
    }
}