package org.firstinspires.ftc.teamcode.Calculators;

//import org.firstinspires.ftc.teamcode.Hardware.Sensors.GoalPositionPipeline;
//import org.firstinspires.ftc.teamcode.Hardware.Sensors.PowerShotPositionPipeline;
//import org.firstinspires.ftc.teamcode.Hardware.Sensors.pipelines.StackDeterminationPipeline;

import org.firstinspires.ftc.teamcode.Utilities.*;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraRotation;

public class OtherCalcs {

    public static Interfaces.OtherCalc TeleAlignPost() {
        return new Interfaces.OtherCalc() {
            @Override
            public void CalcOther(Interfaces.MoveData d) {
                if (d.manip.y() || d.driver.y()) {
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
                    if(tempTotalVector.getLength()>.4){
                        d.robotCentricAdditiveVector =  tempTotalVector.getNormalized().getDivided(1/0.4);
                    } else {
                        d.robotCentricAdditiveVector = tempTotalVector;
                    }


                } else {
                    d.robotCentricAdditiveVector = new Vector2D();
                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };

    }

    public static Interfaces.OtherCalc AutoAlignPost() {

        return new Interfaces.OtherCalc() {
            @Override
            public void CalcOther(Interfaces.MoveData d) {

                Vector2D leftCameraDirection = new Vector2D(1, 0);
                leftCameraDirection.rotateBy(Math.toRadians(-26.0));
                Vector2D rightCameraDirection = new Vector2D(1, 0);
                rightCameraDirection.rotateBy(Math.toRadians(55.0));


                double leftError = d.robot.leftPoleAlignPipeline.distanceFromCenterHigh() / 100.0; // was 120
                double rightError = d.robot.rightPoleAlignPipeline.distanceFromCenterHigh() / 100.0; // was 120
//                    if(leftError>.5)
//                        leftError=.5;
//                    if(rightError>.5)
//                        rightError=.5;
                Vector2D tempTotalVector = leftCameraDirection.getMultiplied(-leftError).getAdded(rightCameraDirection.getMultiplied(-rightError));
                if(tempTotalVector.getLength()>.3){ // was .4
                    d.robotCentricAdditiveVector =  tempTotalVector.getNormalized().getDivided(1/0.3); // was .4
                } else {
                    d.robotCentricAdditiveVector = tempTotalVector;
                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };

    }

    public static Interfaces.OtherCalc Raise(){
        return  new Interfaces.OtherCalc() {
            double myProgress = 0.0;
            int state = 0;
            double startTime = 0.0;
            @Override
            public void CalcOther(Interfaces.MoveData d) {
                switch(state) {
                    case 0:
                        d.robot.lift.setTargetPosition(1620);
                        d.robot.liftEx.setVelocity(1620.0 / 3.0);
                        d.robot.pitch.setPosition(.75);

                        if(d.robot.lift.getCurrentPosition() >= 1600) state++;
                        break;
                    case 1:
                        startTime = System.currentTimeMillis();
                        state++;
                        break;
                    case 2:
                        d.robot.pitch.setPosition(.46);


                        if((System.currentTimeMillis() - startTime) > 500) {
                            startTime = System.currentTimeMillis();
                            state++;
                        }
                        break;
                    case 3:
                        d.robot.claw.setPosition(0.4);
                        if((System.currentTimeMillis() - startTime) > 500) {
                            startTime = System.currentTimeMillis();
                            state++;
                        }
                        break;

                    default:
                        myProgress = 1.0;
                }

            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }
        };
    }

    public static Interfaces.OtherCalc ShakeClaw(){
        return  new Interfaces.OtherCalc() {
            double myProgress = 0.0;
            @Override
            public void CalcOther(Interfaces.MoveData d) {
                if(d.progress < 0.5){
                    d.robot.pitch.setPosition(.55);
                } else {
                    d.robot.pitch.setPosition(.75);
                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }
        };
    }

    public static Interfaces.OtherCalc Lower(){
        return  new Interfaces.OtherCalc() {
            double myProgress = 0.0;
            @Override
            public void CalcOther(Interfaces.MoveData d) {


                d.robot.lift.setTargetPosition(10);
                d.robot.liftEx.setVelocity(1720.0/3.0);
                d.robot.pitch.setPosition(.75);


                if(d.robot.lift.getCurrentPosition()<=10) myProgress = 1.0;



            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }
        };
    }


    public static Interfaces.OtherCalc Lift(){
        return  new Interfaces.OtherCalc() {
            boolean isLiftUp = false;
            int targetLiftPos = 10;
            @Override
            public void CalcOther(Interfaces.MoveData d) {

                //bottom position 0.4375
                //slight position 0.5
                //top position 0.75

                //pitch control
                //left joystick is full up -> pitch should be vertical
                //left joystick is full down -> pitch full horizontal
                //let go of joystick go to original position





                if(d.manip.a()){
                    d.robot.claw.setPosition(0.4);
                }
                else{
                    d.robot.claw.setPosition(.23);
                }

                if(d.manip.u()) {
                    isLiftUp = true;
                    targetLiftPos = 1700;//1825;
                } else if (d.manip.d()){
                    isLiftUp = false;
                    targetLiftPos = 10;
                }

                double defaultPosition = 0.0;
                if(isLiftUp) defaultPosition = 0.2;


                targetLiftPos += (int)(d.manip.rs().y * 20);

                if(targetLiftPos > 1700) targetLiftPos = 1700;
                if(targetLiftPos < 10) targetLiftPos = 10;

                d.robot.lift.setTargetPosition(targetLiftPos);
                d.robot.liftEx.setVelocity(1825);///2.0);

                double pitchPercentPosition = 0.0;
                pitchPercentPosition = 1.0 - d.manip.rt();
/*                if(d.manip.ls().y < 0.0) {
                    pitchPercentPosition = (1.0 - defaultPosition) * -d.manip.ls().y + defaultPosition;
                } else if (d.manip.ls().y > 0.0) {
                    //if down
                    pitchPercentPosition = defaultPosition * -d.manip.ls().y + defaultPosition;
                } else {
                    pitchPercentPosition = defaultPosition;
                }*/

                d.robot.pitch.setPosition((0.75-.4/*0.4375*/) * pitchPercentPosition + .4/*0.4375*/);



            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0.0;
            }
        };
    }

    public static Interfaces.OtherCalc whileOpMode(){

        return new Interfaces.OtherCalc(){
            double myProgress;
            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                myProgress = 0.5;
            }
        };
    }

    public static Interfaces.OtherCalc TimeProgress(final double millis){
        return new Interfaces.OtherCalc() {
            final long initialMillis = System.currentTimeMillis();
            @Override
            public void CalcOther(Interfaces.MoveData d) {

            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return (System.currentTimeMillis() - initialMillis)/(millis);
            }
        };
    }

    public static Interfaces.OtherCalc PIDTest(){
        return new Interfaces.OtherCalc() {
            @Override
            public void CalcOther(Interfaces.MoveData d) {
                if(d.manip.u()){

                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };
    }


    public static Interfaces.OtherCalc TeleOpMatch(){

        final TimeUtil matchTime = new TimeUtil();
        final TimeUtil endGameTime = new TimeUtil();

        return new Interfaces.OtherCalc(){
            private double myProgress;
            private boolean firstLoop = true;
            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                if(firstLoop){
                    endGameTime.startTimer(120000);
                    matchTime.startTimer(150000);
                    firstLoop=false;
                }

                d.timeRemainingUntilEndgame = endGameTime.timeRemaining();
                d.timeRemainingUntilMatch = matchTime.timeRemaining();
                myProgress = 1-(d.timeRemainingUntilMatch/150000);

            }
        };
    }


    public static Interfaces.OtherCalc TelemetryPosition(){
        return new Interfaces.OtherCalc() {
            protected int stringFieldWidth = 24;
            protected int stringFieldHeight = 16;
            @Override
            public void CalcOther(Interfaces.MoveData d) {
                int realFieldWidth = 6;
                int realFieldHeight = 6;
                int adjustedColumn = (int)Math.round((d.wPos.x/realFieldWidth)* stringFieldWidth);
                int adjustedRow = (int)Math.round((d.wPos.y/realFieldHeight)* stringFieldHeight);
                String rval = "";
                for(int row = stringFieldHeight-1; row>-1; row--){
                if(row == stringFieldHeight-1) rval += "\u2004______________________________________________\n";
                rval += "|";

                for(int col = 0; col< stringFieldWidth; col++){

                if(row == adjustedRow && col == adjustedColumn){
                    rval += "■";
                } else {
                    rval += "\u2004\u2002";
                }

                if(col == stringFieldWidth-1) {
                    if (row == 0) {
                        rval += "|";
                    } else {
                        rval += "|\n";
                    }
                }

            }
            }
                rval += "_______________________________________________\u2004";
                d.field = rval;
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };
    }




    public static Interfaces.OtherCalc AutoOpMatch(){
        final TimeUtil autoTime = new TimeUtil();

        return new Interfaces.OtherCalc() {
            private double myProgress;
            protected int timeInAuto = 30_000;
            private boolean firstLoop = true;
            @Override
            public void CalcOther(Interfaces.MoveData d) {
                if(firstLoop){
                    autoTime.startTimer(timeInAuto);
                    firstLoop = false;
                }
                myProgress = 1-(autoTime.timeRemaining()/timeInAuto);
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }
        };

    }




    public enum Controller{
        DRIVER,
        MANIP
    }

    public enum Side {
        FRONT,
        BACK,
        RIGHT,
        LEFT
    }

}
