package com.dycody.android.idealnote.async.bus;

import android.util.Log;

import com.dycody.android.idealnote.utils.Constants;


public class NotesMergeEvent {

	public final boolean keepMergedNotes;


	public NotesMergeEvent(boolean keepMergedNotes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.keepMergedNotes = keepMergedNotes;
	}
}
