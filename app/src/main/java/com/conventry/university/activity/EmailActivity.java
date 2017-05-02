package com.conventry.university.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conventry.university.R;
import com.conventry.university.beans.Email;
import com.conventry.university.database.DatabaseManager;
import com.conventry.university.utils.AppUtils;
import com.conventry.university.utils.SharePrefernceUtil;

import java.util.List;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        setAdapter();
    }

    private void setAdapter() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.email_list_view);

        EmailAdapter mAdapter = new EmailAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.logout_menu:   //this item has your app icon
                SharePrefernceUtil.setIsEmailLoggedIn(false);
                onBackPressed();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.MyViewHolder> {

        private List<Email> emails = null;

         class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView fromText, subject, message, timestamp;

            public MyViewHolder(View view) {
                super(view);
                fromText = (TextView ) view.findViewById(R.id.txt_email_from);
                subject = (TextView ) view.findViewById(R.id.txt_subject_email);
                message = (TextView ) view.findViewById(R.id.txt_message_email);
                timestamp = (TextView ) view.findViewById(R.id.txt_sent_date);
            }
        }

        public EmailAdapter() {
            emails = DatabaseManager.getInstance().getEmailList();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.email_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Email email = emails.get(position);
            holder.fromText.setText(email.getFrom());
            holder.subject.setText(email.getSubject());
            holder.message.setText(email.getMessage());
            holder.timestamp.setText(AppUtils.getDateEmailView(email.getSentDate()));
        }

        @Override
        public int getItemCount() {
            return emails.size();
        }
    }
}
