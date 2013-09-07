package com.spirosbond.callerflashlight;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by spiros on 8/23/13.
 */
public class NotificationService extends AccessibilityService {


	public static final String TAG = NotificationService.class.getSimpleName();
	CallerFlashlight callerFlashlight;

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		Log.d(TAG, "Got event from: " + String.valueOf(event.getPackageName()));
//		Toast.makeText(getApplicationContext(), "Got event from: " + event.getPackageName(), Toast.LENGTH_LONG).show();

		if (callerFlashlight.isMsgFlash() && callerFlashlight.isEnabled() && callerFlashlight.loadApp(String.valueOf(event.getPackageName()))) {
			new ManageFlash().execute(callerFlashlight.getMsgFlashOnDuration(), callerFlashlight.getMsgFlashOffDuration(),
							callerFlashlight.getMsgFlashDuration());
		}
	}

	@Override
	public void onInterrupt() {
		Log.d(TAG, "***** onInterrupt");
		callerFlashlight.setServiceRunning(false);
	}

	@Override
	public void onServiceConnected() {
		Log.d(TAG, "***** onServiceConnected");

		callerFlashlight = (CallerFlashlight) getApplication();
		callerFlashlight.setServiceRunning(true);

		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		info.notificationTimeout = 100;
		info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
		setServiceInfo(info);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "***** onDestroyed");
		callerFlashlight.setServiceRunning(false);
	}

	public class ManageFlash extends AsyncTask<Integer, Integer, String> {


		private Flash flash = new Flash(callerFlashlight);

		public ManageFlash() {
			Flash.incRunning();
		}

		@Override
		protected String doInBackground(Integer... integers) {
			Log.d(TAG, "doInBackgroung Started");
			long start = System.currentTimeMillis();

			if (callerFlashlight.getMsgFlashType() == 1) {
				int durMillis = integers[2] * 1000;
				while (System.currentTimeMillis() - start <= durMillis) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
				}
			} else if (callerFlashlight.getMsgFlashType() == 2) {
				int times = 0;
				int repeats = integers[2];
				while (times < repeats) {
					flash.enableFlash(Long.valueOf(integers[0]), Long.valueOf(integers[1]));
					times = times + 1;
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			Log.d(TAG, "onPostExecute Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Log.d(TAG, "onCancelled Started");
			Flash.decRunning();
			if (Flash.getRunning() == 0) Flash.releaseCam();
		}


	}
}