package com.conventry.university.utils;


import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.conventry.university.R;

/**
 * 
 */
public class CustomAlertDialog {

	private Activity activity;

	private String title;

	private String message;

	private OnClickListener btn1OnClickListener;

	private OnClickListener btn2OnClickListener;

	private Dialog lDialog;

	private String btn1Text;

	private EditText usernameET;

	private EditText passwordET;

	private ImageButton cancelDialog;

	private String text1;

	private String text2;

	public Dialog getlDialog() {
		return lDialog;
	}


	/**
	 *
	 * @param context - Application context
	 * @param title - title
	 * @param btn1Text - button 1 text
	 * @param text1 -  text 1
     * @param text2 = text 2
     */
	public CustomAlertDialog(Activity context, String title, String btn1Text, String text1, String text2) {
		this.activity = context;
		this.title = title;
		this.btn1Text = btn1Text;
		this.text1 = text1;
		this.text2 = text2;
	}

	public CustomAlertDialog(Activity activity) {
		this.activity = activity;
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.btn1OnClickListener = onClickListener;
	}

	public void setOnClickListener2(OnClickListener onClickListener) {
		this.btn2OnClickListener = onClickListener;
	}

	public void showProgressDialog() {
		lDialog = new Dialog(activity, R.style.popup_theme);
		lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		lDialog.setContentView(R.layout.custom_progress_bar);
		lDialog.show();
	}

	public void showLoginDialogView() {
		lDialog = new Dialog(activity, R.style.popup_theme);
		lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		lDialog.setContentView(R.layout.login_view);
		((TextView)lDialog.findViewById(R.id.txt_header_popup)).setText(title);
		usernameET = ((EditText)lDialog.findViewById(R.id.txt_email));
		usernameET.setHint(text1);
		passwordET = (EditText) lDialog.findViewById(R.id.txt_password);
		passwordET.setHint(text2);
		cancelDialog = (ImageButton) lDialog.findViewById(R.id.cancel_dialog);

		cancelDialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				closeDialog();
			}
		});
		Button btn1 = (Button) lDialog.findViewById(R.id.btn_first);
		btn1.setText(btn1Text);
		btn1.setOnClickListener(btn1OnClickListener);
		lDialog.show();
	}

	public void closeDialog(){
		if(lDialog != null && lDialog.isShowing()) {
			lDialog.dismiss();
		}
	}

	public EditText getUsernameET() {
		return usernameET;
	}

	public EditText getPasswordET() {
		return passwordET;
	}
}
