package com.dycody.android.idealnote.intro;

import android.graphics.Color;
import android.os.Bundle;


public class IntroSlide3 extends IntroFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		background.setBackgroundColor(Color.parseColor("#8bc34a"));
		title.setText(com.dycody.android.idealnote.R.string.categories);
		image.setImageResource(com.dycody.android.idealnote.R.drawable.slide3);
		description.setText(com.dycody.android.idealnote.R.string.tour_listactivity_tag_detail);
	}
}