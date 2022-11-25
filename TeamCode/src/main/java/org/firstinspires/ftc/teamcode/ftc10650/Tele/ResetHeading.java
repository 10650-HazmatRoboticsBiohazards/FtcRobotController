package org.firstinspires.ftc.teamcode.ftc10650.Tele;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Calculators.Interfaces;
import org.firstinspires.ftc.teamcode.Hardware.RobertoMap.RobotMap;
import org.firstinspires.ftc.teamcode.Op.ComplexOp;


@TeleOp(name = "reset heading")

public class ResetHeading extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        new RobotMap(hardwareMap);
        waitForStart();
        RobotMap.gyro.resetYaw();
    }
}
