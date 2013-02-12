package siim.puniste.com;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;



import android.app.Activity;
import android.bluetooth.BluetoothClass.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

 
public class ThermistorActivity extends Activity{
 
	private TextView mResponseField;
	
	 // Debugging
    private static final String TAG = "BcastChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_READ 	= 1;
    public static final int MESSAGE_WRITE 	= 2;
    public static final int MESSAGE_TOAST 	= 3;

    // Key names received from the BroadcastChatService Handler
    public static final String TOAST = "toast";

    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Member object for the chat services
    private BroadcastChatService mChatService = null;
    
   

 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ThreadPolicy tp = ThreadPolicy.LAX;
		StrictMode.setThreadPolicy(tp);
	}
 
	 public void onStart() {
	        super.onStart();
	        if(D) Log.e(TAG, "++ ON START ++");

	        setupThis();
	    }

 
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
					
	            	
             
	            	switch (msg.what) {

	    			case MESSAGE_WRITE:

	    				byte[] writeBuf = (byte[]) msg.obj;
	    				// construct a string from the buffer
	    				String writeMessage = new String(writeBuf);
	    				// ///mConversationArrayAdapter.add("Me:  " + writeMessage);
	    				break;
	    			case MESSAGE_READ:
	    				int readBuf = Integer.valueOf((String) msg.obj);
		            	ValueMsg t = new ValueMsg(readBuf);
	    				mResponseField.setText("Temperature: "+readBuf+"C");
	    				// ////mConversationArrayAdapter.add("You:  " + readBuf);
	    				break;
	    			case MESSAGE_TOAST:
	    				Toast.makeText(getApplicationContext(),
	    						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
	    						.show();
	    				break;
	    			}
		}
	};
 
	
	 private void setupThis() {
	        
		 mResponseField = (TextView)findViewById(R.id.arduinoresponse);
	        // Initialize the BluetoothChatService to perform bluetooth connections
	     mChatService = new BroadcastChatService(this, mHandler);
	        
	    }

	@Override
	public synchronized void onResume() {
		super.onResume();
		Log.e(TAG, "+ ON RESUME +");

		mChatService.start();
	}

	public synchronized void onPause() {
		super.onPause();
		Log.e(TAG, "- ON PAUSE -");
	}

	public void onStop() {
		super.onStop();
		if (mChatService != null)
			mChatService.stop();
		Log.e(TAG, "-- ON STOP --");
	}

	public void onDestroy() {
		super.onDestroy();
		// Stop the Broadcast chat services
		if (mChatService != null)
			mChatService.stop();
		Log.e(TAG, "--- ON DESTROY ---");
	}


	public void onStartTrackingTouch(SeekBar arg0) {
	} // onStartTrackingTouch


	public void onStopTrackingTouch(SeekBar arg0) {
	} // onStopTrackingTouch



}