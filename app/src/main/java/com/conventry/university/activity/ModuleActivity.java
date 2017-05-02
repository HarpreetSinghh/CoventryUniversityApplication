package com.conventry.university.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.conventry.university.R;
import com.conventry.university.beans.ModulesDir;

import java.util.List;

public class ModuleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);
        setAdapter();

        TextView  announcements = (TextView) findViewById(R.id.announcementTV);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.marquee_animation);
        announcements.startAnimation(shake);
    }

    public void setAdapter(){
        recyclerView = (RecyclerView) findViewById(R.id.modules_list_view);
        ModuleAdapter mAdapter = new ModuleAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.MyViewHolder> {

        private List<ModulesDir> modules = null;

        class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView folderIV;
            public TextView titleTV;
            public LinearLayout outerLL;

            public MyViewHolder(View view) {
                super(view);
                titleTV= (TextView) view.findViewById(R.id.txt_module_title);
                outerLL = (LinearLayout) view.findViewById(R.id.module_outer_layout);
            }
        }

        public ModuleAdapter() {
            modules = ModulesDir.getModuleFiles();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.module_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ModulesDir module = modules.get(position);
            holder.titleTV.setText(module.getFolderTitle());
            holder.outerLL.setTag(module);
            holder.titleTV.setPaintFlags(holder.titleTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

            holder.outerLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModulesDir modulesDir =  (ModulesDir) view.getTag();
                    Intent intent  = new Intent(ModuleActivity.this, LecturesActivity.class);
                    intent.putExtra("module", modulesDir);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return modules.size();
        }
    }
}
