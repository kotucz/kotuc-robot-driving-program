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

		DigitalOutput.H.setValue(true);

		Led.RED.set(Led.MODE_BLINK, 1000);

		System.out.println("press any button");

		Button.waitForPress();

		Led.RED.set(Led.MODE_OFF, 1000);

		System.out.println("start");

		// stepper();

		// Led.GREEN1.set(Led.MODE_BLINK, 100);

		drive();

		// Button.waitForPress();

	}

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

	static void laserReflect() {
		while (true) {

			Button.waitForPress();

			DigitalOutput.H.setValue(false); // turn laser on

			int valon = -1;

			String line = "on:";

			for (int i = 0; i < 10; i++) {

				valon = AnalogInput.A.readValue_mV();

				line += valon + " ";

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}

			}

			System.out.println(line);

			DigitalOutput.H.setValue(true); // turn off
			int valoff = -1;

			line = "off: ";

			for (int i = 0; i < 10; i++) {

				valoff = AnalogInput.A.readValue_mV();

				line += valoff + " ";

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}

			}

			System.out.println(line);

			System.out.println("result: on: " + valon + " mV; off: " + valoff
					+ " mV; reflect: " + (valon - valoff));

			//
			// System.out.println("" + (int) System.currentTimeMillis()
			// + " voltage: " + System.getMainVoltage());
			// int i = 0;
			// int startt = (int) System.currentTimeMillis();
			// int t;
			// while ((t = (int) System.currentTimeMillis()) - startt < 1000) {
			// t = (int) System.currentTimeMillis();
			// int valon = AnalogInput.A.readValue_mV();

			// int valon = AnalogInput.A.readValue_mV();

			// // int val2 = AnalogInput.B.readValue_mV();
			// // System.out.print(""+t+" voltage: "+val);
			// System.out.println("" + t + " voltage: " + val);
			// Led.GREEN2.set((val > 200) ? Led.MODE_ON : Led.MODE_OFF, 0);
			// Led.ORANGE.set((val > 250) ? Led.MODE_ON : Led.MODE_OFF, 0);
			// Led.RED.set((val > 280) ? Led.MODE_ON : Led.MODE_OFF, 0);
			// i++;
			// }
			// System.out.println("\n***** " + i + "/10s");

			// DigitalOutput.H.setValue(true);
			//			
			// Button.waitForPress();
			//			
			// DigitalOutput.H.setValue(false);
			//			
			// Button.waitForPress();
			// try {
			// Thread.sleep(10);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
	}

	static void drive() {
		// SaberToothSimpleSerial stss = new SaberToothSimpleSerial();
		Motors stss = new Motors();

		bluetoothDriving(stss);

		// for (byte i = -63; i < 64; i++) {
		// System.out.println(""+i);
		//			
		// stss.setMotor(true, i);
		// stss.setMotor(false, i);
		//			
		// try {
		// Thread.sleep(50);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// // e.printStackTrace();
		// }
		// }

		SRF08Sensor[] sonars = { new SRF08Sensor(0xE2), new SRF08Sensor(0xE0),
				new SRF08Sensor(0xE4), };

		// CMPS03Sensor cmps = new CMPS03Sensor(0xC0);

		// Led.GREEN2.set(Led.MODE_BLINK, 200);

		int loops = 1;

		while (true) {

			System.out.println("loop " + loops);

			Led.ORANGE.set(Led.MODE_ON, 0);

			int[] ranges = new int[3];
			for (int i = 0; i < ranges.length; i++) {
				ranges[i] = sonars[i].getRange();
			}

			Led.ORANGE.set(Led.MODE_OFF, 0);
			System.out.println("Ranges: " + ranges[0] + " " + ranges[1] + " "
					+ ranges[2]);

			// System.out.println("azimuth: " +cmps.getAngleHighResolution());

			int fw = ranges[1] - 50;
			int turn = ranges[2] - ranges[0];

			stss.setMotorsFT(fw, turn);

			System.out.println("jkhk");

			try {

				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			loops++;

		}

		// UARTCommPort uart = new UARTCommPort();
		//		
		// uart.open(
		// UARTCommPort.BR_9600,
		// UARTCommPort.PARITY_NONE);
		//		
		// byte[] buffer = new byte[1];
		//		
		// for (byte i = 2; i != 1; i++) {
		// buffer[0] = i;
		//			
		// uart.write(buffer, 0, buffer.length);
		//			
		// System.out.println(""+i);
		//			
		// try {
		// Thread.sleep(50);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// // e.printStackTrace();
		// }
		// }
		//		
		// uart.close();

	}

	static void bluetoothDriving(Motors stss) {

		System.out.println("bluetooth driving");

		CommPort bt = new BTCommPort();
		byte[] buffer = new byte[20];

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
					case 0x7f: // next two bytes are left, right speeds
						stss.setMotorsLR(2 * readByte(bt), 2 * readByte(bt));
						break;
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
				// System.out.println("Current: " + Motor.A.getMotorCurrent()
				// + " " + Motor.B.getMotorCurrent());
			}
			stss.stop();
			// Led.ledSetAll(false);

		}
	}

	static byte readByte(CommPort cp) {
		byte[] buffer = new byte[1];
		while (cp.read(buffer, 0, 1) == 0) {
			//busy wait loop
		}
		return buffer[0];
	}

}
