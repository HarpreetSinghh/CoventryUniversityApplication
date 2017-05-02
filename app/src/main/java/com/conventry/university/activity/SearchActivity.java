package com.conventry.university.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conventry.university.MyApplication;
import com.conventry.university.R;
import com.conventry.university.beans.ModulesDir;
import com.conventry.university.beans.ModulesFiles;
import com.conventry.university.task.PDFReaderTask;
import com.conventry.university.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final int PERMISSION_READ_WRITE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AutoCompleteTextView txtSearch = (AutoCompleteTextView) findViewById(R.id.search_edt);
        txtSearch.setThreshold(1);
        List<ModulesFiles> moduleFiles = new ArrayList<>();
        for(ModulesDir modulesDir : ModulesDir.getModuleFiles()) {
            moduleFiles.addAll(modulesDir.getFiles());
        }
        SearchFieldAdapter adapter = new SearchFieldAdapter(this, R.layout.activity_search, R.id.txt_module_title, moduleFiles);
        txtSearch.setAdapter(adapter);
        getPermission();
    }

    public void getPermission() {
        ActivityCompat.requestPermissions(SearchActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_WRITE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_READ_WRITE:{
                if(grantResults.length>0){
                    boolean read=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean write=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(read && write){
                    }
                }
            }
        }
    }

    class SearchFieldAdapter extends ArrayAdapter<ModulesFiles> {

        private Context context;
        private int resource, textViewResourceId;
        private List<ModulesFiles> files, tempItems, suggestions;

        public SearchFieldAdapter(Context context, int resource, int textViewResourceId, List<ModulesFiles> files) {
            super(context, resource, textViewResourceId, files);
            this.context = context;
            this.resource = resource;
            this.textViewResourceId = textViewResourceId;
            this.files = files;
            tempItems = files; // this makes the difference.
            suggestions = new ArrayList<>();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) MyApplication.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.module_item_layout, parent, false);
            }
            ModulesFiles file = files.get(position);
            if (file != null) {
                TextView lblName = (TextView) view.findViewById(R.id.txt_module_title);
                ImageView fileIcon = (ImageView) view.findViewById(R.id.file_icon);
                LinearLayout outerLL = (LinearLayout) view.findViewById(R.id.module_outer_layout);
                fileIcon.setImageResource(R.mipmap.pdf);
                lblName.setText(file.getFilename());
                outerLL.setTag(file);
                outerLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View clickView) {
                        ModulesFiles openFile = (ModulesFiles) clickView.getTag();
                        AppUtils.hideSoftKeyboard(SearchActivity.this);
                        new PDFReaderTask(SearchActivity.this).execute(openFile.getDirTitle(),openFile.getFilename());
                    }
                });
            }
            return view;
        }

        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = ((ModulesFiles) resultValue).getFilename();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (ModulesFiles file : tempItems) {
                        if (file.getFilename().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            suggestions.add(file);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<ModulesFiles> filterList = (ArrayList<ModulesFiles>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (ModulesFiles file : filterList) {
                        add(file);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }
}
