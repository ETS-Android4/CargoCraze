package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

import java.io.ByteArrayInputStream;
@TeleOp (name = "DsOp")
public class DsOP extends OpMode {
    Robot bsgRobot = new Robot();


    @Override
    public void init() {
        bsgRobot.init(hardwareMap);

        bsgRobot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bsgRobot.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bsgRobot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bsgRobot.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Movements", "L/R Sticks");
        telemetry.update();

    }

    @Override
    public void loop() {
        if (Math.abs(gamepad1.left_stick_y) > .1) {
            bsgRobot.drive(gamepad1.left_stick_y * 0.7);
            telemetry.addLine("Driving F/B");
            telemetry.update();
        } else if (gamepad1.right_stick_x < 0 || gamepad1.right_stick_x > 0) {
            bsgRobot.drive(gamepad1.right_stick_x, -gamepad1.right_stick_x, gamepad1.right_stick_x, -gamepad1.right_stick_x);
            telemetry.addLine("Turning L/R");
            telemetry.update();
        }
        //strafe right
        else if (gamepad1.left_stick_x < .3) {
            bsgRobot.frontRight.setPower(-gamepad1.left_stick_x / 2);
            bsgRobot.backRight.setPower(gamepad1.left_stick_x / 2);
            bsgRobot.frontLeft.setPower(gamepad1.left_stick_x / 2);
            bsgRobot.backLeft.setPower(-gamepad1.left_stick_x / 2);
            telemetry.addLine("Strafe L");
            telemetry.update();
        }
        //strafe left
        else if (gamepad1.left_stick_x > .3) {
            bsgRobot.frontRight.setPower(-gamepad1.left_stick_x / 2);
            bsgRobot.backRight.setPower(gamepad1.left_stick_x / 2);
            bsgRobot.frontLeft.setPower(gamepad1.left_stick_x / 2);
            bsgRobot.backLeft.setPower(-gamepad1.left_stick_x / 2);
            telemetry.addLine("Strafe R");
            telemetry.update();
        } else {
            bsgRobot.stopWheels();

        }

        if(gamepad2.dpad_up){
            bsgRobot.arm.setPower(-1);
        }

            else if (gamepad2.dpad_down){
                bsgRobot.arm.setPower(1);
            }
            else{
            bsgRobot.arm.setPower(0);
        }
            if(gamepad2.dpad_down){
                bsgRobot.extend.setPower(1);
            }
            else if (gamepad2.dpad_up){
                bsgRobot.extend.setPower(-1);
            }
            else{
                bsgRobot.extend.setPower(0);
            }
            if (gamepad2.right_trigger < .1){
                bsgRobot.intake.setPower(1);
            }
            else if(gamepad2.left_trigger < .1){
                bsgRobot.intake.setPower(1);
        } 
            else{
                bsgRobot.intake.setPower(0);
            }
            if(gamepad1.x){
                bsgRobot.dtSpeed = 0.6;
            }
            else{
                bsgRobot.dtSpeed = 1.0;
            }
        }
    }

