package org.riverhillrobotics.frc2013.practice.BrandensVisionCode;

public class Reset {
    long startTime, finalTime, newTime;
    public long startTimer(){
        startTime = System.currentTimeMillis();
    return startTime;
    }
    public long endTimer(long startTime){
        finalTime = System.currentTimeMillis();
        newTime = finalTime - startTime;
    return newTime;
    }
}