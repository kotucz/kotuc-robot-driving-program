/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.kotuc.robot.driver.bt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	private SensorManager mSensorManager;

	// Layout Views
	private TextView mTitle;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	private Button mForwardButton;
	private Button mRightButton;
	private Button mLeftButton;
	private Button mStopButton;

    private JoyStickView leftJSV;
    private JoyStickView rightJSV;

	private ToggleButton mAccToogleButton;
	//
//	private LinearLayout mKeyPad;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;

	private SimulationView simView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// setContentView(R.layout.keypad);
		// getLayoutInflater().i
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		mForwardButton = (Button) findViewById(R.id.forwardButton);
		mRightButton = (Button) findViewById(R.id.rightButton);
		mLeftButton = (Button) findViewById(R.id.leftButton);
		mStopButton = (Button) findViewById(R.id.stopButton);

        leftJSV = (JoyStickView) findViewById(R.id.leftJS);
        rightJSV = (JoyStickView) findViewById(R.id.rightJS);

		mAccToogleButton = (ToggleButton) findViewById(R.id.acceleratorToogle);
		
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		((FrameLayout) findViewById(R.id.cursorFrame))
				.addView(simView = new SimulationView(this));

	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
		simView.startSimulation();
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.message);
		mConversationView = (ListView) findViewById(R.id.in);
		mConversationView.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});

		// Initialize the forward button with a listener that for click events

		// OnClickListener keypadListener = new OnClickListener() {
		// public void onClick(View v) {
		// Log.e("KEYPAD", "" + v.toString());
		// if (mForwardButton.equals(v)) {
		// sendMessage("w");
		// } else if (mRightButton.equals(v)) {
		// sendMessage("d");
		// } else if (mLeftButton.equals(v)) {
		// sendMessage("a");
		// } else if (mStopButton.equals(v)) {
		// sendMessage("x");
		// }
		// // Send a message using content of the edit text widget
		// // TextView view = (TextView) findViewById(R.id.edit_text_out);
		// // String message = view.getText().toString();
		//
		// }
		// };
		// mForwardButton.setOnClickListener(keypadListener);

		// mForwardButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// // Send a message using content of the edit text widget
		// TextView view = (TextView) findViewById(R.id.edit_text_out);
		// String message = view.getText().toString();
		// sendMessage(message);
		// }
		// });
		//
		// UUID MY_UUID =
		// UUID.fromString("00000000-0000-0000-0000-000000001101");

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	public synchronized void onButtonClick(View v) {
		Log.e("KEYPAD", "" + v.toString());
		if (mForwardButton.equals(v)) {
			sendMessage("w");
		} else if (mRightButton.equals(v)) {
			sendMessage("d");
		} else if (mLeftButton.equals(v)) {
			sendMessage("a");
		} else if (mStopButton.equals(v)) {
			sendMessage("x");
		} else if (mAccToogleButton.equals(v)) {
			if (mAccToogleButton.isChecked()) {
				// perhaps calibrate
			} else {
				sendMessage("xxx");
			}
		}
		// Send a message using content of the edit text widget
		// TextView view = (TextView) findViewById(R.id.edit_text_out);
		// String message = view.getText().toString();

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
		simView.stopSimulation();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			mOutEditText.setText(mOutStringBuffer);
		}
	}

	/**
	 * Sends a message.
	 *            A string of text to send.
	 */
	void sendSpeedCommandSpeedTurn(float speed, float turnr) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

        sendSpeedCommandLeftRight(speed+turnr, speed-turnr);

	}

    private void sendSpeedCommandLeftRight(float left, float right) {
        left = Math.max(-1f, Math.min(left, 1f));
        right = Math.max(-1f, Math.min(right, 1f));

        byte leftByte = (byte) Math.round(100 * left);
        byte rightByte = (byte) Math.round(100 * right);

        byte[] send = new byte[] { 0x7f, leftByte, rightByte };

        mChatService.write(send);
    }

    // The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					mConversationArrayAdapter.clear();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				mConversationArrayAdapter.add(mConnectedDeviceName + ":  "
						+ readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				// Get the device MAC address
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				// Get the BLuetoothDevice object
				BluetoothDevice device = mBluetoothAdapter
						.getRemoteDevice(address);
				// Attempt to connect to the device
				mChatService.connect(device);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			// Launch the DeviceListActivity to see devices and do scan
			Intent serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	class SimulationView extends View implements SensorEventListener {

		private Sensor mAccelerometer;
		private float mSensorX;
		private float mSensorY;
//		private long mSensorTimeStamp;
//		private long mCpuTimeStamp;
		private static final float maxG = 5f;

		public void startSimulation() {
			/*
			 * It is not necessary to get accelerometer events at a very high
			 * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
			 * automatic low-pass filter, which "extracts" the gravity component
			 * of the acceleration. As an added benefit, we use less power and
			 * CPU resources.
			 */
			mSensorManager.registerListener(this, mAccelerometer,
					SensorManager.SENSOR_DELAY_UI);
		}

		public void stopSimulation() {
			mSensorManager.unregisterListener(this);
		}

		public SimulationView(Context context) {
			super(context);
			mAccelerometer = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
				return;
			/*
			 * record the accelerometer data, the event's timestamp as well as
			 * the current time. The latter is needed so we can calculate the
			 * "present" time during rendering. In this application, we need to
			 * take into account how the screen is rotated with respect to the
			 * sensors (which always return data in a coordinate space aligned
			 * to with the screen in its native orientation).
			 */

			// switch (mDisplay.getRotation()) {
			// case Surface.ROTATION_0:
			// mSensorX = event.values[0];
			// mSensorY = event.values[1];
			// break;
			// case Surface.ROTATION_90:
			// mSensorX = -event.values[1];
			// mSensorY = event.values[0];
			// break;
			// case Surface.ROTATION_180:
			// mSensorX = -event.values[0];
			// mSensorY = -event.values[1];
			// break;
			// case Surface.ROTATION_270:
			// mSensorX = event.values[1];
			// mSensorY = -event.values[0];
			// break;
			// }
			mSensorX = event.values[0];
			mSensorY = event.values[1];

//			mSensorTimeStamp = event.timestamp;
//			mCpuTimeStamp = System.nanoTime();
		}

		float turnr = 0;
		float speed = 0;
		
		@Override
		protected void onDraw(Canvas canvas) {

			// int w = canvas.getWidth();
			// int h = canvas.getHeight();

			int w = getWidth();
			int h = getHeight();

//			final long now = mSensorTimeStamp
//					+ (System.nanoTime() - mCpuTimeStamp);

			final float sx = mSensorX / maxG;
			final float sy = mSensorY / maxG;

			float size1 = 50;
			float size2 = h / 2f;

			Paint p = new Paint();
			p.setColor(0xff74AC23);
			p.setStrokeWidth(5);
			p.setStyle(Paint.Style.STROKE);

			Paint fontp = new Paint();
			fontp.setTextSize(30);
			fontp.setColor(0xffC48C23);

			canvas.drawOval(new RectF((w - h) / 2f, 0, (w + h) / 2f, h), p);

			fontp.setColor(0xff74AC23);
			canvas.drawOval(new RectF(w / 2f - size1, h / 2f - size1, w / 2f
					+ size1, h / 2f + size1), p);

			turnr = -sx;
			speed = -sy;
					
			canvas.drawLine(w / 2f, h / 2f, w / 2f + size2 * turnr, h / 2f
					- size2 * speed, p);

			canvas.drawText("x: " + sx, w / 2f, h / 2f + 100, fontp);
			canvas.drawText("y: " + sy, w / 2f, h / 2f + 200, fontp);

			if (mAccToogleButton.isChecked()) {
				sendSpeedCommandSpeedTurn(speed, turnr);
				return;
			}
			
			if (motionEvent != null) {

				// p.setColor(0xff4723AC);
				//
				// final int historySize = motionEvent.getHistorySize();
				// final int pointerCount = motionEvent.getPointerCount();
				// Log.e("TOUCH", "hs: " + historySize + " pc: " +
				// pointerCount);
				// for (int ih = 0; ih < historySize; ih++) {
				// // System.out.printf("At time %d:",
				// // motionEvent.getHistoricalEventTime(ih));
				// for (int ip = 0; ip < pointerCount; ip++) {
				// // System.out.printf("  pointer %d: (%f,%f)",
				// // motionEvent.getPointerId(ip),
				// // motionEvent.getHistoricalX(ip, ih),
				// // motionEvent.getHistoricalY(ip, ih));
				// canvas.drawText("id: " + motionEvent.getPointerId(ip),
				// motionEvent.getHistoricalX(ip, ih),
				// motionEvent.getHistoricalY(ip, ih), fontp);
				// }
				// }

				p.setColor(0xffA7234C);

				for (int i = 0; i < motionEvent.getPointerCount(); i++) {

					float px = motionEvent.getX(i);
					float py = motionEvent.getY(i);

					canvas.drawOval(new RectF(px - size1, py - size1, px
							+ size1, py + size1), p);

					canvas.drawText("id: " + motionEvent.getPointerId(i), px,
							py - 100, fontp);

					// motionEvent

				}

				boolean touched = true;

				String astring;
				switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					astring = "ACTION DOWN";
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					astring = "ACTION POINTER DOWN";
					break;
				case MotionEvent.ACTION_POINTER_UP:
					astring = "ACTION POINTER UP";
					break;
				case MotionEvent.ACTION_UP:
					astring = "ACTION UP";
					touched = false;
					motionEvent = null;
					sendMessage("xxx");
					break;
				case MotionEvent.ACTION_MOVE:
					astring = "ACTION MOVE";
					break;
				default:
					astring = "" + motionEvent.getAction();
				}

				if (touched) {
					// p.setColor(0xffAC7423);
                    int[] xy0 =  new int[2];
                    getLocationOnScreen(xy0);
					float px = motionEvent.getX() - xy0[0];
					float py = motionEvent.getY() - xy0[1];

					p.setColor(0xffaaff22);
					canvas.drawOval(new RectF(px - size1, py - size1, px
							+ size1, py + size1), p);

					turnr = (px - w / 2f) / size2;
					speed = -(py - h / 2f) / size2;

					canvas.drawLine(w / 2f, h / 2f, w / 2f + size2 * turnr, h
							/ 2f - size2 * speed, p);

					canvas.drawText(astring, 100, 100, fontp);

					sendSpeedCommandSpeedTurn(speed, turnr);

				}



			}


            // joystick driving
            {
                sendSpeedCommandLeftRight(leftJSV.setX/100f, rightJSV.setX/100f);
            }

			// and make sure to redraw asap
			invalidate();
		}

		MotionEvent motionEvent;

		// int pointerx;
		// int pointery;

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			Log.e("touch", "event");
			this.motionEvent = event;
			return true; // true if consumed event
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	}

}