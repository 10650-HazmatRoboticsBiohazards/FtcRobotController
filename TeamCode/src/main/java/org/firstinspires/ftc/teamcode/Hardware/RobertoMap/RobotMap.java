package org.firstinspires.ftc.teamcode.Hardware.RobertoMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.LeftCameraStackAlignPipeline;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.RightCameraStackAlignPipeline;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.SignalPipeline;
import org.jetbrains.annotations.NotNull;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.List;


public class RobotMap {

    public static DcMotor bleft, fleft, bright, fright, lift;

    public static DcMotorEx bleftEx, fleftEx, brightEx, frightEx, liftEx;

    public static Servo claw;

    public static IMU gyro;

    public static OpenCvCamera rightCamera, leftCamera;

    public final SignalPipeline signalPipeline = new SignalPipeline();

    public final LeftCameraStackAlignPipeline leftCameraStackAlignPipeline = new LeftCameraStackAlignPipeline();

    public final RightCameraStackAlignPipeline rightCameraStackAlignPipeline = new RightCameraStackAlignPipeline();

    public static HardwareMap hw;


    public RobotMap(HardwareMap hw) {

        this.hw = hw;
        /**
         * @see <a href="https://ftc-tricks.com/dc-motors/"</a>
         * @see DcMotor.RunMode.RUN_USING_ENCODER this implements a PID for all of the motors
         * This elminates the problems such as the inconsistent auto and having to charge the battery to full every use
         * @see DcMotorSimple.Direction.REVERSE is the correct place to change the directions of the motors
         * it should not be done in a higher level code this is the correct spot
         */
//PIDCoefficients pidDrive = new PIDCoefficients(50, 10, 0);
        PIDFCoefficients pidDrive = new PIDFCoefficients(10, 4, 1, 10);//p5 i2 d5 f17.5

        bleft = hw.get(DcMotor.class, "bleft");
        bleft.setDirection(DcMotorSimple.Direction.FORWARD);
        bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        bleftEx = (DcMotorEx) bleft;
        bleftEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);

        fleft = hw.get(DcMotor.class, "fleft");
        fleft.setDirection(DcMotorSimple.Direction.FORWARD);
        fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        fleftEx = (DcMotorEx) fleft;
        fleftEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);

        bright = hw.get(DcMotor.class, "bright");
        bright.setDirection(DcMotorSimple.Direction.REVERSE);
        bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        brightEx = (DcMotorEx) bright;
        brightEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);

        fright = hw.get(DcMotor.class, "fright");
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frightEx = (DcMotorEx) fright;
        frightEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);


        lift = hw.get(DcMotor.class, "lift");
        lift.setDirection(DcMotorSimple.Direction.REVERSE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setTargetPosition(lift.getCurrentPosition());
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftEx = (DcMotorEx) lift;
//        frightEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);


        claw = hw.get(Servo.class, "claw");



//        gyro = hw.get(BNO055IMU.class, "imu");
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//
//        parameters.mode                = BNO055IMU.SensorMode.IMU;
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.loggingEnabled      = false;
//
//        gyro.initialize(parameters);

        gyro = hw.get(IMU.class, "imu");
        gyro.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                        )
                )
        );

//        CameraManager cameraManager = ClassFactory.getInstance().getCameraManager();
//        List<WebcamName> webcams = cameraManager.getAllWebcams();
//        RobotLog.a("CAMERA TEST MESSAGE");
//        for(WebcamName w:webcams){
//            RobotLog.aa("CAMERA DEVICE NAME", w + " : "+w.getSerialNumber().toString()+" : "+w.getDeviceName().toString()+" : "+w.getManufacturer()+" : "+w.getVersion());
//
//        }
//        int[] viewportContainerIds = OpenCvCameraFactory.getInstance().sp
        int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());
        int[] viewportContainerIds = OpenCvCameraFactory.getInstance()
                .splitLayoutForMultipleViewports(
                        cameraMonitorViewId,
                        2,
                        OpenCvCameraFactory.ViewportSplitMethod.HORIZONTALLY);

        leftCamera = OpenCvCameraFactory.getInstance().createWebcam(
                hw.get(WebcamName.class, "LeftCamera"),
                viewportContainerIds[0]);

        rightCamera = OpenCvCameraFactory.getInstance().createWebcam(
                hw.get(WebcamName.class, "RightCamera"),
                viewportContainerIds[1]);

        leftCamera.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() {
                        leftCamera.startStreaming(640, 360, OpenCvCameraRotation.UPSIDE_DOWN);

                        leftCamera.setPipeline(leftCameraStackAlignPipeline);
                    }

                    @Override
                    public void onError(int errorCode) {

                    }
                }
        );

        rightCamera.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() {
                        rightCamera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

                        rightCamera.setPipeline(rightCameraStackAlignPipeline);
                    }

                    @Override
                    public void onError(int errorCode) {

                    }
                }
        );

    }
}
