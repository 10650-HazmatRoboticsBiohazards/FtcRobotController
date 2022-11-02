//package org.firstinspires.ftc.teamcode.ftc10650.Tele;
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//
//import org.firstinspires.ftc.teamcode.Calculators.*;
//import org.firstinspires.ftc.teamcode.Op.ComplexOp;
//import org.firstinspires.ftc.teamcode.Utilities.Vector2D;
//import org.firstinspires.ftc.teamcode.Utilities.Vector3D;
//
//import java.util.Vector;
//
//@TeleOp(name = "Odom Test", group = "ftc10650")
//public class OdometryTest extends ComplexOp {
//
//    Vector<SpeedCalcs.ProgressSpeed> s = new Vector<SpeedCalcs.ProgressSpeed>();
//    Vector<Vector3D> p = new Vector<Vector3D>();
//
//    @Override
//    public Interfaces.MoveData.StartData startPositionAndOrientation() {
//        return new Interfaces.MoveData.StartData(new Vector2D(50, 50), 90);
//    }
//
//    @Override
//    public void initMove() throws InterruptedException {
//
////        s.add(new SpeedCalcs.ProgressSpeed(0.05, 0, SpeedCalcs.ProgressSpeed.timeOrProg.PROG));
////        s.add(new SpeedCalcs.ProgressSpeed(0.2, 1, SpeedCalcs.ProgressSpeed.timeOrProg.PROG));
////        p.add(new Vector3D(0, 0.3f, 0.3f));
//        d.robot.barm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        d.robot.tarm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        while(d.robot.bop.getState()) {
//            d.robot.barm.setPower(-0.2);
//            d.robot.tarm.setPower(-0.08);
//        }
//        d.robot.barm.setPower(0.0);
//        d.robot.barm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        d.robot.barm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        d.initBarmPos = d.robot.barm.getCurrentPosition();
////        d.robot.barm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        while(d.robot.top.getState()) {
//            d.robot.tarm.setPower(0.2);
//        }
//        d.robot.tarm.setPower(0.0);
//        d.robot.tarm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        d.robot.tarm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        d.initTarmPos = d.robot.tarm.getCurrentPosition();
//        d.initSarmPos = d.robot.sarm.getCurrentPosition();
////        d.robot.tarm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//    }
//
//    @Override
//    public void body() throws InterruptedException {
//
//        ComplexMove(
////                null,
////null,
////null,
////                SpeedCalcs.SetSpeed(1.0),
//               SpeedCalcs.JoystickSpeed(),
////                MotionCalcs.ObjectCentricJoystick(),
//                MotionCalcs.FieldCentricJoystick(0),
//                //MotionCalcs.ConstantDistanceToPoint(100, new Vector2D(100,100)),
//                OrientationCalcs.turnWithJoystick(),
////                OrientationCalcs.lookToOrientationUnderJoystick(0),
////                OrientationCalcs.lookToGoal(),
////                OrientationCalcs.lookToPower(),
////                OtherCalcs.Arm(),
////                OtherCalcs.ArmPath(s, p),
//
//                OtherCalcs.Arm2D(),
//                OtherCalcs.FindTurnToCube(),
//                OtherCalcs.Claw(),
//                OtherCalcs.Duck(),
//                OtherCalcs.TelemetryPosition());
//                /*OrientationCalcs.lookToPointTurnWithBumperTurnWithJoystick(
//                        "a",
//                        new OrientationCalcs.lookProgress(new Vector2D(0,0),0.95),
//                        new OrientationCalcs.lookProgress(new Vector2D(150,150),1.0)),*/
//        /**
//         * OtherCalcs.armPath(speeds... , points...
//         */
//    }
//}
