package com.dycody.android.idealnote.intro;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;


public class IntroSlide1 extends IntroFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		background.setBackgroundColor(Color.parseColor("#222222"));
		title.setText(com.dycody.android.idealnote.R.string.tour_listactivity_intro_title);
		image.setVisibility(View.GONE);
		image_small.setImageResource(com.dycody.android.idealnote.R.drawable.logo);
		image_small.setVisibility(View.VISIBLE);
		description.setText(com.dycody.android.idealnote.R.string.tour_listactivity_final_detail);
	}
}