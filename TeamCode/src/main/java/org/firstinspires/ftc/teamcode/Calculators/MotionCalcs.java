package org.firstinspires.ftc.teamcode.Calculators;

import org.firstinspires.ftc.teamcode.Utilities.*;

import java.util.ArrayList;

public class MotionCalcs { //This will always output a power on the x axis of the robot and a power on the y axis


    /**
     * This is a less common way to drive it implies that
     * when the joystick is pushed away the robot will go away
     * disregarding orientation
     * @return A vector on the joysticks coordinates rotated by the heading of the robot
     */
    public static Interfaces.MotionCalc ObjectCentricJoystick() {
        return new Interfaces.MotionCalc() {
            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                Vector2D power = (d.driver.ls());
                return power.getRotatedBy(Math.toRadians(-d.heading));
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };
    }


    /**
     * This is the most common way to drive a robot when the
     * joystick is pushed forward the robot goes forward
     * @return A vector on the joysticks coordinates
     */
    public static Interfaces.MotionCalc FieldCentricJoystick(final double driverOffsetDeg){
        return new Interfaces.MotionCalc() {
            double myProgress = 0.0;
            @Override
            public double myProgress(Interfaces.MoveData d) {

                return myProgress;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {

                return d.driver.ls().getRotatedBy(Math.toRadians(driverOffsetDeg)).getNormalizedSquare();

            }
        };
    }


    /**
     * All motion is described by curves
     * @param curveLength The length of the curve maybe in cm
     * @param curveFromHeading Using compass heading (clock wise from the top 0, 90, 180, 270)
     *                     This is the heading the robot should travel at the beginning of its travel
     * @param curveToHeading Using compass heading (clock wise from the top 0, 90, 180, 270)
     *                     This is the heading the robot should travel at the end of its travel
     * @return returns the progress of the Curve
     * @return returns a unit vector of the direction that is desired
     */
    public static Interfaces.MotionCalc CurveMotion(final double curveLength, final double curveFromHeading, final double curveToHeading){

        return new Interfaces.MotionCalc() {

            private double myProgress = 0;
            private double worldDist = 0;

            private Vector2D curve = new Vector2D(Vector2D.toCartesian(1, Math.toRadians(curveFromHeading)));

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                worldDist += d.wPos.distance(d.preWPos);
                myProgress = worldDist / curveLength;
                double targetHeading = ((curveToHeading-curveFromHeading)*myProgress);
//                d.debugData1 = targetHeading;
                return curve.getRotatedBy((Math.PI/2)-Math.toRadians((targetHeading))).getNormalized();

            }
        };
    }


