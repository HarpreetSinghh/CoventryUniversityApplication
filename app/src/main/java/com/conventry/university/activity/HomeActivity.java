package com.conventry.university.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.conventry.university.MyApplication;
import com.conventry.university.R;
import com.conventry.university.beans.Category;
import com.conventry.university.database.DatabaseManager;
import com.conventry.university.service.NotificationReciever;
import com.conventry.university.utils.AppConstants;
import com.conventry.university.utils.AppUtils;
import com.conventry.university.utils.CustomAlertDialog;
import com.conventry.university.utils.SharePrefernceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private GridView gLView;

    private CategoryAdapter adapter;

    private AppCompatActivity activity;

    public final int PERMISSION_ACCESS_COARSE_LOCATION = 1111;
    public final int PERMISSION_READ_WRITE = 2222;
    private Object permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        AppUtils.getUserLocationFromGps();
    }

    private void init() {
        activity  = this;
        gLView = (GridView) findViewById(R.id.gL_category);
        setAdapter();

        findViewById(R.id.btn_notifications).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1);
                Intent intent =  new Intent("action.DISPLAY_NOTIFICATION");
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getAppContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
            }
        });
    }

    private void setAdapter() {
        adapter  = new CategoryAdapter();
        gLView.setAdapter(adapter);
        gLView.setSmoothScrollbarEnabled(true);
        adapter.notifyDataSetChanged();
    }



    class CategoryAdapter extends BaseAdapter {

        private List<Category> categoryList = new ArrayList<>();

        public CategoryAdapter() {
            categoryList = DatabaseManager.getInstance().getCategoriesList();
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View row, ViewGroup parent) {
            CategoryHolder holder = null;
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(row == null) {
                row = inflater.inflate(R.layout.category_grid_item_layout, parent, false);
                holder = new CategoryHolder();
                holder.catName = (TextView) row.findViewById(R.id.tv_catName);
                holder.catImage = (ImageView) row.findViewById(R.id.cat_img);
                holder.outerLayout = (LinearLayout) row.findViewById(R.id.outer_layout);
                row.setTag(holder);
            } else {
                holder = (CategoryHolder) row.getTag();
            }

            Category category = categoryList.get(position);
            if(category != null) {
                if(!AppUtils.isBlank(category.getIcon())) {
                    setImageView((int)category.getCategoryId(), holder.catImage);
                }

                if(!AppUtils.isBlank(category.getName()))
                    holder.catName.setText(category.getName().toUpperCase());

                if(category.getCategoryId() == 3001 && SharePrefernceUtil.getIsLoggedIn())
                    holder.catName.setText("LOGOUT");

                holder.outerLayout.setTag(category);

                holder.outerLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Category vo = (Category) view.getTag();
                        handleClick((int)vo.getCategoryId());
                    }
                });
            }
            return row;
        }
    }
    class CategoryHolder {

        private LinearLayout outerLayout;

        private ImageView catImage;

        private TextView catName;
    }

    private void setImageView(int catId, ImageView imageView) {
        switch (catId) {

            case 1001:
                imageView.setImageResource(R.mipmap.emaillogin);
                break;

            case 2001:
                imageView.setImageResource(R.mipmap.modules);
                break;

            case 3001:
                if(SharePrefernceUtil.getIsLoggedIn())
                    imageView.setImageResource(R.mipmap.cat_logout);
                else
                    imageView.setImageResource(R.mipmap.login);
                break;

            case 4001:
                imageView.setImageResource(R.mipmap.weather);
                break;

            case 5001:
                imageView.setImageResource(R.mipmap.search);
                break;

            case 6001:
                imageView.setImageResource(R.mipmap.news);
                break;

            case 7001:
                imageView.setImageResource(R.mipmap.campusmap);
                break;

            case 8001:
                imageView.setImageResource(R.mipmap.buses);
                break;

            case 9001:
                imageView.setImageResource(R.mipmap.timetable);
                break;
        }
    }


    private void handleClick(int catId) {
        switch (catId) {
            case 1001:
                if(!SharePrefernceUtil.getIsEmailLoggedIn())
                    initiateEmailPopupWindow();
                else
                    navigateActivity(EmailActivity.class);
                break;

            case 2001:
                if(loginRequired())
                    navigateActivity(ModuleActivity.class);
                break;

            case 3001:
                if(!SharePrefernceUtil.getIsLoggedIn())
                    initiateLoginPopupWindow();
                else
                    AppUtils.showLogoutMessage(this, gLView);
                break;

            case 4001:
                checkAccess(WeatherActivity.class);
                break;

            case 5001:
                navigateActivity(SearchActivity.class);
                break;

            case 6001:
                navigateActivity(NewsActivity.class);
                break;

            case 7001:
                navigateActivity(CampusMapActivity.class);
                break;

            case 8001:
                checkAccess(BusActivity.class);
                break;

            case 9001:
                if(loginRequired())
                    navigateActivity(TimetableActivity.class);
                break;

        }
    }

    private void navigateActivity(Class aClass){
        Intent intent  = new Intent(this, aClass);
        startActivity(intent);
    }

    private void initiateLoginPopupWindow() {
        final CustomAlertDialog alertDialog = new CustomAlertDialog(this, "LOGIN", "LOGIN", "USERNAME", "PASSWORD");
        alertDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                usernameLogin(alertDialog.getUsernameET(), alertDialog.getPasswordET(), alertDialog);
            }
        });
        alertDialog.showLoginDialogView();
    }

    private void usernameLogin(EditText username, EditText password, CustomAlertDialog alertDialog) {
        if(AppUtils.isBlank(username.getText().toString())) {
            AppUtils.showToastMessage("Please enter username.");
        }
        else if(AppUtils.isBlank(password.getText().toString())) {
            AppUtils.showToastMessage("Please enter correct password.");
        } else {
            SharePrefernceUtil.setUsername(username.getText().toString());
            SharePrefernceUtil.setPassword(password.getText().toString());
            SharePrefernceUtil.setIsLoggedIn(true);
            alertDialog.closeDialog();
            gLView.invalidateViews();
        }

    }

    private void initiateEmailPopupWindow() {
        final CustomAlertDialog alertDialog = new CustomAlertDialog(this, "EMAIL", "LOGIN", "EMAIL", "PASSWORD");
        alertDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emailLogin(alertDialog.getUsernameET(), alertDialog.getPasswordET(), alertDialog);
            }
        });
        alertDialog.showLoginDialogView();
    }

    private void emailLogin(EditText email, EditText password, CustomAlertDialog alertDialog) {
        if(!AppUtils.validEmail(email.getText().toString())) {
            AppUtils.showToastMessage("Please enter correct email.");
        }
        else if(!AppUtils.validPassword(password.getText().toString())) {
            AppUtils.showToastMessage("Please enter correct password.");
        } else {
            SharePrefernceUtil.setIsEmailLoggedIn(true);
            navigateActivity(EmailActivity.class);
            alertDialog.closeDialog();
        }
    }

    private void checkAccess(Class aClass){
        if(AppUtils.isConnected(AppConstants.INTERNET_ERROR_MESSAGE)) {
            if(checkPermission()) {
                if(AppUtils.getUserLocationFromGps() == null)
                    AppUtils.showToastMessage("Location could not be determined.");
                else
                    navigateActivity(aClass);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ACCESS_COARSE_LOCATION);
            }
        }
    }

    private boolean checkPermission(){
        return ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    navigateActivity(WeatherActivity.class);
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private  boolean loginRequired(){
        if(!SharePrefernceUtil.getIsLoggedIn()) {
            AppUtils.showToastMessage("Login is required to view the content!");
            initiateLoginPopupWindow();
        }
        return  SharePrefernceUtil.getIsLoggedIn();
    }
}
