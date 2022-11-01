package org.firstinspires.ftc.teamcode.Hardware.RobertoMap;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Hardware.Sensors.DuckSpotPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


public class RobotMap {

    public static DcMotor bleft, fleft, bright, fright;

    public static DcMotorEx bleftEx, fleftEx, brightEx, frightEx;

    public static BNO055IMU gyro;

    public static OpenCvCamera frontCamera;

    public final DuckSpotPipeline duckSpotPipeline = new DuckSpotPipeline();

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
        PIDFCoefficients pidDrive = new PIDFCoefficients(20, 12, 5, 17.5);//p5 i2 d5 f17.5

        bleft = hw.get(DcMotor.class, "bleft");
        bleft.setDirection(DcMotorSimple.Direction.FORWARD);
        bleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bleftEx = (DcMotorEx) bleft;
        bleftEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);

        fleft = hw.get(DcMotor.class, "fleft");
        fleft.setDirection(DcMotorSimple.Direction.FORWARD);
        fleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fleftEx = (DcMotorEx) fleft;
        fleftEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);

        bright = hw.get(DcMotor.class, "bright");
        bright.setDirection(DcMotorSimple.Direction.REVERSE);
        bright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bright.setDirection(DcMotorSimple.Direction.REVERSE);
        bright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brightEx = (DcMotorEx) bright;
        brightEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);

        fright = hw.get(DcMotor.class, "fright");
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        fright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        fright.setDirection(DcMotorSimple.Direction.REVERSE);
        fright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frightEx = (DcMotorEx) fright;
        frightEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidDrive);


        gyro = hw.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;

        gyro.initialize(parameters);


        int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());
        WebcamName webcamName = hw.get(WebcamName.class, "ClawCam");
        frontCamera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        frontCamera.openCameraDeviceAsync(
                new OpenCvCamera.AsyncCameraOpenListener() {
                    @Override
                    public void onOpened() {
                        frontCamera.startStreaming(640, 480, OpenCvCameraRotation.UPSIDE_DOWN);
                        frontCamera.setPipeline(duckSpotPipeline);
                    }

                    @Override
                    public void onError(int errorCode) {

                    }
                }
        );

    }
}