    /**
     * PointMotion is used to give the robot cartesian coordinates that start at (0,0) at a decided corner of the field
     * @param turnRadius this determines how tight the turn between the points will be
     * @param points these are all of the positions that the robot will travel to
     * @return this returns a normalized vector to be comprehended by {@link org.firstinspires.ftc.teamcode.Op.ComplexOp}
     */
    public static Interfaces.MotionCalc PointMotion(final double turnRadius, final Vector2D... points) {

        final Interfaces.OtherCalc telemtryMap = OtherCalcs.TelemetryPosition();

        return new Interfaces.MotionCalc() {

            private double myProgress = 0;//this is what is returned for progress
            private boolean firstLoop = true;//firstLoop is for determining what the start position is
            private double totalDist = 0;//the total distance the robot will need to travel
            private double worldDist = 0;//the amount of distance traveled // the worldDist/totalDist = to myProgress in rough terms
            private ArrayList<Vector2D> ePosArray = new ArrayList<Vector2D>();//this is the vector the robot is traveling towards
            private ArrayList<Vector2D> preEPosArray = new ArrayList<Vector2D>();//this is the vector the robot is coming from
            private ArrayList<SegmentData> segmentDataArray = new ArrayList<SegmentData>();//this is a list of necessary information about the segments
            private int currentSegmentIndex = 0;
            // ^ is used for determining the distance traveled


            /**
             * CalcWorldDists adds all of the different segments between all of the points
             * to determine the total distance needed to travel this is used to find the progress throughout the calc
             * @return the total amount of distance needed to travel
             */
            double CalcWorldDists()
            {
                double total = 0;
                double lastDist = 0;
                for (int i=0; i < segmentDataArray.size(); i++)
                {
                    total += segmentDataArray.get(i).length;
                    segmentDataArray.get(i).worldStartDist = lastDist;
                    segmentDataArray.get(i).worldEndDist = total;
                    lastDist = total;
                }
                return total;
            }


            /**
             * GetSegmentByWorldDist finds the segment based on distance traveled
             * @param worldDist is the total amount of distance traveled to determine the segment
             * @return a {@link SegmentData}
             */
            SegmentData GetSegmentByWorldDist(double worldDist){
                for (int i=0; i < segmentDataArray.size(); i++)
                {
                    SegmentData candidate = segmentDataArray.get(i);

                    if (worldDist >= candidate.worldStartDist &&
                            worldDist <= candidate.worldEndDist)
                    {
                        return segmentDataArray.get(i);
                    }
                }
                // Return the last one if none are in range
                return segmentDataArray.get(segmentDataArray.size()-1);
            }



            /**
             * The data that is used for defining a segment
             */
            class SegmentData{
                double worldStartDist;
                double worldEndDist;
                double length;
                Vector2D startDirection;
                Vector2D startPos;
                Vector2D endPos;
            }


            /**
             * This is a simplified implementation of {@link SegmentData}
             */
            class StraightData extends SegmentData {
                /**
                 * StraightData sets the length for a segment
                 * @param length this is the length of the segment
                 */
                StraightData(double length){
                    this.length = length;
                }
            }


            /**
             * CurveData includes the information for defining a curve
             */
            class CurveData extends SegmentData{
                double curveLength;
                double arcAngle;
                double arcSegCoverDist;
                double radius = turnRadius;

                /**
                 * This is an extension of {@link SegmentData} using only the arcAngle and
                 * the other information provided in the parameters of PointMotion
                 * @param arcAngle is the theta between two vectors
                 */
                CurveData (double arcAngle){
                    this.arcAngle = arcAngle;
                    this.arcSegCoverDist = radius/Math.tan(Math.toRadians(Math.abs(this.arcAngle/2)));//was* radius
                    this.curveLength = Math.abs(Math.PI*2.0*radius*(180-(Math.abs(this.arcAngle)))/360.0);
                    this.length = curveLength;
                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                //to initialize all of the end points
                if (firstLoop) {
                    //setting the first point to where the robot starts || this could also be a chosen starting position
                    preEPosArray.add(0, d.wPos.clone());

                    CurveData lastCurve = null;
                    for (int i = 0; i < points.length; i++) {
                        //copying all of the arguments from PointMotion into a this class// because I can
                        ePosArray.add(i, points[i].clone());
                        //setting the previous endpoint
                        // i+1 pre instead of i-1 e avoids having to deal with null pointer exceptions
                        preEPosArray.add(i + 1, ePosArray.get(i).clone());
                    }
                    for (int i = 0; i < points.length; i++) {
                        double fullSegmentLength = ePosArray.get(i).distance(preEPosArray.get(i));

                        SegmentData tempStraight = new StraightData(fullSegmentLength);
                        if (lastCurve != null)
                        {
                            // Remove the first portion of the straight due to the curve
                            tempStraight.length -= lastCurve.arcSegCoverDist;
                        }
                        tempStraight.startDirection = ePosArray.get(i).getSubtracted(preEPosArray.get(i)).getNormalized();
                        tempStraight.startPos = preEPosArray.get(i).clone();
                        tempStraight.endPos = ePosArray.get(i).clone(); // To be replaced if there is a curve
                        segmentDataArray.add(tempStraight);

                        // Add the curve info
                        if (i < points.length - 1) {
                            //EDITING HERE
//                            Vector2D preVec = new Vector2D(ePosArray.get(i).x - preEPosArray.get(i).x,
//                                    ePosArray.get(i).y - preEPosArray.get(i).y);
//                            Vector2D postVec = new Vector2D(ePosArray.get(i + 1).x - ePosArray.get(i).x,
//                                    ePosArray.get(i + 1).y - ePosArray.get(i).y);
                            Vector2D preVec = new Vector2D(ePosArray.get(i).getSubtracted(preEPosArray.get(i)));////////////////////////////
                            Vector2D postVec = new Vector2D(ePosArray.get(i+1).getSubtracted(ePosArray.get(i)));////////////////////////////
                            double arcAngDiff = Vector2D.angleDifferenceDeg(postVec, preVec);
                            CurveData tempCurve = new CurveData(arcAngDiff);
                            tempCurve.startDirection = tempStraight.startDirection.clone();
                            tempCurve.startPos = ePosArray.get(i).getSubtracted(tempCurve.startDirection.getMultiplied(tempCurve.arcSegCoverDist));
                            tempCurve.endPos = tempCurve.startPos.getRotatedBy(Math.toRadians(tempCurve.arcAngle));
                            segmentDataArray.add(tempCurve);
                            lastCurve = tempCurve;
                            tempStraight.length -= tempCurve.arcSegCoverDist;
                            tempStraight.endPos = tempCurve.startPos.clone();
                        }

                    }
                    totalDist = CalcWorldDists();
                    d.debugData1 = totalDist;
                    //so it doesn't loop again //very important
                    firstLoop = false;
                }



                telemtryMap.CalcOther(d);


                SegmentData currentSegment = segmentDataArray.get(currentSegmentIndex);
                myProgress = (currentSegment.worldStartDist + d.wPos.distance(currentSegment.startPos)) / totalDist;
//                double segmentProgress = (worldDist - currentSegment.worldStartDist)/currentSegment.length;
                double segmentProgress = (currentSegment.startPos.distance(d.wPos))/currentSegment.length;
                if(segmentProgress > .99) {///changed from .97
                    currentSegmentIndex++;
                    if(currentSegmentIndex >= segmentDataArray.size()){
                        myProgress = 1.00;
                        return new Vector2D();
                    } else {
                        currentSegment = segmentDataArray.get(currentSegmentIndex);
                    }
                }
                //System.out.print("segmentProgress: ");System.out.println(segmentProgress);
                Vector2D rval = null;
                if (currentSegment instanceof CurveData)
                {
                    CurveData curveSegment = (CurveData) currentSegment;
                    rval = currentSegment.startDirection.getRotatedBy(segmentProgress * Math.toRadians(curveSegment.arcAngle));
                }
                else
                {
                    rval = currentSegment.endPos.getSubtracted(d.wPos).getNormalized();
                    //Alternative that doesn't use current location: rval = currentSegment.startDirection;
                }

                return rval;
            }
        };
    }


//
//    public static Interfaces.MotionCalc DriveTowardsDuckBlue(){
//        return new Interfaces.MotionCalc() {
//            double myProgress = 0.0;
//            @Override
//            public Vector2D CalcMotion(Interfaces.MoveData d) {
//                //find the ducks offset from the center of the intake
//
//                int duckOffset  = d.robot.findDuckPipeline.duckOffset;
//
//                //if it will run into a wall it will move on
//                if(d.wPos.x > 5.8 || d.wPos.x < 0.2 || d.wPos.y > 2.0 || (d.wPos.y < 0.3 && d.wPos.x < 0.4)){
//                    myProgress = 1.0;
//                }
//                //if the duck offset is less than 20 it will go forward
//                //the ducks offset to the left or right is proportional to speed in teh y direction
//                if(duckOffset == -160) return new Vector2D(0.0, 1.0);
//                return new Vector2D(Math.abs(duckOffset)<20?-1.0: 0.0, duckOffset/160.0);
//            }
//
//            @Override
//            public double myProgress(Interfaces.MoveData d) {
//                return myProgress;
//            }
//        };
//    }

    public static Interfaces.MotionCalc PointMotionNoProgress(final double turnRadius, final Vector2D... points) {


        return new Interfaces.MotionCalc() {

            private double myProgress = 0;//this is what is returned for progress
            private boolean firstLoop = true;//firstLoop is for determining what the start position is
            private double totalDist = 0;//the total distance the robot will need to travel
            private double worldDist = 0;//the amount of distance traveled // the worldDist/totalDist = to myProgress in rough terms
            private ArrayList<Vector2D> ePosArray = new ArrayList<Vector2D>();//this is the vector the robot is traveling towards
            private ArrayList<Vector2D> preEPosArray = new ArrayList<Vector2D>();//this is the vector the robot is coming from
            private ArrayList<SegmentData> segmentDataArray = new ArrayList<SegmentData>();//this is a list of necessary information about the segments
            private SegmentData currentSegment = null;
            // ^ is used for determining the distance traveled


            /**
             * CalcWorldDists adds all of the different segments between all of the points
             * to determine the total distance needed to travel this is used to find the progress throughout the calc
             * @return the total amount of distance needed to travel
             */
            double CalcWorldDists()
            {
                double total = 0;
                double lastDist = 0;
                for (int i=0; i < segmentDataArray.size(); i++)
                {
                    total += segmentDataArray.get(i).length;
                    segmentDataArray.get(i).worldStartDist = lastDist;
                    segmentDataArray.get(i).worldEndDist = total;
                    lastDist = total;
                }
                return total;
            }


            /**
             * GetSegmentByWorldDist finds the segment based on distance traveled
             * @param worldDist is the total amount of distance traveled to determine the segment
             * @return a {@link SegmentData}
             */
            SegmentData GetSegmentByWorldDist(double worldDist){
                for (int i=0; i < segmentDataArray.size(); i++)
                {
                    SegmentData candidate = segmentDataArray.get(i);

                    if (worldDist >= candidate.worldStartDist &&
                            worldDist <= candidate.worldEndDist)
                    {
                        return segmentDataArray.get(i);
                    }
                }
                // Return the last one if none are in range
                return segmentDataArray.get(segmentDataArray.size()-1);
            }



            /**
             * The data that is used for defining a segment
             */
            class SegmentData{
                double worldStartDist;
                double worldEndDist;
                double length;
                Vector2D startDirection;
                Vector2D startPos;
                Vector2D endPos;
            }


            /**
             * This is a simplified implementation of {@link SegmentData}
             */
            class StraightData extends SegmentData {
                /**
                 * StraightData sets the length for a segment
                 * @param length this is the length of the segment
                 */
                StraightData(double length){
                    this.length = length;
                }
            }


            /**
             * CurveData includes the information for defining a curve
             */
            class CurveData extends SegmentData{
                double curveLength;
                double arcAngle;
                double arcSegCoverDist;
                double radius = turnRadius;

                /**
                 * This is an extension of {@link SegmentData} using only the arcAngle and
                 * the other information provided in the parameters of PointMotion
                 * @param arcAngle is the theta between two vectors
                 */
                CurveData (double arcAngle){
                    this.arcAngle = arcAngle;
                    this.arcSegCoverDist = Math.tan(Math.toRadians(this.arcAngle/2))*radius;
                    this.curveLength = Math.abs(Math.PI*2.0*radius*this.arcAngle/360.0);
                    this.length = curveLength;
                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                //to initialize all of the end points
                if (firstLoop) {
                    //setting the first point to where the robot starts || this could also be a chosen starting position
                    preEPosArray.add(0, d.wPos.clone());

                    CurveData lastCurve = null;
                    for (int i = 0; i < points.length; i++) {
                        //copying all of the arguments from PointMotion into a this class// because I can
                        ePosArray.add(i, points[i].clone());
                        //setting the previous endpoint
                        // i+1 pre instead of i-1 e avoids having to deal with null pointer exceptions
                        preEPosArray.add(i + 1, ePosArray.get(i).clone());
                    }
                    for (int i = 0; i < points.length; i++) {
                        double fullSegmentLength = ePosArray.get(i).distance(preEPosArray.get(i));

                        SegmentData tempStraight = new StraightData(fullSegmentLength);
                        if (lastCurve != null)
                        {
                            // Remove the first portion of the straight due to the curve
                            tempStraight.length -= lastCurve.arcSegCoverDist;
                        }
                        tempStraight.startDirection = ePosArray.get(i).getSubtracted(preEPosArray.get(i)).getNormalized();
                        tempStraight.startPos = preEPosArray.get(i).clone();
                        tempStraight.endPos = ePosArray.get(i).clone(); // To be replaced if there is a curve
                        segmentDataArray.add(tempStraight);

                        // Add the curve info
                        if (i < points.length - 1) {
                            Vector2D preVec = new Vector2D(ePosArray.get(i).x - preEPosArray.get(i).x,
                                    ePosArray.get(i).y - preEPosArray.get(i).y);
                            Vector2D postVec = new Vector2D(ePosArray.get(i + 1).x - ePosArray.get(i).x,
                                    ePosArray.get(i + 1).y - ePosArray.get(i).y);
                            double arcAngDiff = Vector2D.angleDifferenceDeg(postVec, preVec);
                            CurveData tempCurve = new CurveData(arcAngDiff);
                            tempCurve.startDirection = tempStraight.startDirection.clone();
                            tempCurve.startPos = ePosArray.get(i).getSubtracted(tempCurve.startDirection.getMultiplied(tempCurve.arcSegCoverDist));
                            tempCurve.endPos = tempCurve.startPos.getRotatedBy(Math.toRadians(tempCurve.arcAngle));
                            segmentDataArray.add(tempCurve);
                            lastCurve = tempCurve;
                            tempStraight.length -= tempCurve.arcSegCoverDist;
                            tempStraight.endPos = tempCurve.startPos.clone();
                        }
                    }
                    totalDist = CalcWorldDists();
                    currentSegment = segmentDataArray.get(0);
                    //so it doesn't loop again //very important
                    firstLoop = false;
                }

                worldDist += d.wPos.distance(d.preWPos);
                //Making a ratio of how much we have traveled over what we should travel to create progress
                myProgress = worldDist / totalDist;

                SegmentData currentSegment = GetSegmentByWorldDist(worldDist);


                double segmentProgress = (worldDist - currentSegment.worldStartDist)/currentSegment.length;
                //System.out.print("segmentProgress: ");System.out.println(segmentProgress);
                Vector2D rval = null;
                if (currentSegment instanceof CurveData)
                {
                    CurveData curveSegment = (CurveData) currentSegment;
                    rval = currentSegment.startDirection.getRotatedBy(segmentProgress * Math.toRadians(curveSegment.arcAngle));
                }
                else
                {
                    rval = currentSegment.endPos.getSubtracted(d.wPos).getNormalized();
                    //Alternative that doesn't use current location: rval = currentSegment.startDirection;
                }
                return rval;
            }
        };
    }

    public static Interfaces.MotionCalc ConstantDistanceToPoint(final double distance, final Vector2D inputPoint){
        return new Interfaces.MotionCalc() {
            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                Vector2D point = new Vector2D(inputPoint);
                double currDistance = d.wPos.distance(point);
                double magnitude = currDistance-distance;
                point.subtract(d.wPos);
                point.normalizeNotZero();
                point.multiply(magnitude);
                point.normalizeSquareSmaller();
                Vector2D perp = new Vector2D(inputPoint.clone());
                perp.perp();
                perp.normalizeNotZero();
                perp.multiply((d.driver.ls().x));
                point.add(perp);
                return point;
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };
    }

    public static Interfaces.MotionCalc AlignPost() {
        return new Interfaces.MotionCalc() {
            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                Vector2D leftCameraDirection = new Vector2D(1, 0);
                leftCameraDirection.rotateBy(Math.toRadians(-26.0));
                Vector2D rightCameraDirection = new Vector2D(1, 0);
                rightCameraDirection.rotateBy(Math.toRadians(55.0));


                double leftError = d.robot.leftPoleAlignPipeline.distanceFromCenterHigh() / 120.0;
                double rightError = d.robot.rightPoleAlignPipeline.distanceFromCenterHigh() / 120.0;
//                    if(leftError>.5)
//                        leftError=.5;
//                    if(rightError>.5)
//                        rightError=.5;
                Vector2D tempTotalVector = leftCameraDirection.getMultiplied(-leftError).getAdded(rightCameraDirection.getMultiplied(-rightError));
                if(tempTotalVector.getLength()>.5){
                    return tempTotalVector.getNormalized().getDivided(2.0);
                } else {
                    return tempTotalVector;
                }

            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };
    }



}
