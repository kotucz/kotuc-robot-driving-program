package jBotBrain.JBrainTest3;

import jBotBrain.comm.UARTCommPort;

public class SaberToothSimpleSerial {

	final UARTCommPort uart = new UARTCommPort();

	// byte[] buffer = new byte[2];

	public SaberToothSimpleSerial() {

		uart.open(UARTCommPort.BR_9600, UARTCommPort.PARITY_NONE);

	}

	public void stop() {

		byte[] buffer = new byte[] { 0 };
		uart.write(buffer, 0, buffer.length);

	}

	public void setMotor(boolean right, int val) {

		val = Math.max(-63, Math.min(val, 63));

		byte[] buffer = new byte[] { (byte) ((right ? 128 : 0) + 64 + val) };
		uart.write(buffer, 0, buffer.length);

	}

	public void setMotorsFT(int forward, int turn) {

		setMotorsLR(forward + turn, forward - turn);

	}

	private int pl, pr;

	public void setMotorsLR(int left, int right) {

		left = Math.max(-63, Math.min(left, 63));
		right = Math.max(-63, Math.min(right, 63));

		if ((pr != right) || (pl != left)) {

			byte[] buffer = new byte[] { (byte) (0 + 64 + left),
					(byte) (128 + 64 + right)

			};

			System.out.println(" uart write "
					+ uart.write(buffer, 0, buffer.length));

		}
		pl = left;
		pr = right;

	}

	public void close() {
		uart.close();
	}

}
