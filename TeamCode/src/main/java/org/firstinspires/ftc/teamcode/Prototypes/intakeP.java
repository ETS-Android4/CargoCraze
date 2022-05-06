package org.firstinspires.ftc.teamcode.Prototypes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.Robot;
@TeleOp (name = "intakeP")
public class intakeP extends OpMode {

    Robot bsgRobot = new Robot();
    @Override
    public void init() {
        telemetry.addData("Spin", "Trigger L/R");
        telemetry.update();
    }

    @Override
    public void loop() {
        if (gamepad2.left_trigger < .1){
            bsgRobot.intake.setPower(1);
        }
        else if(gamepad2.right_trigger < .1){
            bsgRobot.intake.setPower(-1);
        }
        else{
            bsgRobot.intake.setPower(0);
        }

    }
}
