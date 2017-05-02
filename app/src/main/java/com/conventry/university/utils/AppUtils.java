package com.conventry.university.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.Toast;

import com.conventry.university.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

	final static String EMAIL_PATTERN =
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	static final String PASSWORD_PATTERN =
			"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

	private static Pattern emailPattern;

	private static Pattern passwordPattern;

	static {
		emailPattern = Pattern.compile(EMAIL_PATTERN);
		passwordPattern = Pattern.compile(PASSWORD_PATTERN);
	}

	/**
	 * Return true if email is valid otherwise false.
	 * @param email - email text
	 * @return true if valid email otherwise false.
     */
	public static boolean validEmail(String email) {
		Matcher matcher = emailPattern.matcher(email);
		return matcher.matches();
	}

	/**
	 * Return true if password is valid otherwise false.
	 * @param password - password text
	 * @return true if valid password otherwise false.
	 */
	public static boolean validPassword(String password) {
		Matcher matcher = passwordPattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * Do log debug.
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public static void doLogDebug(String key, String value) {
		System.out.println(key + ": " + value); // difficult to get LogCat on hardware device
		Log.d(key, value);
	}

	/**
	 * Do log error.
	 *
	 * @param key   the key
	 * @param value the value
	 */
	public static void doLogError(String key, String value) {
		System.out.println(key + ": " + value); // difficult to get LogCat on hardware device
		Log.e(key, value);
	}

	/**
	 * @return boolean flag on the basis of internet connection
	 */
	public static boolean isConnected(String message) {
		ConnectivityManager connMgr = (ConnectivityManager) MyApplication.getAppContext().getSystemService(MyApplication.getAppContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			showToastMessage(AppConstants.INTERNET_ERROR_MESSAGE);
			return false;
		}
	}

	/**
	 * @param message - text need to be display as toast message
	 */
	public static void showToastMessage(String message) {
		Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param str
	 * @return boolean
	 * this utility method checks for string null or blank
	 */
	public static boolean isBlank(String str) {
		if (str == null || AppConstants.BLANK.equals(str.trim())) {
			return true;
		}
		return false;
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}


	public static int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

	public static String getDateEmailView(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yy, h:mm:a");
		try {
			Date convertDate = dateFormat.parse(date);
			String dateText = formatter.format(convertDate);
			return dateText.trim();
		} catch (ParseException e) {
			AppUtils.doLogError(AppConstants.ERROR_TAG, e.getLocalizedMessage());
		}
		return AppConstants.BLANK;
	}

	public static String[] getEmails(String text) {
		if(isBlank(text))
			return null;
		else
			return text.split(",");
	}

	public static String getFilenameWithoutExtension(String fileName){
		if(isBlank(fileName))
			return AppConstants.BLANK;
		else
			return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static String getTempSign(int unitType){
		switch (unitType) {
			case 17:
				return "\u2103";

			case 18:
				return "\u2109";

			default:
				return  "";
		}
	}

	public static Date getDateFormat(String dateText){
		Date date = new Date();
		dateText = dateText.substring(0, 11);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = dateFormat.parse(dateText);
		}catch(Exception e) {
			AppUtils.doLogError(AppConstants.ERROR_TAG, e.getLocalizedMessage());
		}
		return date;
	}

	public static String getWeekDay(Date date) {
		String[] days =
				{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		if(date == null)
			return "";
		Calendar c = Calendar.getInstance();

		c.setTime(date);
		return days[c.get(Calendar.DAY_OF_WEEK)-1];
	}

	public static Location getUserLocationFromGps() {
		Location location = null;
		if (ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			LocationManager locationManager = (LocationManager) MyApplication.getAppContext().getSystemService(Context.LOCATION_SERVICE);
			Boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			Log.d("msg","GPS:" + isGpsEnabled);
			if(isGpsEnabled) {
				Criteria criteria = new Criteria();
				String provider = locationManager.getBestProvider(criteria, false);
				location = locationManager.getLastKnownLocation(provider);
			}
			if(!isGpsEnabled ||  location == null){
				LocationManager lm = (LocationManager) MyApplication.getAppContext().getSystemService(
						Context.LOCATION_SERVICE);
				List<String> providers = lm.getProviders(true);
				for (int i=providers.size()-1; i>=0; i--) {
					location = lm.getLastKnownLocation(providers.get(i));
					if (location != null) return location;
				}
			}
		}
		return location;
	}

	public static void showLogoutMessage(Activity activity, final GridView gridView){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("Are you sure you want to logout?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						SharePrefernceUtil.setIsLoggedIn(false);
						gridView.invalidateViews();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
