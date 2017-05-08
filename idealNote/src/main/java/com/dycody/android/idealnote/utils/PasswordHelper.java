/*
 * Copyright (C) 2016 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundatibehaon, either version 3 of the License, or
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

package com.dycody.android.idealnote.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dycody.android.idealnote.IdealNote;
import com.dycody.android.idealnote.async.bus.PasswordRemovedEvent;
import com.dycody.android.idealnote.models.PasswordValidator;

import de.greenrobot.event.EventBus;

import com.dycody.android.idealnote.db.DbHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class PasswordHelper {


	public static void requestPassword(final Activity mActivity, final PasswordValidator mPasswordValidator) {
		LayoutInflater inflater = mActivity.getLayoutInflater();
		final View v = inflater.inflate(com.dycody.android.idealnote.R.layout.password_request_dialog_layout, null);
		final EditText passwordEditText = (EditText) v.findViewById(com.dycody.android.idealnote.R.id.password_request);

		MaterialDialog dialog = new MaterialDialog.Builder(mActivity)
				.autoDismiss(false)
				.title(com.dycody.android.idealnote.R.string.insert_security_password)
				.customView(v, false)
				.positiveText(com.dycody.android.idealnote.R.string.ok)
				.onPositive((dialog12, which) -> {
					// When positive button is pressed password correctness is checked
					String oldPassword = mActivity.getSharedPreferences(Constants.PREFS_NAME, Context
							.MODE_MULTI_PROCESS)
							.getString(Constants.PREF_PASSWORD, "");
					String password = passwordEditText.getText().toString();
					// The check is done on password's hash stored in preferences
					boolean result = Security.md5(password).equals(oldPassword);

					// In case password is ok dialog is dismissed and result sent to callback
					if (result) {
						KeyboardUtils.hideKeyboard(passwordEditText);
						dialog12.dismiss();
						mPasswordValidator.onPasswordValidated(true);
						// If password is wrong the auth flow is not interrupted and simply a message is shown
					} else {
						passwordEditText.setError(mActivity.getString(com.dycody.android.idealnote.R.string.wrong_password));
					}
				})
				.neutralText(mActivity.getResources().getString(com.dycody.android.idealnote.R.string.password_forgot))
				.onNeutral((dialog13, which) -> {
					PasswordHelper.resetPassword(mActivity);
					dialog13.dismiss();
				})
				.build();

		dialog.setOnCancelListener(dialog1 -> {
			KeyboardUtils.hideKeyboard(passwordEditText);
			dialog1.dismiss();
			mPasswordValidator.onPasswordValidated(false);
		});

		dialog.show();

		// Force focus and shows soft keyboard
		new Handler().postDelayed(() -> KeyboardUtils.showKeyboard(passwordEditText), 100);
	}


	public static void resetPassword(final Activity mActivity) {
		View layout = mActivity.getLayoutInflater().inflate(com.dycody.android.idealnote.R.layout.password_reset_dialog_layout, null);
		final EditText answerEditText = (EditText) layout.findViewById(com.dycody.android.idealnote.R.id.reset_password_answer);

		new MaterialDialog.Builder(mActivity)
				.title(IdealNote.getSharedPreferences().getString(Constants.PREF_PASSWORD_QUESTION, ""))
				.customView(layout, false)
				.autoDismiss(false)
				.contentColorRes(com.dycody.android.idealnote.R.color.text_color)
				.positiveText(com.dycody.android.idealnote.R.string.ok)
				.onPositive((dialog, which) -> {
					// When positive button is pressed answer correctness is checked
					String oldAnswer = IdealNote.getSharedPreferences().getString(Constants.PREF_PASSWORD_ANSWER, "");
					String answer1 = answerEditText.getText().toString();
					// The check is done on password's hash stored in preferences
					boolean result = Security.md5(answer1).equals(oldAnswer);
					if (result) {
						dialog.dismiss();
						removePassword();
					} else {
						answerEditText.setError(mActivity.getString(com.dycody.android.idealnote.R.string.wrong_answer));
					}
				}).build().show();
	}


	public static void removePassword() {
		Observable
				.from(DbHelper.getInstance().getNotesWithLock(true))
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnNext(note -> {
					note.setLocked(false);
					DbHelper.getInstance().updateNote(note, false);
				})
				.doOnCompleted(() -> {
					EventBus.getDefault().post(new PasswordRemovedEvent());
					IdealNote.getSharedPreferences().edit()
							.remove(Constants.PREF_PASSWORD)
							.remove(Constants.PREF_PASSWORD_QUESTION)
							.remove(Constants.PREF_PASSWORD_ANSWER)
							.remove("settings_password_access")
							.apply();
				})
				.subscribe();
	}
}
