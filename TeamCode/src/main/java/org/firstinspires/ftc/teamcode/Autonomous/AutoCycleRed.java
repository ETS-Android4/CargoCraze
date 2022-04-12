package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Robot;

import java.sql.Driver;

public class AutoCycleRed extends LinearOpMode {
    Robot bsgRobot = new Robot();

    /*
   *
   VARIABLES FOR ENCODERS
   *
   */
    private ElapsedTime runtime = new ElapsedTime();

    static final double COUNTS_PER_MOTOR_REV = 384.5;    // gobilda 435
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 3.78;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;

    //For turning with encoders
    Integer cpr = 28; //counts per rotation originally 28
    Double gearratio = 13.7; //IDK IT WAS ORIGINALLY 40
    Double diameter = 4.0;
    Double cpi = (cpr * gearratio) / (Math.PI * diameter); //counts per inch, 28cpr * gear ratio / (2 * pi * diameter (in inches, in the center))
    Double bias = 0.8;//default 0.8
    Double meccyBias = 0.9;//change to adjust only strafing movement (was .9)
    //
    Double conversion = cpi * bias;
    Boolean exit = false;

    //values for arm
    static final double     COUNTS_PER_ARM_MOTOR_REV    = 537.7;  // eg: TETRIX Motor Encoder //2150.8
    static final double     ARM_GEAR_REDUCTION    = 0.3;        // This is < 1.0 if geared UP
    static final double     SPROCKET_DIAMETER_INCHES   = 2.5;     // For figuring circumference

    static final double     ARM_PER_INCH         = (COUNTS_PER_ARM_MOTOR_REV * ARM_GEAR_REDUCTION) / (SPROCKET_DIAMETER_INCHES * 3.1415);
    static final double     LVL_1_INCHES         = 15;
    static final double     LVL_2_INCHES         = 18;
    static final double     LVL_3_INCHES         = 21;

    @Override
    public void runOpMode() throws InterruptedException {
        bsgRobot.init(hardwareMap);
        bsgRobot.initIMU(hardwareMap);

        encoderDrive(DRIVE_SPEED, 46,46,3);

        encoderDrive(DRIVE_SPEED, 10,0, 3);

        encoderDrive(DRIVE_SPEED, 4, 4, 3);

        bsgRobot.extend.setPower(1);
        sleep(200);

        bsgRobot.stopMotors();

        bsgRobot.intake.setPower(1);
        sleep(500);

        bsgRobot.stopMotors();

        bsgRobot.arm.setPower(-1);
        sleep(500);

        encoderDrive(DRIVE_SPEED, -10,-10,3);

        armEncoderDrive(DRIVE_SPEED, LVL_3_INCHES,3);

        bsgRobot.intake.setPower(-1);
        sleep(400);

        bsgRobot.stopMotors();

        bsgRobot.arm.setPower(1);
        sleep(300);

        bsgRobot.stopMotors();

        encoderDrive(DRIVE_SPEED, 12,12,3);

        bsgRobot.intake.setPower(1);
        sleep(300);

        bsgRobot.stopMotors();

        bsgRobot.arm.setPower(-1);
        sleep(500);

        encoderDrive(DRIVE_SPEED, -10,-10,3);

        armEncoderDrive(DRIVE_SPEED, LVL_3_INCHES,3);

        bsgRobot.intake.setPower(-1);
        sleep(400);

        bsgRobot.stopMotors();


    }
    /*
      *
      *
      *
      FUNCTIONS FOR ENCODERS
      *
      *
      *
      *
       */
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newFrontLeftTarget;
        int newFrontRightTarget;
        int newBackLeftTarget;
        int newBackRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFrontLeftTarget = bsgRobot.frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newBackLeftTarget = bsgRobot.backLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newFrontRightTarget = bsgRobot.frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newBackRightTarget = bsgRobot.backRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

            bsgRobot.frontLeft.setTargetPosition(newFrontLeftTarget);
            bsgRobot.backLeft.setTargetPosition(newBackLeftTarget);
            bsgRobot.frontRight.setTargetPosition(newFrontRightTarget);
            bsgRobot.backRight.setTargetPosition(newBackRightTarget);


