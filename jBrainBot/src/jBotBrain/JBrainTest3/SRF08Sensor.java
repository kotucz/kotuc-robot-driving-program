package jBotBrain.JBrainTest3;

import jBotBrain.hw.I2CPort;

/**
 * SRF08 ultrasonic range measure sensor abstraction class.
 * 
 * <b>NOTE:</b> This class is not static, it must be allocated.
 * 
 * @author Tomas Kotula from Martin Wolf SRF02Sensor
 * 
 * @see http://www.robot-electronics.co.uk/htm/srf08tech.shtml
 *
 */
public class SRF08Sensor {

    	private static final int COMMAND_REGISTER = 0x00;
    	private static final int GAIN_REGISTER = 0x01;
    	private static final int PING_CM = 0x51;
    	private static final int RANGE_REGISTER = 0x02;
	
		/**
		 * Device address on I2C bus.
		 */
		private final int i2c_address;
		
		/**
		 * Constructor, set the device address on I2C bus.
		 */
		public SRF08Sensor(int address)	{
			i2c_address = address;
		}
		
		/**
		 * Read range from sensor in centimeters.
		 */
		public int getRange() {
//	        changeRange(26);
	        
	        // 0x51  	 Ranging Mode - Result in centimeters
			byte[] buffer = {26}; // range to 1 meter
			
			System.out.println("srf ping: ");
			System.out.println("ranges "+I2CPort.i2cStart(i2c_address, RANGE_REGISTER, 1, buffer, 1, I2CPort.I2C_WRITE));					
			
			buffer[0] = PING_CM;
			System.out.println("res "+I2CPort.i2cStart(i2c_address, COMMAND_REGISTER, 1, buffer, 1, I2CPort.I2C_WRITE));
			
			try {
				Thread.sleep(10); // since range is only 1 meter
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			buffer = new byte[2];
			
			System.out.println("read" + I2CPort.i2cStart(i2c_address, RANGE_REGISTER, 2, buffer, 2, I2CPort.I2C_READ));
//			
//			System.out.println("buffer "+buffer[0]+","+buffer[1]);
			
			int range = ((buffer[0] & 0xFF) << 8) + (buffer[1] & 0xFF);
			
			if( range == 0xFFFF ){
				return -1;
			}
			
			return range;
		}
	

		

}
