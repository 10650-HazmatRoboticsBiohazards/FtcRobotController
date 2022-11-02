//package org.firstinspires.ftc.teamcode.ftc10650.Tele;
//
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.PIDCoefficients;
//import com.qualcomm.robotcore.hardware.PIDFCoefficients;
//
//import org.firstinspires.ftc.teamcode.Calculators.Interfaces;
//import org.firstinspires.ftc.teamcode.Calculators.MotionCalcs;
//import org.firstinspires.ftc.teamcode.Calculators.OrientationCalcs;
//import org.firstinspires.ftc.teamcode.Calculators.OtherCalcs;
//import org.firstinspires.ftc.teamcode.Calculators.SpeedCalcs;
//import org.firstinspires.ftc.teamcode.Op.ComplexOp;
//import org.firstinspires.ftc.teamcode.Utilities.Vector2D;
//import org.firstinspires.ftc.teamcode.Utilities.Vector3D;
//
//import java.util.Vector;
//
//@TeleOp(name = "Raw Arm", group = "ftc10650")
//public class RawArm extends ComplexOp {
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
//    public void body() throws InterruptedException {
//        double pCoef = 20;
//        double dCoef = 0;
//        boolean lastUpU = false;
//        boolean lastUpD = false;
//        boolean lastUpR = false;
//        boolean lastUpL = false;
//        PIDFCoefficients pidfCoefficients = new PIDFCoefficients();
////        d.telemetry.addData("string", "string");
////        d.robot.tarm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
////        d.robot.barm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//
//        while(opModeIsActive()) {
////            telemetry.addData("string2", "string2");
//
//            d.robot.tarm.setPower(d.manip.ls().y / 2.0);
//            d.robot.barm.setPower(d.manip.ls().x / 2.0);
////            d.robot.tarmEx.setPower(d.manip.rs().x / 5.0);
//            if(d.manip.u() && !lastUpU) {
//                pCoef += 0.5;
//            }
//            if (d.manip.d() && !lastUpD) {
//                pCoef -= 0.5;
//            }
//            if(d.manip.r() && !lastUpR) {
//                dCoef += 0.5;
//            }
//            if (d.manip.l() && !lastUpL) {
//                dCoef -= 0.5;
//            }
//
//            lastUpU = d.manip.u();
//            lastUpD = d.manip.d();
//            lastUpR = d.manip.r();
//            lastUpL = d.manip.l();
//
//            pidfCoefficients.p = 20;
//            pidfCoefficients.i = 0;
//            pidfCoefficients.d = dCoef;
//            pidfCoefficients.f = pCoef;
//            d.telemetry.addData("p", pCoef);
//            d.telemetry.addData("d", dCoef);
//            d.robot.tarmEx.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
//            telemetry.update();
//            Thread.sleep(10);
//
//        }
//        /**
//         * OtherCalcs.armPath(speeds... , points...
//         */
//    }
//}
