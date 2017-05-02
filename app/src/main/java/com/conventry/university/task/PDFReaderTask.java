package com.conventry.university.task;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.conventry.university.MyApplication;
import com.conventry.university.utils.AppConstants;
import com.conventry.university.utils.AppUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PDFReaderTask extends AsyncTask<String,String,String> {

    private ProgressDialog progressDialog;

    private AssetManager assetManager = null;

    private Activity activity;

    public PDFReaderTask(Activity activity) {
        this.activity = activity;
        progressDialog=new ProgressDialog(activity);
        assetManager = activity.getAssets();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Loading file");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String dirName=params[0];
        String filename=params[1];

        InputStream in = null;
        FileOutputStream out = null;
        File file = new File(Environment.getExternalStoragePublicDirectory(""), filename);
        if(!file.exists()){
            try {
                file.createNewFile();
                in = assetManager.open("modules/"+dirName+"/"+filename);
                out = new FileOutputStream(file);
                int read=-1;
                while((read=in.read())!=-1){
                    out.write(read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (Exception e) {
                AppUtils.doLogError(AppConstants.ERROR_TAG, e.getLocalizedMessage());
            }
        }
        return filename;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if(progressDialog != null || progressDialog.isShowing())
        progressDialog.cancel();
    }

    @Override
    protected void onPostExecute(String filename) {
        super.onPostExecute(filename);
        if(progressDialog != null || progressDialog.isShowing())
            progressDialog.cancel();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + Environment.getExternalStoragePublicDirectory("")+ "/"+filename),
                "application/pdf");
        activity.startActivity(intent);
    }
}
