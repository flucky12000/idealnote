package com.dycody.android.idealnote.intro;

import android.graphics.Color;
import android.os.Bundle;


public class IntroSlide4 extends IntroFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		background.setBackgroundColor(Color.parseColor("#2196f3"));
		title.setText(com.dycody.android.idealnote.R.string.tour_detailactivity_attachment_title);
		image.setImageResource(com.dycody.android.idealnote.R.drawable.slide4);
		description.setText(com.dycody.android.idealnote.R.string.tour_detailactivity_attachment_detail);
	}
}