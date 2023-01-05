package org.firstinspires.ftc.teamcode.Op;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Calculators.OtherCalcs;
//import org.firstinspires.ftc.teamcode.Hardware.Sensors.Camera;
import org.firstinspires.ftc.teamcode.Hardware.RobertoMap.RobotMap;
import org.firstinspires.ftc.teamcode.Utilities.*;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Calculators.Interfaces;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.CompleteController;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDrive;

import java.net.*;

public abstract class ComplexOp extends LinearOpMode{

    private MecanumDrive mecanumDrive;

    double previousHeading = 0;

//    private Vector2D slamraOffset = new Vector2D(-2.0, -16.0);

//    private Pose2d initPose = null;
//    private double initPoseX = 0.0;
//    private double initPoseY = 0.0;

    public void ComplexMove(Interfaces.SpeedCalc speedCalc,
                            Interfaces.MotionCalc motionCalc,
                            Interfaces.OrientationCalc orientationCalc,
                            Interfaces.OtherCalc... otherCalc) throws InterruptedException {

        d.progress = 0;

        Vector2D vector = new Vector2D();

        float endGameTime = 0;

        d.lastCommand = d.currentCommand;
        d.currentCommand = new Interfaces.MoveData.Command(0, vector,0.0);
        DatagramSocket ds = null;
//        try {
//            ds = new DatagramSocket();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }


        while(d.progress < 1.0) {
            //_______________________
//            if(ds != null) {
//                InetAddress ip = null;
//                try {
//                    ip = InetAddress.getByName("192.168.43.255");
//                    //ip = InetAddress.getLocalHost();
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//                String str = String.valueOf(System.currentTimeMillis())+":"+String.valueOf(Math.abs(d.robot.intakeEx.getVelocity())+":"+Double.toString(d.intakeCommand));
//                byte[] strBytes = str.getBytes();
//                DatagramPacket DpSend =
//                        new DatagramPacket(strBytes, strBytes.length, ip, 10650);
//                try {
//                    ds.send(DpSend);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            //_______________________
            telemetry.addData("lift position", d.robot.lift.getCurrentPosition());


//            Orientation orientation = d.robot.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
//            double heading = -orientation.thirdAngle-d.startData.StartNorthOffset;
//            Orientation orientation = d.robot.gyro.getAngularOrientation();//OLD INTERFACE

//            double heading = orientation.firstAngle-d.startHeading-d.startData.StartNorthOffset;
            YawPitchRollAngles robotOrientation = d.robot.gyro.getRobotYawPitchRollAngles();
            double heading = robotOrientation.getYaw(AngleUnit.DEGREES);
            double diffHeading = heading - previousHeading;
            telemetry.addData("raw heading", heading);
//            telemetry.addData("d.startHeading", d.startHeading);
            telemetry.addData("d.StartNorthOffset", d.startData.StartNorthOffset);
            telemetry.addData("firstAngle", robotOrientation.getYaw(AngleUnit.DEGREES));


            if(diffHeading > 180.0) {
                diffHeading -= 360.0;
            } else if (diffHeading <= -180.0){
                diffHeading += 360.0;
            }
            d.heading += diffHeading;
            previousHeading = heading;
            telemetry.addData("d.heading", d.heading);
//
//            telemetry.addData("gryo", orientation.thirdAngle);
//            telemetry.addData("orientation", d.heading);

            double distanceCorrectionFactorForward = 0.019;
            double distanceCorrectionFactorSide = 0.01864;
            Vector2D encoderPre = d.encoderPos.clone();
            long lastEncoderUpdateTime = d.encodePosUpdateTimeMillis;
            d.encoderPos = mecanumDrive.getVectorDistanceCm();
            telemetry.addData("encoderPos",d.encoderPos);
            d.encodePosUpdateTimeMillis = System.currentTimeMillis();
            Vector2D deltaMove = d.encoderPos.getSubtracted(encoderPre);
            double unitsForwardPerTile = 135.0/4.0;
            double unitsStrafePerTile = 156.0/4.0;
            deltaMove.x /= unitsStrafePerTile;
            deltaMove.y /= unitsForwardPerTile;
            Vector2D moveSpeed = deltaMove.getDivided(Math.max(0.001,(d.encodePosUpdateTimeMillis - lastEncoderUpdateTime)/1000.0));
//            moveSpeed.x *= distanceCorrectionFactorForward;
//            moveSpeed.y *= distanceCorrectionFactorSide;
//            // moveSpeed.y + is moving forward
//            // moveSpeed.x + is moving robot to the right
//            // First value is y value of robot (moving forward == positive)
//            // Second value is x value of robot (positive to left of robot)
//            d.robot.slamra.sendOdometry(-moveSpeed.x,moveSpeed.y);
//
//            telemetry.addData("initPoseX","%.3f", initPoseX);
//            telemetry.addData("initPoseY","%.3f", initPoseY);
//            telemetry.addData("initPoseDeg","%.3f", initPose.getRotation().getDegrees());
//
////            telemetry.addData("encodeMoveSpeed X","%.3f", moveSpeed.x);
////            telemetry.addData("encodeMoveSpeed Y","%.3f", moveSpeed.y);
//
            deltaMove.rotateBy(Math.toRadians(-d.heading));//WAS -d.heading !!!!!!!!!!!!!!!!!!!!//180+d.heading
            d.preWPos.set(d.wPos);
            d.wPos.add(deltaMove);
//            T265Camera.CameraUpdate cameraUpdate = d.robot.slamra.getLastReceivedCameraUpdate();
//            Pose2d p = cameraUpdate.pose;
//            telemetry.addData("velocity",cameraUpdate.velocity);
//            telemetry.addData("confidence",cameraUpdate.confidence);
//            telemetry.addData("new X",p.getTranslation().getX());
//            telemetry.addData("new Y",p.getTranslation().getY());
//
//            telemetry.addData("intake pos", d.robot.intake.getCurrentPosition());
////            System.out.println("\n init:" + initPoseX + "    :    " + initPoseY);
////            System.out.println(" pose" + p.getTranslation().getX() + "    :    " + p.getTranslation().getY());
//            Vector2D slamraPos = new Vector2D(
//                    (p.getTranslation().getX()-initPoseX)*100.0,
//                    (p.getTranslation().getY()-initPoseY)*100.0);
////            slamraPos.subtract(slamraOffset);
//            slamraPos.rotateBy(-Math.toRadians(startPositionAndOrientation().StartNorthOffset + initPose.getRotation().getDegrees()));
//
//            slamraPos.add(startPositionAndOrientation().StartPos);
////            slamraPos.subtract(slamraOffset);
//
////            slamraPos.subtract(slamraOffset);
//
//
//            d.heading = p.getRotation().getDegrees() - initPose.getRotation().getDegrees();
//
////            slamraPos.add(slamraOffset.getRotatedBy(Math.toRadians(d.heading)));
//
//            d.wPos.set(slamraPos);
            telemetry.addData("distance from center", d.debugData2);


            if(orientationCalc != null) d.currentCommand.orientationSpeed = orientationCalc.CalcOrientation(d);
            if(motionCalc != null) {
                d.currentCommand.motionSpeed = motionCalc.CalcMotion(d);
                d.currentCommand.motionSpeed.rotateBy(Math.toRadians(-d.heading));
            }
            if(speedCalc != null) d.currentCommand.speed = speedCalc.CalcSpeed(d);

            for (Interfaces.OtherCalc calc : otherCalc) calc.CalcOther(d);

            if (d.timeRemainingUntilEndgame >= 0) endGameTime = (float)(Math.round(d.timeRemainingUntilEndgame / 100) / 10.0);

            telemetry.addData("Robot is here", "\n"+d.field);
            telemetry.addData("Position", d.wPos.x + "   " + d.wPos.y);
            telemetry.addData("Left Driver Joystick", d.driver.ls());
            telemetry.addData("Left Driver Joystick Raw", "[" + gamepad1.left_stick_x + ", " + gamepad1.left_stick_y + "]");
            telemetry.update();


            mecanumDrive.driveMecanum(
                    d.currentCommand.motionSpeed,
                    d.currentCommand.speed,
                    d.currentCommand.orientationSpeed);



            d.progress = MathUtil.findMaxList(
                    motionCalc == null ? 0 : motionCalc.myProgress(d),
                    orientationCalc == null ? 0 : orientationCalc.myProgress(d),
                    speedCalc == null ? 0 : speedCalc.myProgress(d));


            for (Interfaces.OtherCalc calc : otherCalc) d.progress = Math.max(d.progress,calc.myProgress(d));


            Thread.sleep(10);


            if (!opModeIsActive()) throw new InterruptedException();
        }
    }

