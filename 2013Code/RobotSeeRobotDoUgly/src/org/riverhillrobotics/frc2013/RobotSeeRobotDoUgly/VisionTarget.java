/*
 * RobotSeeRobotDo - code for adjusting the turret to align with the targets
 * Copyright (C) 2013 Albert Huang
 * Based on the 2013 Vision Sample Project by FIRST, (C) 2008, FIRST BSD license
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
 * This class is designed to contain all the Vision Tracking related code,
 * therefore making the robot code much more organized, and easier to work with.
 */

package org.riverhillrobotics.frc2013.RobotSeeRobotDoUgly;

import com.sun.squawk.util.Arrays;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.LinearAverages;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.RGBImage;

/**
 * The VisionTarget class is a class designed to handle vision targeting,
 * and ONLY vision targeting!
 * @author Albert Huang (Team 4067), FIRST
 * @param camera The AxisCamera object that VisionTarget will use to fetch images from.
 */

public class VisionTarget {
    /*************************************************************************
     * Vision variables
     *************************************************************************/
    
    // Initialize the camera object
    AxisCamera camera;
    
    final int XMAXSIZE = 24;
    final int XMINSIZE = 24;
    final int YMAXSIZE = 24;
    final int YMINSIZE = 48;
    final double xMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    final double xMin[] = {.4, .6, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, .1, 0.6, 0};
    final double yMax[] = {1, 1, 1, 1, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, .5, 1, 1, 1, 1};
    final double yMin[] = {.4, .6, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
								.05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05, .05,
								.05, .05, .6, 0};
    
    final int RECTANGULARITY_LIMIT = 60;
    final int ASPECT_RATIO_LIMIT = 75;
    final int X_EDGE_LIMIT = 40;
    final int Y_EDGE_LIMIT = 60;
    
    final int X_IMAGE_RES = 320;          //X Image resolution in pixels, should be 160, 320 or 640
    final double VIEW_ANGLE = 48;       //Axis M1011 camera - this is our camera!
    
    final static int RECTANGLE_GOAL_LOW = 1;
    final static int RECTANGLE_GOAL_MIDDLE = 2;
    final static int RECTANGLE_GOAL_HIGH = 3;
    
    // Initialize the image processor - create the criteria for the particle filter
    CriteriaCollection cc = new CriteriaCollection();
    
