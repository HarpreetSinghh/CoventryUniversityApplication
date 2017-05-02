package com.conventry.university.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conventry.university.R;
import com.conventry.university.beans.ModulesDir;
import com.conventry.university.beans.Weather;
import com.conventry.university.service.WeatherService;
import com.conventry.university.utils.AppConstants;
import com.conventry.university.utils.AppUtils;
import com.conventry.university.utils.CustomAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private int tempUnitType;

    private  WeatherTask weatherTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        if(AppUtils.isConnected(AppConstants.INTERNET_ERROR_MESSAGE)) {
            weatherTask = new WeatherTask(this);
            weatherTask.execute();
        }

    }

    class WeatherTask extends AsyncTask<Void, Void, Void>  {

        private Weather weatherDetails = null;
        private CustomAlertDialog alertDialog = null;

        public WeatherTask(Activity activity){
            alertDialog = new CustomAlertDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            weatherDetails =  WeatherService.getLocationKey(AppUtils.getUserLocationFromGps());
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            alertDialog.closeDialog();
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.closeDialog();
            if(weatherDetails !=null) {
                updateUI(weatherDetails);
            }
        }
    }

    private void updateUI(Weather  weather) {
        TextView cityTV = (TextView) findViewById(R.id.city);
        cityTV.setText(weather.getCity());
        setAdapter(weather);
        if(weather.getCurrentJsonObject() != null) {
            TextView currentTemp = (TextView) findViewById(R.id.currentTemp);
            TextView textWeather = (TextView) findViewById(R.id.textDesc);
            TextView realFeelTemp = (TextView) findViewById(R.id.realFeelTemp);
            TextView windSpeedTV = (TextView) findViewById(R.id.windSpeed);
            try {
                JSONObject currentTempDetail = weather.getCurrentJsonObject().getJSONObject(0);
                tempUnitType = currentTempDetail.getJSONObject("Temperature").getJSONObject("Metric").getInt("UnitType");
                currentTemp.setText(currentTempDetail.getJSONObject("Temperature").getJSONObject("Metric").getString("Value") + AppUtils.getTempSign(tempUnitType));
                textWeather.setText(currentTempDetail.getString("WeatherText"));
                realFeelTemp.setText("RealFeel"+ getResources().getString(R.string.reg_symbol)+ AppConstants.SPACE+currentTempDetail.getJSONObject("RealFeelTemperature").getJSONObject("Metric").getString("Value") + AppUtils.getTempSign(tempUnitType));
                windSpeedTV.setText(currentTempDetail.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getString("Value") +
                    AppConstants.SPACE+    currentTempDetail.getJSONObject("Wind").getJSONObject("Speed").getJSONObject("Metric").getString("Unit"));
            }catch (Exception e){
                AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
            }
        }
    }

    public void setAdapter(Weather weather){
        try {
            JSONArray jsonArray = weather.getJsonObject().getJSONArray("DailyForecasts");
            JSONObject tempObject = jsonArray.getJSONObject(0).getJSONObject("Temperature");
            tempUnitType = tempObject.getJSONObject("Minimum").getInt("UnitType");
            TextView minMaxTemp = (TextView) findViewById(R.id.minMaxTemp);
            minMaxTemp.setText(
                    tempObject.getJSONObject("Minimum").get("Value") + AppUtils.getTempSign(tempUnitType) + AppConstants.SPACE
                     + tempObject.getJSONObject("Maximum").get("Value")  + AppUtils.getTempSign(tempUnitType)
            );
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.temp_list_view);
            TempAdapter mAdapter = new TempAdapter(jsonArray);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(mDividerItemDecoration);
        }catch (Exception e) {
            AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
        }
    }


    class TempAdapter extends RecyclerView.Adapter<TempAdapter.MyViewHolder> {

        private JSONArray jsonArray;

        class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView dayTV;
            public TextView highTemp;
            public TextView lowTemp;

            public MyViewHolder(View view) {
                super(view);
                dayTV= (TextView) view.findViewById(R.id.txt_module_title);
                highTemp = (TextView) view.findViewById(R.id.temp_high);
                lowTemp = (TextView) view.findViewById(R.id.temp_low);
            }
        }

        public TempAdapter(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.temp_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject((position + 1));
                JSONObject tempObject = jsonObject.getJSONObject("Temperature");
                holder.dayTV.setText(AppUtils.getWeekDay(AppUtils.getDateFormat(jsonObject.getString("Date"))));
                holder.highTemp.setText(tempObject.getJSONObject("Maximum").get("Value") + AppUtils.getTempSign(tempUnitType));
                holder.lowTemp.setText(tempObject.getJSONObject("Minimum").get("Value") + AppUtils.getTempSign(tempUnitType));
            }catch (Exception e){
                AppUtils.doLogError(AppConstants.ERROR_TAG, e.getMessage());
            }

        }

        @Override
        public int getItemCount() {
            return jsonArray.length() -1;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(weatherTask != null && weatherTask.getStatus() == AsyncTask.Status.RUNNING) {
            weatherTask.cancel(true);
        }
    }

}
