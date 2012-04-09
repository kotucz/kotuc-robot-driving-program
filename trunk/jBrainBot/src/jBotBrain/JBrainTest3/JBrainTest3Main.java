package jBotBrain.JBrainTest3;

import jBotBrain.comm.BTCommPort;
import jBotBrain.comm.CommPort;
import jBotBrain.hw.AnalogInput;
import jBotBrain.hw.Button;
import jBotBrain.hw.DigitalOutput;
import jBotBrain.hw.Led;
import jBotBrain.hw.Motor;

//import jBotBrain.sensors.CMPS03Sensor;

public class JBrainTest3Main {

	static final byte MODE_FT = 17;

	public static void main(String[] args) {

		// DigitalOutput.H.setValue(true);
		//
		// Led.RED.set(Led.MODE_BLINK, 1000);
		//
		// System.out.println("press any button");
		//
		// Button.waitForPress();
		//
		// Led.RED.set(Led.MODE_OFF, 1000);
		//
		// System.out.println("start");

		// stepper();

		// Led.GREEN1.set(Led.MODE_BLINK, 100);

		// drive();

		bluetoothDriving();

		// Button.waitForPress();

	}

	static void bluetoothDriving() {

		System.out.println("bluetooth driving");

		Motors stss = new Motors();

		CommPort bt = new BTCommPort();

		while (true) {

			Led.GREEN2.set(Led.MODE_OFF, 100);
			Led.ORANGE.set(Led.MODE_BLINK, 100);
			System.out.println("Waiting for connection");

			while (bt.getStatus() != CommPort.DEVICE_CONNECTED) {
			}

			bt.writeString("Welcome! use binary ");
			System.out.println("connected");
			Led.ORANGE.set(Led.MODE_OFF, 100);
			Led.GREEN2.set(Led.MODE_BLINK, 100);

			final byte CTRL = -128;

			// int ST = 0;
			int stl = 0;
			int str = 0;

			byte[] buffer = new byte[20];

			while (bt.getStatus() == CommPort.DEVICE_CONNECTED) {

				//byte b = readByte(bt);

				int readen = bt.read(buffer, 0, 20);
				if (readen > 0) {
					bt.write(buffer, 0, readen); // echo
					//System.out.println("\nr " + readen);
					// lets hope overcurrent will not be a problem
//					 System.out.println("Current: " + Motor.A.getMotorCurrent()
//					 + " " + Motor.B.getMotorCurrent());
				}

				for (int i = 0; i < readen; i++) {
					byte b = buffer[i];
					//System.out.print("b " + (int)b);

					if (CTRL == b) {
						stss.setMotorsLR(2*stl, 2*str);
					} else {
						stl = str;
						str = b;
					}
				}

				
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

				
			}
			stss.stop();
			// Led.ledSetAll(false);

		}
	}

	static byte readByte(CommPort cp) {
		byte[] buffer = new byte[1];
		while (cp.read(buffer, 0, 1) == 0) {
			// busy wait loop
		}
		return buffer[0];
	}

	static void terminalDriving() {

		System.out.println("terminal driving");

		Motors stss = new Motors();

		CommPort bt = new BTCommPort();

		while (true) {

			Led.GREEN2.set(Led.MODE_OFF, 100);
			Led.ORANGE.set(Led.MODE_BLINK, 100);
			System.out.println("Waiting for connection");

			while (bt.getStatus() != CommPort.DEVICE_CONNECTED) {
			}

			bt.writeString("Welcome! use WASD ");
			System.out.println("connected");
			Led.ORANGE.set(Led.MODE_OFF, 100);
			Led.GREEN2.set(Led.MODE_BLINK, 100);

			int speed = 50;

			while (bt.getStatus() == CommPort.DEVICE_CONNECTED) {

				// int readen = bt.read(buffer, 0, 1);
				// System.out.print("" + readen + ": ");
				// for (int i = 0; i < readen; i++) {
				// System.out.print("" + buffer[i] + " ");
				// }

				// System.out.print((readen > 0) ? "\n" : "\r");

				// if (readen > 0)
				{
					Led.GREEN1.set(Led.MODE_BLINK, 100);
					// bt.write(buffer, 0, readen); // echo
					// switch (buffer[0]) {
					switch (readByte(bt)) {
					case 's':
						// Led.ledSetAll(false);
						stss.setMotorsLR(speed, speed);
						// stss.setMotor(true, (byte) 33);
						// stss.setMotor(false, (byte) 33);
						break;
					case 'd':
						// Led.ledSetAll(false);

						stss.setMotorsLR(speed, -speed);
						// stss.setMotor(true, (byte) -33);
						// stss.setMotor(false, (byte) 33);
						break;
					case 'a':
						// Led.ledSetAll(false);
						// Led.ORANGE.set(Led.MODE_BLINK, 100);
						stss.setMotorsLR(-speed, speed);
						// stss.setMotor(true, (byte) 33);
						// stss.setMotor(false, (byte) -33);

						break;
					case 'w':
						// Led.ledSetAll(false);
						// Led.GREEN1.set(Led.MODE_BLINK, 100);
						stss.setMotorsLR(-speed, -speed);
						// stss.setMotor(true, (byte) -33);
						// stss.setMotor(false, (byte) -33);
						break;
					case 'k':
						speed += 5;
						System.out.println("speed up: " + speed);
						break;
					case 'j':
						speed -= 5;
						System.out.println("speed down: " + speed);
						break;
					// case MODE_FT:
					// Led.ledSetAll(false);
					// Led.ORANGE.set(Led.MODE_BLINK, 100);
					// stss.setMotorsFT(buffer[1], buffer[2]);
					// // stss.setMotor(true, (byte) -33);
					// // stss.setMotor(false, (byte) -33);
					// break;
					default:
						stss.stop();
						// Led.ledSetAll(false);
						// Led.RED.set(Led.MODE_BLINK, 100);
					}

				}
				// try {
				// Thread.sleep(100);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// // e.printStackTrace();
				// }
				System.out.println("Current: " + Motor.A.getMotorCurrent()
						+ " " + Motor.B.getMotorCurrent());
			}
			stss.stop();
			// Led.ledSetAll(false);

		}
	}

}