    //How data is transferred between calculators and complexOp
    public Interfaces.MoveData d = new Interfaces.MoveData();//if you delete this the world will end


    void initHardware(HardwareMap hwMap) {

        telemetry.addData("ENTERED INIT HARDWARE", "<-");
        d.telemetry = telemetry;
        d.robot = new RobotMap(hwMap);//, startPositionAndOrientation());
//        d.robot.slamra.setPose(new Pose2d(0, 0,
////                startPositionAndOrientation().StartPos.x/100,
////                startPositionAndOrientation().StartPos.y/100,
//
//                new Rotation2d(0)));//Math.toRadians(startPositionAndOrientation().StartHeading))));
//        d.robot.slamra.start();



        mecanumDrive = new MecanumDrive(d);
    }

    public abstract Interfaces.MoveData.StartData startPositionAndOrientation();

    public abstract void body() throws InterruptedException;

    public void initMove() throws InterruptedException{

    }

//    public void saveInitialHeading(boolean forceSave){
//        if(forceSave || !d.startHeadingIsSet){
//            d.startHeading = d.robot.gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
//            d.startHeadingIsSet = true;
//        }
//    }

    void exit(){//so we don't run into a wall at full speed
//        d.lastFrameBarmPos = d.robot.barm.getCurrentPosition();
//        d.lastFrameTarmPos = d.robot.tarm.getCurrentPosition();
//        d.lastFrameSarmPos = d.robot.sarm.getCurrentPosition();
//        d.initBarmPos = d.initBarmPos - d.robot.barm.getCurrentPosition();// - d.firstFrameBarmPos ;
//        d.initTarmPos = d.initTarmPos - d.robot.tarm.getCurrentPosition();// - d.firstFrameTarmPos ;
//        d.initSarmPos = d.initSarmPos - d.robot.sarm.getCurrentPosition();// - d.firstFrameSarmPos ;
        d.robot.bright.setPower(0);
        d.robot.fright.setPower(0);
        d.robot.bleft.setPower(0);
        d.robot.fleft.setPower(0);
//        d.robot.tarm.setPower(0);
//        d.robot.barm.setPower(0);
//        d.robot.sarm.setPower(0);

//        d.robot.slamra.stop();

    }



