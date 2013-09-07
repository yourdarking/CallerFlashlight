package com.spirosbond.callerflashlight;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by spiros on 8/5/13.
 */
public class PrefsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = PrefsActivity.class.getSimpleName();
	ListPreference lp;
	CallerFlashlight callerFlashlight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		callerFlashlight = (CallerFlashlight) this.getApplication();
		addPreferencesFromResource(R.xml.prefs);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) getActionBar().setDisplayHomeAsUpEnabled(true);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

//		lp = (ListPreference) findPreference("type_list");
		setTypeSum();


	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
		Log.d(TAG, "onSharedPreferenceChanged: " + s);
		if (s.equals("type")) {
//			lp = (ListPreference) findPreference("type_list");
			setTypeSum();

		}
	}

	public void setTypeSum() {

		lp = (ListPreference) findPreference("type_list");
		Log.d(TAG, "setTypeSum");
		int type = callerFlashlight.getType();
		if (type == 1) {
			Log.d(TAG, "sum type 1");
			lp.setSummary(getResources().getString(R.string.type_list_1));
		} else if (type == 2) {
			Log.d(TAG, "sum type 2");
			lp.setSummary(getResources().getString(R.string.type_list_2));
		} else if (type == 3) {
			Log.d(TAG, "sum type 3");
			lp.setSummary(getResources().getString(R.string.type_list_3));
		}


	}
}