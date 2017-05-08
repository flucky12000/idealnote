package com.dycody.android.idealnote.async.bus;

import android.util.Log;
import com.dycody.android.idealnote.utils.Constants;


public class NavigationUpdatedNavDrawerClosedEvent {

	public final Object navigationItem;


	public NavigationUpdatedNavDrawerClosedEvent(Object navigationItem) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.navigationItem = navigationItem;
	}
}
