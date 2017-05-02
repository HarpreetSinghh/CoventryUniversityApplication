package com.conventry.university.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.conventry.university.MyApplication;


public class SharePrefernceUtil {

	private static final String PREF_FILE_NAME = "univPref";

	public static void setIsLoggedIn(Boolean status) {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putBoolean("is_loggedIn", status);
		editor.commit();
	}

	public static boolean getIsLoggedIn() {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0);
		return pref.getBoolean("is_loggedIn", false);
	}

	public static void setIsEmailLoggedIn(Boolean status) {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putBoolean("is_email_loggedIn", status);
		editor.commit();
	}

	public static boolean getIsEmailLoggedIn() {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0);
		return pref.getBoolean("is_email_loggedIn", false);
	}

	public static void setUsername(String username) {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putString("username", username);
		editor.commit();
	}

	public static String getUsername() {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0);
		return pref.getString("username", AppConstants.BLANK);
	}

	public static void setPassword(String username) {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0); // 0 - for private mode
		Editor editor = pref.edit();
		editor.putString("password", username);
		editor.commit();
	}

	public static String getPassword() {
		SharedPreferences pref = MyApplication.getAppContext().getSharedPreferences(PREF_FILE_NAME, 0);
		return pref.getString("password", AppConstants.BLANK);
	}
}
