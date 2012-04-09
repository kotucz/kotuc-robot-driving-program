package jBotBrain.JBrainTest3;

import jBotBrain.hw.Motor;

public class Motors {

	Motor leftm = Motor.A;
	Motor rightm = Motor.B;

	public void setMotorsFT(int forward, int turn) {

		setMotorsLR(forward + turn, forward - turn);

	}

	public void setMotorsLR(int left, int right) {

//		System.out.println("setMotorsLR "+left+" "+right);

		if (Math.abs(left)>255 || Math.abs(right)>255) {
			System.out.println("illegal speed abort "+left+" "+right);
			return;
		}
		
//		left = Math.max(-63, Math.min(left, 63));
//		right = Math.max(-63, Math.min(right, 63));

		leftm.setDirection((left > 0) ? Motor.MOTOR_FORWARD
				: Motor.MOTOR_BACKWARD);
		rightm.setDirection((right < 0) ? Motor.MOTOR_FORWARD
				: Motor.MOTOR_BACKWARD);

//		leftm.setSpeed(Math.min(Math.abs(left), 255));		
//		rightm.setSpeed(Math.min(Math.abs(right), 255));

		leftm.setSpeed(Math.min(Math.abs(left), 255));
		rightm.setSpeed(Math.min(Math.abs(right), 255));

		
	}

	public void stop() {
		setMotorsLR(0, 0);
	}

}
