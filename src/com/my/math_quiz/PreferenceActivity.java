
package com.my.math_quiz;

import com.my.math_quiz.views.PreferenceFragmentm;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class PreferenceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenceFragmentm())
                .commit();
    }
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void finish() {
		ApplicationClass.settingsChanged();
		super.finish();
	}

    
}
