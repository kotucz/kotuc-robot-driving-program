package jBotBrain.JBrainTest3;

import jBotBrain.hw.Led;

/**
 * Simple obstacle avoidance using sonars
 * @author Acer
 *
 */
public class Breitenberg {

	/**
	 * main method
	 */
	static void drive() {
		// SaberToothSimpleSerial stss = new SaberToothSimpleSerial();
		Motors stss = new Motors();

		//bluetoothDriving(stss);

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
	
}
