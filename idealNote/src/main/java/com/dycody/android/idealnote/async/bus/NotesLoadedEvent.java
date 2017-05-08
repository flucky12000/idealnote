package com.dycody.android.idealnote.async.bus;

import android.util.Log;
import com.dycody.android.idealnote.models.Note;
import com.dycody.android.idealnote.utils.Constants;

import java.util.ArrayList;


public class NotesLoadedEvent {

	public ArrayList<Note> notes;


	public NotesLoadedEvent(ArrayList<Note> notes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.notes = notes;
	}
}
