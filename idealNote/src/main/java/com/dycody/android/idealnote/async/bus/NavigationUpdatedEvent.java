package com.dycody.android.idealnote.async.bus;

import android.util.Log;
import com.dycody.android.idealnote.utils.Constants;


/**
 * Created by fede on 18/04/15.
 */
public class NavigationUpdatedEvent {

	public final Object navigationItem;


	public NavigationUpdatedEvent(Object navigationItem) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.navigationItem = navigationItem;
	}
}
