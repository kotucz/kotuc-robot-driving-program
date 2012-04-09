package jBotBrain.JBrainTest3;

import jBotBrain.hw.AnalogInput;
import jBotBrain.hw.Button;
import jBotBrain.hw.DigitalOutput;

public class LaserReflect {

	
	/**
	 * 
	 * main method.
	 * 
	 * turning laser on and off. comparing light levels on sensor.
	 *  when laser reflects, the difference in light levels is significant 
	 *  
	 */
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
	
}
