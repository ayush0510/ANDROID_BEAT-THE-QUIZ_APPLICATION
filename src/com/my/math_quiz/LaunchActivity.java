package com.my.math_quiz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LaunchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);

		((ImageButton) findViewById(R.id.ALsettingsButton))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LaunchActivity.this,
								PreferenceActivity.class);
						startActivity(intent);

					}
				});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d("conCh", "configng changfe");
		// super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.launch, menu);
		return true;
	}

	public void launcherActivityButtonClick(View v) {
		if (v.getId() == R.id.ALmultiPLayer) {
			Intent intent = new Intent(this, MultiPlayerActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.ALsinglePLayer) {
			Intent intent = new Intent(this, LevelsDisplayedActivity.class);
			intent.putExtra(LevelsDisplayedActivity.KEY_FOR_MODE_PARAMETER,
					LevelsDisplayedActivity.MODE_BEFORE_SINGLE_PLAYER_GAME);
			startActivity(intent);

		} 

	}

}