    // Analogous to __init__ in Python - this is run to set the variables when this
    // class is initialized.
    VisionTarget(AxisCamera cam) {
        camera = cam;
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, 500, 65535, false);
    }
    
    // Scores class - sort of a C struct for storing data (in this case, Scores) in!
    public static class Scores {
        double rectangularity;
        double aspectRatioInner;
        double aspectRatioOuter;
        double xEdge;
        double yEdge;
    }
    
    // Rectangle class - struct for a Rectangle
    /**
     * Finds the rectangles in the camera image. BAD METHOD, DO NOT USE.
     * 
     * @param v_tl_x Top-left vertex of the rectangle, x coordinate
     * @param v_tl_y Top-left vertex of the rectangle, y coordinate
     * @param v_tr_x Top-right vertex of the rectangle, x coordinate
     * @param v_tr_y Top-right vertex of the rectangle, y coordinate
     * @param v_br_x Bottom-right vertex of the rectangle, x coordinate
     * @param v_br_y Bottom-right vertex of the rectangle, y coordinate
     * @param v_bl_x Bottom-left vertex of the rectangle, x coordinate
     * @param v_bl_y Bottom-left vertex of the rectangle, y coordinate
     * @param c_x Center of the rectangle, x coordinate
     * @param c_y Center of the rectangle, y coordinate
     * @param c_n_x Center of the rectangle (normalized to -1.0 < n < 1.0), x coordinate
     * @param c_n_y Center of the rectangle (normalized to -1.0 < n < 1.0), y coordinate
     * @return Rectangle object with entered data
     */
    public static class Rectangle {
        int vertex_tl_x;
        int vertex_tl_y;
        int vertex_tr_x;
        int vertex_tr_y;
        int vertex_br_x;
        int vertex_br_y;
        int vertex_bl_x;
        int vertex_bl_y;
        
        int center_x;
        int center_y;
        
        double center_norm_x;
        double center_norm_y;
        
        int goal_type;
        
        public Rectangle(int v_tl_x, int v_tl_y, int v_tr_x, int v_tr_y, int v_br_x, int v_br_y, int v_bl_x, int v_bl_y, int c_x, int c_y, double c_n_x, double c_n_y, int gt) {
            vertex_tl_x = v_tl_x;
            vertex_tl_y = v_tl_y;
            vertex_tr_x = v_tr_x;
            vertex_tr_y = v_tr_y;
            vertex_br_x = v_br_x;
            vertex_br_y = v_br_y;
            vertex_bl_x = v_bl_x;
            vertex_bl_y = v_bl_y;
            
            center_x = c_x;
            center_y = c_y;
            
            center_norm_x = c_n_x;
            center_norm_y = c_n_y;
            
            goal_type = gt;
        }
        
        // Overload so that we can allow a no-value initialization
        public Rectangle() {
            this(0, 0, 0, 100, 100, 100, 0, 100, 50, 50, 0.0, 0.0, RECTANGLE_GOAL_LOW);
        }
    }
    
    /**
     * Append a Rectangle element to a Rectangle array.
     * This method is not intended for external use, but it is made available
     * for anyone wishing to use it.
     * 
     * @param org Rectangle array
     * @param added Rectangle element to append
     * @return Rectangle array with appended element
     */
    Rectangle[] _addElement(Rectangle[] org, Rectangle added) {
        Rectangle[] result = new Rectangle[org.length + 1];
        System.arraycopy(org, 0, result, 0, org.length);
        result[org.length] = added;
        return result;
    }
    
    /**
     * Append a Rectangle element to a Rectangle array.
     * This method is not intended for external use, but it is made available
     * for anyone wishing to use it.
     * 
     * @param None
     * @return Rectangle array with detected rectangles in image
     */
    public Rectangle[] findRectangles() {
        // Initalize an empty Rectangle array
        Rectangle[] rects = new Rectangle[0];
        
        try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "testImage.jpg"
             * 
             */
            
            // Fetch an image from the camera.
            ColorImage image = camera.getImage();
            
            // Keep only red objects. This can be changed later.
            BinaryImage thresholdImage = image.thresholdHSV(60, 100, 90, 255, 20, 255);
            
            // Fill in occluded rectangles
            BinaryImage convexHullImage = thresholdImage.convexHull(false);
            
            // Filter out small particles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);

            // Go and iterate through each particle and score to see if it is a target
            Scores scores[] = new Scores[filteredImage.getNumberParticles()];
            
            for (int i = 0; i < scores.length; i++) {
                // Analyze the "particles" found via a report.
                ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                
                // Make a new Scores class to store data in.
                scores[i] = new Scores();
                
                // Score rectangularity, from 0 - 100, and store it
                scores[i].rectangularity = scoreRectangularity(report);
                
                // Score the outer aspect ratio, 0 - 100, and store it
                scores[i].aspectRatioOuter = scoreAspectRatio(filteredImage, report, i, true);
                
                // Score the inner aspect ratio, 0 - 100, and store it
                scores[i].aspectRatioInner = scoreAspectRatio(filteredImage, report, i, false);
                
                // Score the X edge, 0 - 100, and store it
                scores[i].xEdge = scoreXEdge(thresholdImage, report);
                
                // Score the X edge, 0 - 100, and store it
                scores[i].yEdge = scoreYEdge(thresholdImage, report);
                
                // Check if the rectangle target analysis meets requirements.
                // The false boolean indicates that the target is in the middle.
                    if(scoreCompare(scores[i], false))
                    {
                        // Store rectangle information for a high goal rectangle!
                        
                        // Variable naming scheme:
                        // tmp_v_pos_xy
                        // tmp - temporary variable (erased upon method completion)
                        // v - vertice
                        // pos - two letter position (top-left is tl, bottom-right is br, etc.)
                        // xy - x or y coordinate
                        int tmp_v_tl_x = report.boundingRectLeft;
                        int tmp_v_tl_y = report.boundingRectTop;
                        int tmp_v_tr_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_tr_y = report.boundingRectTop;

                        int tmp_v_bl_x = report.boundingRectLeft;
                        int tmp_v_bl_y = report.boundingRectTop + report.boundingRectHeight;
                        int tmp_v_br_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_br_y = report.boundingRectTop + report.boundingRectHeight;
                        
                        // Variable naming scheme: tmp_center_xy
                        int tmp_center_x = report.center_mass_x;
                        int tmp_center_y = report.center_mass_y;
                        
                        // Variable naming scheme: tmp_center_norm_xy
                        double tmp_center_norm_x = report.center_mass_x_normalized;
                        double tmp_center_norm_y = report.center_mass_y_normalized;

                        // Append a new Rectangle to the Rectangle array
                        rects = _addElement(rects, new Rectangle(tmp_v_tl_x, tmp_v_tl_y, tmp_v_tr_x, tmp_v_tr_y, tmp_v_bl_x, tmp_v_bl_y, tmp_v_br_x, tmp_v_br_y, tmp_center_x, tmp_center_y, tmp_center_norm_x, tmp_center_norm_y, RECTANGLE_GOAL_HIGH));

                        System.out.println("particle: " + i + "is a High Goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                        System.out.println("Distance: " + computeDistance(thresholdImage, report, i, false));
                        
                // Check if the rectangle target analysis meets requirements.
                // The true boolean indicates that the target is on the outside.
                    } else if (scoreCompare(scores[i], true)) {
                        // Store rectangle information for a middle goal rectangle!
                        
                        // Variable naming scheme:
                        // tmp_v_pos_xy
                        // tmp - temporary variable (erased upon method completion)
                        // v - vertice
                        // pos - two letter position (top-left is tl, bottom-right is br, etc.)
                        // xy - x or y coordinate
                        int tmp_v_tl_x = report.boundingRectLeft;
                        int tmp_v_tl_y = report.boundingRectTop;
                        int tmp_v_tr_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_tr_y = report.boundingRectTop;

                        int tmp_v_bl_x = report.boundingRectLeft;
                        int tmp_v_bl_y = report.boundingRectTop + report.boundingRectHeight;
                        int tmp_v_br_x = report.boundingRectLeft + report.boundingRectWidth;
                        int tmp_v_br_y = report.boundingRectTop + report.boundingRectHeight;
                        
                        // Variable naming scheme: tmp_center_xy
                        int tmp_center_x = report.center_mass_x;
                        int tmp_center_y = report.center_mass_y;
                        
                        // Variable naming scheme: tmp_center_norm_xy
                        double tmp_center_norm_x = report.center_mass_x_normalized;
                        double tmp_center_norm_y = report.center_mass_y_normalized;

                        // Append a new Rectangle to the Rectangle array
                        rects = _addElement(rects, new Rectangle(tmp_v_tl_x, tmp_v_tl_y, tmp_v_tr_x, tmp_v_tr_y, tmp_v_bl_x, tmp_v_bl_y, tmp_v_br_x, tmp_v_br_y, tmp_center_x, tmp_center_y, tmp_center_norm_x, tmp_center_norm_y, RECTANGLE_GOAL_MIDDLE));
                        
                        System.out.println("particle: " + i + "is a Middle Goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                        System.out.println("Distance: " + computeDistance(thresholdImage, report, i, true));
                    } else {
                        System.out.println("particle: " + i + "is not a goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                    }
                    System.out.println("rect: " + scores[i].rectangularity + "ARinner: " + scores[i].aspectRatioInner);
                    System.out.println("ARouter: " + scores[i].aspectRatioOuter + "xEdge: " + scores[i].xEdge + "yEdge: " + scores[i].yEdge);	
            }

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */
            filteredImage.free();
            convexHullImage.free();
            thresholdImage.free();
            image.free();

        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
        
        // Return found Rectangle array
        return rects;
    }
    
    /**
     * Finds the rectangles in the camera image. BAD METHOD, DO NOT USE.
     * 
     * @param None
     * @return None
     */
    public void findRectanglesBad() {
        try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "testImage.jpg"
             * 
             */
            ColorImage image = camera.getImage();     // comment if using stored images
            //ColorImage image;                           // next 2 lines read image from flash on cRIO
            //image = new RGBImage("/testImage.jpg");		// get the sample image from the cRIO flash
            BinaryImage thresholdImage = image.thresholdHSV(60, 100, 90, 255, 20, 255);   // keep only red objects
            //thresholdImage.write("/threshold.bmp");
            BinaryImage convexHullImage = thresholdImage.convexHull(false);          // fill in occluded rectangles
            //convexHullImage.write("/convexHull.bmp");
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // filter out small particles
            //filteredImage.write("/filteredImage.bmp");

            //iterate through each particle and score to see if it is a target
            Scores scores[] = new Scores[filteredImage.getNumberParticles()];
            for (int i = 0; i < scores.length; i++) {
                ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                scores[i] = new Scores();

                scores[i].rectangularity = scoreRectangularity(report);
                scores[i].aspectRatioOuter = scoreAspectRatio(filteredImage,report, i, true);
                scores[i].aspectRatioInner = scoreAspectRatio(filteredImage, report, i, false);
                scores[i].xEdge = scoreXEdge(thresholdImage, report);
                scores[i].yEdge = scoreYEdge(thresholdImage, report);

                if(scoreCompare(scores[i], false))
                {
                    System.out.println("particle: " + i + "is a High Goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                    System.out.println("Distance: " + computeDistance(thresholdImage, report, i, false));
                } else if (scoreCompare(scores[i], true)) {
                    System.out.println("particle: " + i + "is a Middle Goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                    System.out.println("Distance: " + computeDistance(thresholdImage, report, i, true));
                } else {
                    System.out.println("particle: " + i + "is not a goal  centerX: " + report.center_mass_x_normalized + "centerY: " + report.center_mass_y_normalized);
                }
                    System.out.println("rect: " + scores[i].rectangularity + "ARinner: " + scores[i].aspectRatioInner);
                    System.out.println("ARouter: " + scores[i].aspectRatioOuter + "xEdge: " + scores[i].xEdge + "yEdge: " + scores[i].yEdge);	
                }

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */
            filteredImage.free();
            convexHullImage.free();
            thresholdImage.free();
            image.free();

        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Computes the estimated distance to a target using the height of the particle in the image. For more information and graphics
     * showing the math behind this approach see the Vision Processing section of the ScreenStepsLive documentation.
     * 
     * @param image The image to use for measuring the particle estimated rectangle
     * @param report The Particle Analysis Report for the particle
     * @param outer True if the particle should be treated as an outer target, false to treat it as a center target
     * @return The estimated distance to the target in Inches.
     */
    public double computeDistance (BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean outer) throws NIVisionException {
            double rectShort, height;
            int targetHeight;

            rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
            //using the smaller of the estimated rectangle short side and the bounding rectangle height results in better performance
            //on skewed rectangles
            height = Math.min(report.boundingRectHeight, rectShort);
            targetHeight = outer ? 29 : 21;

            return X_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
    }
    
    /**
     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect ratio for the target. This method uses
     * the equivalent rectangle sides to determine aspect ratio as it performs better as the target gets skewed by moving
     * to the left or right. The equivalent rectangle is the rectangle with sides x and y where particle area= x*y
     * and particle perimeter= 2x+2y
     * 
     * @param image The image containing the particle to score, needed to performa additional measurements
     * @param report The Particle Analysis Report for the particle, used for the width, height, and particle number
     * @param outer	Indicates whether the particle aspect ratio should be compared to the ratio for the inner target or the outer
     * @return The aspect ratio score (0-100)
     */
    public double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean outer) throws NIVisionException
    {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        idealAspectRatio = outer ? (62/29) : (62/20);	//Dimensions of goal opening + 4 inches on all 4 sides for reflective tape
	
        //Divide width by height to measure aspect ratio
        if(report.boundingRectWidth > report.boundingRectHeight){
            //particle is wider than it is tall, divide long by short
            aspectRatio = 100*(1-Math.abs((1-((rectLong/rectShort)/idealAspectRatio))));
        } else {
            //particle is taller than it is wide, divide short by long
                aspectRatio = 100*(1-Math.abs((1-((rectShort/rectLong)/idealAspectRatio))));
        }
	return (Math.max(0, Math.min(aspectRatio, 100.0)));		//force to be in range 0-100
    }
    
    /**
     * Compares scores to defined limits and returns true if the particle appears to be a target
     * 
     * @param scores The structure containing the scores to compare
     * @param outer True if the particle should be treated as an outer target, false to treat it as a center target
     * 
     * @return True if the particle meets all limits, false otherwise
     */
    public boolean scoreCompare(Scores scores, boolean outer){
            boolean isTarget = true;

            isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT;
            if(outer){
                    isTarget &= scores.aspectRatioOuter > ASPECT_RATIO_LIMIT;
            } else {
                    isTarget &= scores.aspectRatioInner > ASPECT_RATIO_LIMIT;
            }
            isTarget &= scores.xEdge > X_EDGE_LIMIT;
            isTarget &= scores.yEdge > Y_EDGE_LIMIT;

            return isTarget;
    }
    
    /**
     * Computes a score (0-100) estimating how rectangular the particle is by comparing the area of the particle
     * to the area of the bounding box surrounding it. A perfect rectangle would cover the entire bounding box.
     * 
     * @param report The Particle Analysis Report for the particle to score
     * @return The rectangularity score (0-100)
     */
    public double scoreRectangularity(ParticleAnalysisReport report){
            if(report.boundingRectWidth*report.boundingRectHeight !=0){
                    return 100*report.particleArea/(report.boundingRectWidth*report.boundingRectHeight);
            } else {
                    return 0;
            }	
    }
    
    /**
     * Computes a score based on the match between a template profile and the particle profile in the X direction. This method uses the
     * the column averages and the profile defined at the top of the sample to look for the solid vertical edges with
     * a hollow center.
     * 
     * @param image The image to use, should be the image before the convex hull is performed
     * @param report The Particle Analysis Report for the particle
     * 
     * @return The X Edge Score (0-100)
     */
    public double scoreXEdge(BinaryImage image, ParticleAnalysisReport report) throws NIVisionException
    {
        double total = 0;
        LinearAverages averages;
        
        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(image.image, LinearAverages.LinearAveragesMode.IMAQ_COLUMN_AVERAGES, rect);
        float columnAverages[] = averages.getColumnAverages();
        for(int i=0; i < (columnAverages.length); i++){
                if(xMin[(i*(XMINSIZE-1)/columnAverages.length)] < columnAverages[i] 
                   && columnAverages[i] < xMax[i*(XMAXSIZE-1)/columnAverages.length]){
                        total++;
                }
        }
        total = 100*total/(columnAverages.length);
        return total;
    }
    
    /**
	 * Computes a score based on the match between a template profile and the particle profile in the Y direction. This method uses the
	 * the row averages and the profile defined at the top of the sample to look for the solid horizontal edges with
	 * a hollow center
	 * 
	 * @param image The image to use, should be the image before the convex hull is performed
	 * @param report The Particle Analysis Report for the particle
	 * 
	 * @return The Y Edge score (0-100)
	 *
    */
    public double scoreYEdge(BinaryImage image, ParticleAnalysisReport report) throws NIVisionException
    {
        double total = 0;
        LinearAverages averages;
        
        NIVision.Rect rect = new NIVision.Rect(report.boundingRectTop, report.boundingRectLeft, report.boundingRectHeight, report.boundingRectWidth);
        averages = NIVision.getLinearAverages(image.image, LinearAverages.LinearAveragesMode.IMAQ_ROW_AVERAGES, rect);
        float rowAverages[] = averages.getRowAverages();
        for(int i=0; i < (rowAverages.length); i++){
                if(yMin[(i*(YMINSIZE-1)/rowAverages.length)] < rowAverages[i] 
                   && rowAverages[i] < yMax[i*(YMAXSIZE-1)/rowAverages.length]){
                        total++;
                }
        }
        total = 100*total/(rowAverages.length);
        return total;
    }
}
