package org.firstinspires.ftc.teamcode.Calculators;

//import org.firstinspires.ftc.teamcode.Hardware.Sensors.GoalPositionPipeline;
//import org.firstinspires.ftc.teamcode.Hardware.Sensors.PowerShotPositionPipeline;
//import org.firstinspires.ftc.teamcode.Hardware.Sensors.StackDeterminationPipeline;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Utilities.*;
//import org.openftc.easyopencv.OpenCvCamera;
//import org.openftc.easyopencv.OpenCvCameraRotation;

public class OtherCalcs {


    public static Interfaces.OtherCalc Raise(){
        return  new Interfaces.OtherCalc() {
            double myProgress = 0.0;
            @Override
            public void CalcOther(Interfaces.MoveData d) {

                int counter = 0;
                while (d.robot.lift.getCurrentPosition()<1715) {
                        d.robot.lift.setTargetPosition(1720);
                        d.robot.liftEx.setVelocity(1720.0/3.0);
                        d.robot.pitch.setPosition(.75);
                    }
                while (d.robot.pitch.getPosition()>.51){
                        d.robot.pitch.setPosition(.46);
                    }
                d.robot.claw.setPosition(0.4);
                myProgress = 1.0;


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

                int counter = 0;
                while (d.robot.lift.getCurrentPosition()>10) {

                    d.robot.lift.setTargetPosition(10);
                    d.robot.liftEx.setVelocity(1720.0/3.0);
                    d.robot.pitch.setPosition(.75);
                }

                myProgress = 1.0;



            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }
        };
    }


    public static Interfaces.OtherCalc Lift(){
        return  new Interfaces.OtherCalc() {
            @Override
            public void CalcOther(Interfaces.MoveData d) {





                if(d.manip.a()){
                    d.robot.claw.setPosition(0.4);
                }
                else{
                    d.robot.claw.setPosition(.23);
                }

                if(d.manip.u()) {
                    d.robot.lift.setTargetPosition(1825);
                    d.robot.pitch.setPosition(.5);
                } else if (d.manip.d()){
                    d.robot.lift.setTargetPosition(10);
                    d.robot.pitch.setPosition(0.4375);
                }
                d.robot.liftEx.setVelocity(1825/2.0);
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
