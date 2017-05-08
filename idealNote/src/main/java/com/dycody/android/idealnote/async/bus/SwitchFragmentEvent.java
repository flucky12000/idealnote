package com.dycody.android.idealnote.async.bus;

import android.util.Log;
import com.dycody.android.idealnote.utils.Constants;


public class SwitchFragmentEvent {

	public enum Direction {
		CHILDREN, PARENT
	}


	public Direction direction;


	public SwitchFragmentEvent(Direction direction) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.direction = direction;
	}
}
