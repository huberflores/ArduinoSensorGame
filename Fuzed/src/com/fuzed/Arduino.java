package com.fuzed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import android.widget.Toast;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

public class Arduino extends Plugin {

	// Debugging
    private static final String TAG = "BcastChat";
    private static final boolean D = true;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_READ 	= 1;
    public static final int MESSAGE_WRITE 	= 2;
    public static final int MESSAGE_TOAST 	= 3;
    // Key names received from the BroadcastChatService Handler
    public static final String TOAST = "toast";

    // Member object for the chat services
    private static BroadcastChatService mChatService = null;
    static int lastReading = 100;
	
    private static Handler mHandler = new Handler() {
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
					Arduino.lastReading = t.getReading();
					Log.d(TAG, "MESSAGEREAD "+readBuf);
					break;
			}
		}
	};
	/**
	 * Initializes the chat service
	 * 
	 */
	public static void start(Context context) {
		ThreadPolicy tp = ThreadPolicy.LAX;
		StrictMode.setThreadPolicy(tp);
		Arduino.mChatService = new BroadcastChatService(context, mHandler);
		Arduino.mChatService.start();
	}
	
	/**
	 * Stops chat service
	 */
	public static void stop() {
		Arduino.mChatService.stop();
	}
	
	@Override
	public PluginResult execute(String action, JSONArray data, String callbackId) {
		PluginResult result = null;
		JSONObject response = new JSONObject();
		try {
			response.put("action", action);
		
			if (action.equals("getTemperature")) {
				response.put("value", this.getTemperature());
				result = new PluginResult(Status.OK, response);
			}
		}
		catch(JSONException jsonEx) {
			Log.d("ArduinoPlugin", "Got JSON Exception "+ jsonEx.getMessage());
			result = new PluginResult(Status.JSON_EXCEPTION);
		}
		return result;
	}
	
	private int getTemperature() {
		return Arduino.lastReading;
	}
}
