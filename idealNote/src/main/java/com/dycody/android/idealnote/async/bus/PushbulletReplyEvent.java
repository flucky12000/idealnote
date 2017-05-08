package com.dycody.android.idealnote.async.bus;

import android.util.Log;
import com.dycody.android.idealnote.utils.Constants;


public class PushbulletReplyEvent {

	public String message;

	public PushbulletReplyEvent(String message) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.message = message;
	}
}
