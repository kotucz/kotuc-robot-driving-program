package jBotBrain.JBrainTest3;

import jBotBrain.hw.Button;
import jBotBrain.hw.Motor;

public class BipolarStepper {

	/***
	 * main method
	 */
	static void stepper() {

		BipolarStepper stepper = new BipolarStepper();

		while (true) {

			switch (Button.readButtons()) {
			case Button.S1_BUTTON:
				stepper.step--;
				stepper.refresh();
				break;
			case Button.S2_BUTTON:
				stepper.step++;
				stepper.refresh();
				break;
			default:
				stepper.sleep();
				break;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}

		}
	}
	
	Motor coila = Motor.A;
	Motor coilb = Motor.B;

	int step = 0;

	void refresh() {
		switch (step&3) {
		case 0:
			setCoils(1, 0);
			break;
		case 1:
			setCoils(0, 1);
			break;
		case 2:
			setCoils(-1, 0);
			break;
		case 3:
			setCoils(0, -1);
			break;
		default:
			setCoils(0, 0);
			break;
		}
	}

	void sleep() {
		setCoils(0, 0);
	}
	
	public void setCoils(int left, int right) {

		coila.setSpeed(0);
		coilb.setSpeed(0);

		// left = Math.max(-63, Math.min(left, 63));
		// right = Math.max(-63, Math.min(right, 63));

		left *= 255;
		right *= 255;

		coila.setDirection((left > 0) ? Motor.MOTOR_FORWARD
				: Motor.MOTOR_BACKWARD);
		coilb.setDirection((right < 0) ? Motor.MOTOR_FORWARD
				: Motor.MOTOR_BACKWARD);

		coila.setSpeed(Math.min(Math.abs(left), 150));
		coilb.setSpeed(Math.min(Math.abs(right), 150));

	}

}
