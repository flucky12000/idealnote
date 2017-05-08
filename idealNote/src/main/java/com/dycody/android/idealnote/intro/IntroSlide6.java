package com.dycody.android.idealnote.intro;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


public class IntroSlide6 extends IntroFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		background.setBackgroundColor(Color.parseColor("#222222"));
		title.setText(com.dycody.android.idealnote.R.string.tour_listactivity_final_title);
		image.setVisibility(View.GONE);
		image_small.setImageResource(com.dycody.android.idealnote.R.drawable.gplus);
		image_small.setVisibility(View.VISIBLE);
		image_small.setOnClickListener(v -> {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(getString(com.dycody.android.idealnote.R.string.gplus_community)));
			startActivity(intent);
		});
		description.setText(com.dycody.android.idealnote.R.string.tour_community);
	}
}