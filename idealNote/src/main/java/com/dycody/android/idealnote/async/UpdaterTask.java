/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dycody.android.idealnote.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dycody.android.idealnote.BuildConfig;
import com.dycody.android.idealnote.IdealNote;
import com.dycody.android.idealnote.utils.ConnectionManager;
import com.dycody.android.idealnote.utils.Constants;
import com.dycody.android.idealnote.utils.SystemHelper;

import it.feio.android.analitica.AnalyticsHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;



public class UpdaterTask extends AsyncTask<String, Void, Void> {

	private static final String BETA = " Beta ";
	private final WeakReference<Activity> mActivityReference;
	private final Activity mActivity;
	private final SharedPreferences prefs;
	private boolean promptUpdate = false;
	private long now;


	public UpdaterTask(Activity mActivity) {
		this.mActivityReference = new WeakReference<>(mActivity);
		this.mActivity = mActivity;
		this.prefs = mActivity.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS);
	}


	@Override
	protected void onPreExecute() {
		now = System.currentTimeMillis();
		if (IdealNote.isDebugBuild() || !ConnectionManager.internetAvailable(IdealNote.getAppContext()) || now < prefs.getLong(Constants
				.PREF_LAST_UPDATE_CHECK, 0) + Constants.UPDATE_MIN_FREQUENCY) {
			cancel(true);
		}
		super.onPreExecute();
	}


	@Override
	protected Void doInBackground(String... params) {
		if (!isCancelled()) {
			try {
				promptUpdate = isVersionUpdated(getAppData());
				if (promptUpdate) {
					prefs.edit().putLong(Constants.PREF_LAST_UPDATE_CHECK, now).apply();
				}
			} catch (Exception e) {
				Log.w(Constants.TAG, "Error fetching app metadata", e);
			}
		}
		return null;
	}


	private void promptUpdate() {
		new MaterialDialog.Builder(mActivityReference.get())
				.title(com.dycody.android.idealnote.R.string.app_name)
				.content(com.dycody.android.idealnote.R.string.new_update_available)
				.positiveText(com.dycody.android.idealnote.R.string.update)
				.negativeText(com.dycody.android.idealnote.R.string.not_now)
				.negativeColorRes(com.dycody.android.idealnote.R.color.colorPrimary)
				.callback(new MaterialDialog.ButtonCallback() {
					@Override
					public void onPositive(MaterialDialog materialDialog) {
						if (isGooglePlayAvailable()) {
							((IdealNote)mActivity.getApplication()).getAnalyticsHelper().trackEvent(AnalyticsHelper.CATEGORIES.UPDATE, "Play Store");
							mActivityReference.get().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
									("market://details?id=" + mActivity.getPackageName())));
						} else {
							((IdealNote)mActivity.getApplication()).getAnalyticsHelper().trackEvent(AnalyticsHelper.CATEGORIES.UPDATE, "Drive Repository");
							mActivityReference.get().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants
									.DRIVE_FOLDER_LAST_BUILD)));
						}
					}
				}).build().show();
	}


	@Override
	protected void onPostExecute(Void result) {
		if (isAlive(mActivityReference)) {
			if (promptUpdate) {
				promptUpdate();
			} else {
				showChangelog();
			}
		}
	}


	private void showChangelog() {
		try {
			String newVersion = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0)
					.versionName;
			String currentVersion = mActivity.getSharedPreferences(Constants.PREFS_NAME,
					Context.MODE_MULTI_PROCESS).getString(Constants.PREF_CURRENT_APP_VERSION, "");
			if (!newVersion.equals(currentVersion)) {
				new MaterialDialog.Builder(mActivity)
						.customView(com.dycody.android.idealnote.R.layout.activity_changelog, false)
						.positiveText(com.dycody.android.idealnote.R.string.ok)
						.build().show();
				mActivity.getSharedPreferences(Constants.PREFS_NAME,
						Context.MODE_MULTI_PROCESS).edit().putString(Constants.PREF_CURRENT_APP_VERSION,
						newVersion).apply();
			}
		} catch (NameNotFoundException e) {
			Log.e(Constants.TAG, "Error retrieving app version", e);
		}
	}


	private boolean isAlive(WeakReference<Activity> weakActivityReference) {
		return !(weakActivityReference.get() == null || weakActivityReference.get().isFinishing());
	}


	/**
	 * Fetches application data from internet
	 */
	private String getAppData() throws IOException {
		InputStream is = null;
		InputStreamReader inputStreamReader = null;
		try {
			StringBuilder sb = new StringBuilder();
			URLConnection conn = new URL(BuildConfig.VERSION_CHECK_URL).openConnection();
			is = conn.getInputStream();
			inputStreamReader = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(inputStreamReader);

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}

			return sb.toString();
		} finally {
			SystemHelper.closeCloseable(inputStreamReader, is);
		}
	}


	/**
	 * Checks parsing "android:versionName" if app has been updated
	 *
	 * @throws NameNotFoundException
	 */
	private boolean isVersionUpdated(String playStoreVersion)
			throws NameNotFoundException {

		boolean result = false;

		// Retrieval of installed app version
		PackageInfo pInfo = mActivity.getPackageManager().getPackageInfo(
				mActivity.getPackageName(), 0);
		String installedVersion = pInfo.versionName;

		// Parsing version string to obtain major.minor.point (excluding eventually beta)
		String[] playStoreVersionArray = playStoreVersion.split(BETA)[0].split("\\.");
		String[] installedVersionArray = installedVersion.split(BETA)[0].split("\\.");

		// Versions strings are converted into integer
		String playStoreVersionString = playStoreVersionArray[0];
		String installedVersionString = installedVersionArray[0];
		for (int i = 1; i < playStoreVersionArray.length; i++) {
			playStoreVersionString += String.format("%02d", Integer.parseInt(playStoreVersionArray[i]));
			installedVersionString += String.format("%02d", Integer.parseInt(installedVersionArray[i]));
		}

		// And then compared
		if (Integer.parseInt(playStoreVersionString) > Integer.parseInt(installedVersionString)) {
			result = true;
		}

		// And then compared again to check if we're out of Beta
		else if (Integer.parseInt(playStoreVersionString) == Integer.parseInt(installedVersionString)
				&& playStoreVersion.split("b").length == 1 && installedVersion.split("b").length == 2) {
			result = true;
		}

		return result;
	}


	private boolean isGooglePlayAvailable() {
		try {
			mActivity.getPackageManager().getPackageInfo("com.android.vending", 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
