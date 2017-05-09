package com.dycody.android.idealnote;

import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {


	@Override
	public void onStart() {
		super.onStart();
		((IdealNote)getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		RefWatcher refWatcher = IdealNote.getRefWatcher();
		refWatcher.watch(this);
	}

}