    @Override
    public void runOpMode() throws InterruptedException{

        //INITIALIZATION
        telemetry.addData("Initializing", "Started");
        telemetry.update();


        d.isFinished = false;
        d.isStarted = false;

        d.driver = new CompleteController(gamepad1);
        d.manip = new CompleteController(gamepad2);

        //
        //
        //try {
        initHardware(hardwareMap);
        //} catch (Exception e){
        //    StringWriter sw = new StringWriter();
        //    PrintWriter pw = new PrintWriter(sw);
        //    e.printStackTrace(pw);
        //    telemetry.addData(sw.toString(), "this");
        //}
        //
        //

        //START POSITION
        if (d.startData == null) {
            d.startData = this.startPositionAndOrientation();
            d.preWPos.set(d.startData.StartPos.clone());
            d.wPos.set(d.startData.StartPos.clone());
        }

        final Interfaces.OtherCalc posDisplay = OtherCalcs.TelemetryPosition();
        posDisplay.CalcOther(d);

        telemetry.addData("Place robot here", "\n"+d.field);
        telemetry.addData("heading"," "+d.startData.StartNorthOffset +" | position: ("+String.valueOf((d.startData.StartPos.x))+", "+String.valueOf((d.startData.StartPos.y))+")");
        telemetry.update();

//        d.firstFrameBarmPos = d.robot.barm.getCurrentPosition();
//        d.firstFrameTarmPos = d.robot.tarm.getCurrentPosition();
//        d.firstFrameSarmPos = d.robot.sarm.getCurrentPosition();
//        d.firstLiftPos = d.robot.lift.getCurrentPosition();




//        saveInitialHeading(true);
        initMove();


        waitForStart();



//        initPose = d.robot.slamra.getLastReceivedCameraUpdate().pose;
//        d.telemetry.addData("confidence", d.robot.slamra.getLastReceivedCameraUpdate().confidence);
//        initPoseX = initPose.getTranslation().getX();
//        initPoseY = initPose.getTranslation().getY();
        d.telemetry.update();

        d.isStarted = true;

        //BODY
        try {
            body();
        } catch (InterruptedException ie) {
            telemetry.addData("Interrupted","Exception");
            telemetry.update();
        }
        telemetry.addData("Body", "Finished");
        telemetry.update();

        //EXIT
        telemetry.addData("Exit", "Started");
        telemetry.update();
        exit();
        d.isFinished = true;
        telemetry.addData("Exit", "Finished");
        telemetry.update();
    }
}
