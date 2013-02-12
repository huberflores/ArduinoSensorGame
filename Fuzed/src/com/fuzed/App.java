package com.fuzed;


import com.phonegap.*;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.os.Bundle;

public class App extends DroidGap {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
    	KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
    	lock.disableKeyguard();
    	
	    super.onCreate(savedInstanceState);
	    super.setBooleanProperty("loadInWebView", true);
	    super.clearCache(); 
	    super.loadUrl("http://macrosimgames.com/fuzed");
    }
    
    public void onStart() {
    	super.onStart();
    	Arduino.start(this);
    }
	
	public void onStop() {
		super.onStop();
		Arduino.stop();
	}
	
	public void onDestroy() {
		super.onDestroy();
		Arduino.stop();
	}
}