package com.conventry.university.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.conventry.university.beans.ModulesFiles;
import com.conventry.university.task.PDFReaderTask;
import com.conventry.university.utils.AppUtils;
import com.conventry.university.utils.FileType;

import java.util.List;

public class LecturesActivity extends AppCompatActivity {

    private static final int PERMISSION_READ_WRITE = 100;

    private TextView lectureTV;

    private ModulesDir modules;



    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectures);
        getPermission();
        setAdapter();
    }

    private void setAdapter() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            modules = (ModulesDir) bundle.getSerializable("module");
            lectureTV = (TextView) findViewById(R.id.lectures_title);
            lectureTV.setText(modules.getFolderTitle());
            recyclerView = (RecyclerView) findViewById(R.id.lectures_list_view);
            LectureAdapter mAdapter = new LectureAdapter(modules);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }
    }

    class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.MyViewHolder> {

        private List<ModulesFiles> moduleFiles = null;

        private ModulesDir modulesDir;

        class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView fileIV;
            public TextView titleTV;
            public LinearLayout outerLL;

            public MyViewHolder(View view) {
                super(view);
                titleTV = (TextView) view.findViewById(R.id.txt_module_title);
                outerLL = (LinearLayout) view.findViewById(R.id.module_outer_layout);
                fileIV =  (ImageView) view.findViewById(R.id.file_icon);
            }
        }

        public LectureAdapter(ModulesDir modulesDir) {
            this.modulesDir = modulesDir;
            moduleFiles = modulesDir.getFiles();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.module_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ModulesFiles module = moduleFiles.get(position);
            holder.titleTV.setText(AppUtils.getFilenameWithoutExtension(module.getFilename()));
            holder.outerLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new PDFReaderTask(LecturesActivity.this).execute(module.getDirTitle(),module.getFilename());
                }
            });
            setIcon(module.getType(),holder.fileIV);
            holder.titleTV.setPaintFlags(holder.titleTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        }

        @Override
        public int getItemCount() {
            return moduleFiles.size();
        }


    }

    private void setIcon(FileType fileType, ImageView fileIcon){
        if(FileType.PDF_FILE == fileType) {
            fileIcon.setImageResource(R.mipmap.pdf);
        }}

    public void getPermission() {
        ActivityCompat.requestPermissions(LecturesActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_WRITE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_READ_WRITE:{
                if(grantResults.length>0){
                    boolean read=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean write=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(read && write){
                    }
                }
            }
        }
    }


}