            // Turn On RUN_TO_POSITION
            bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            bsgRobot.frontLeft.setPower(Math.abs(speed));
            bsgRobot.backLeft.setPower(Math.abs(speed));
            bsgRobot.frontRight.setPower(Math.abs(speed));
            bsgRobot.backRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (bsgRobot.frontLeft.isBusy() && bsgRobot.frontRight.isBusy() &&
                            bsgRobot.backLeft.isBusy() && bsgRobot.backRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newFrontLeftTarget, newFrontRightTarget,
                        newBackLeftTarget, newBackRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        bsgRobot.frontLeft.getCurrentPosition(),
                        bsgRobot.backLeft.getCurrentPosition(),
                        bsgRobot.frontRight.getCurrentPosition(),
                        bsgRobot.backRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            bsgRobot.stopWheels();

            // Turn off RUN_TO_POSITION
            bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    //strafing with encoders
    public void strafeToPosition(double inches, double speed) {
        //
        int move = (int) (Math.round(inches * cpi * meccyBias * 1.265));
        //
        bsgRobot.backLeft.setTargetPosition(bsgRobot.backLeft.getCurrentPosition() - move);
        bsgRobot.frontLeft.setTargetPosition(bsgRobot.frontLeft.getCurrentPosition() + move);
        bsgRobot.backRight.setTargetPosition(bsgRobot.backRight.getCurrentPosition() + move);
        bsgRobot.frontRight.setTargetPosition(bsgRobot.frontRight.getCurrentPosition() - move);
        //
        bsgRobot.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bsgRobot.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bsgRobot.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bsgRobot.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //
        bsgRobot.frontLeft.setPower(speed);
        bsgRobot.backLeft.setPower(speed);
        bsgRobot.frontRight.setPower(speed);
        bsgRobot.backRight.setPower(speed);
        //
        while (bsgRobot.frontLeft.isBusy() && bsgRobot.frontRight.isBusy() &&
                bsgRobot.backLeft.isBusy() && bsgRobot.backRight.isBusy()) {
        }
        bsgRobot.frontRight.setPower(0);
        bsgRobot.frontLeft.setPower(0);
        bsgRobot.backRight.setPower(0);
        bsgRobot.backLeft.setPower(0);
        return;
    }
    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void armEncoderDrive(double speed,
                                double inches,
                                double timeoutS) {
        int newarmTarget;
        // int newBackLeftTarget;
        //  int newFrontRightTarget;
        //  int newBackRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            bsgRobot.extend.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            // frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            // backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            // backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            // Determine new target position, and pass to motor controller
            newarmTarget = bsgRobot.extend.getCurrentPosition() + (int) (inches * ARM_PER_INCH);
            // newBackLeftTarget = backLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            // newFrontRightTarget = frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            // newBackRightTarget = backRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            bsgRobot.extend.setTargetPosition(newarmTarget);
            //  backLeft.setTargetPosition(newBackLeftTarget);
            //  frontRight.setTargetPosition(newFrontRightTarget);
            //  backRight.setTargetPosition(newBackRightTarget);
            // Turn On RUN_TO_POSITION
            bsgRobot.extend.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            // reset the timeout time and start motion.
            runtime.reset();
            bsgRobot.extend.setPower(Math.abs(speed));
            // backLeft.setPower(Math.abs(speed));
            //  frontRight.setPower(Math.abs(speed));
            //  backRight.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (bsgRobot.extend.isBusy())) {// frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d ", newarmTarget);//newBackLeftTarget, newFrontRightTarget, newBackRightTarget);
                telemetry.addData("Path2", "Running at %7d  ",
                        bsgRobot.extend.getCurrentPosition());
                // backLeft.getCurrentPosition(),
                // frontRight.getCurrentPosition(),
                //  backRight.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            bsgRobot.extend.setPower(0);
            //backLeft.setPower(0);
            // frontRight.setPower(0);
            // backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            bsgRobot.extend.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            // backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
}
